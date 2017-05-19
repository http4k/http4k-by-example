package verysecuresystems

import org.http4k.contract.Root
import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters
import org.http4k.filter.ServerFilters.CatchAll
import verysecuresystems.api.Api
import verysecuresystems.diagnostic.Auditor
import verysecuresystems.diagnostic.Diagnostic
import verysecuresystems.external.EntryLogger
import verysecuresystems.external.UserDirectory
import verysecuresystems.web.Web
import java.time.Clock

/**
 * Sets up the business-level API for the application. Note that the generic clients on the constructor allow us to
 * inject non-HTTP versions of the downstream dependencies so we can run tests without starting up real HTTP servers.
 */
object SecuritySystem {

    operator fun invoke(clock: Clock, events: Events,
                        userDirectoryClient: Pair<String, HttpHandler>,
                        entryLoggerClient: Pair<String, HttpHandler>): HttpHandler {
        val userDirectory = UserDirectory(userDirectoryClient.toAutoSetHost())
        val entryLogger = EntryLogger(entryLoggerClient.toAutoSetHost(), clock)
        val inhabitants = Inhabitants()

        val app = Api.module(Root / "api", userDirectory, entryLogger, inhabitants)
            .then(Diagnostic.module(Root / "internal", clock))
            .then(Web.module(Root, userDirectory))
            .toHttpHandler()

        return Auditor(clock, events)
            .then(CatchAll())
            .then(app)
    }

   private  fun Pair<String, HttpHandler>.toAutoSetHost() = ClientFilters.SetHostFrom(Uri.of(this.first)).then(this.second)
}