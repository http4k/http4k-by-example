package verysecuresystems

import org.http4k.contract.Root
import org.http4k.core.HttpHandler
import org.http4k.core.then
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
    operator fun invoke(clock: Clock, events: Events, userDirectoryClient: HttpHandler, entryLoggerClient: HttpHandler): HttpHandler {

        val userDirectory = UserDirectory(userDirectoryClient)
        val entryLogger = EntryLogger(entryLoggerClient, clock)
        val inhabitants = Inhabitants()

        val app = Api.module(Root / "api", userDirectory, entryLogger, inhabitants)
            .then(Diagnostic.module(Root / "internal", clock))
            .then(Web.module(Root, userDirectory))
            .toHttpHandler()

        return Auditor(clock, events)
            .then(CatchAll())
            .then(app)
    }
}