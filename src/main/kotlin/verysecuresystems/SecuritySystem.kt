package verysecuresystems

import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters
import org.http4k.filter.ServerFilters
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
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
                        userDirectoryClient: Pair<Uri, HttpHandler>,
                        entryLoggerClient: Pair<Uri, HttpHandler>): HttpHandler {
        val userDirectory = UserDirectory(userDirectoryClient.toAutoSetHost())
        val entryLogger = EntryLogger(entryLoggerClient.toAutoSetHost(), clock)
        val inhabitants = Inhabitants()

        val app = routes(
            "/api" bind  Api.router(userDirectory, entryLogger, inhabitants),
            "/internal" bind Diagnostic.router(clock),
            "/" bind Web.router(userDirectory),
            "/" bind static(ResourceLoader.Classpath("public"))
        )

        return Auditor(clock, events)
            .then(ServerFilters.CatchAll())
            .then(ServerFilters.CatchLensFailure)
            .then(app)
    }

    private fun Pair<Uri, HttpHandler>.toAutoSetHost() = ClientFilters.SetHostFrom(first).then(second)
}
