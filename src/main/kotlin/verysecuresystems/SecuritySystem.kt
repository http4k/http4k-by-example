package verysecuresystems

import org.http4k.core.Events
import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters
import org.http4k.filter.HandleUpstreamRequestFailed
import org.http4k.filter.ServerFilters
import org.http4k.routing.ResourceLoader.Companion.Classpath
import org.http4k.routing.routes
import org.http4k.routing.static
import verysecuresystems.api.Api
import verysecuresystems.diagnostic.Auditor
import verysecuresystems.diagnostic.Diagnostic
import verysecuresystems.external.EntryLogger
import verysecuresystems.external.UserDirectory
import verysecuresystems.oauth.SecurityServerAuthProvider
import verysecuresystems.web.Web
import java.time.Clock

/**
 * Sets up the business-level API for the application. Note that the generic clients on the constructor allow us to
 * inject non-HTTP versions of the downstream dependencies so we can run tests without starting up real HTTP servers.
 */
object SecuritySystem {
    operator fun invoke(clock: Clock, events: Events,
                        oauthCallbackUri: Uri,
                        oauthServerUri: Uri,
                        oauthServerHttp: HttpHandler,
                        userDirectoryHttp: HttpHandler,
                        entryLoggerHttp: HttpHandler): HttpHandler {

        val inhabitants = Inhabitants()
        val oAuthProvider = SecurityServerAuthProvider(oauthCallbackUri, oauthServerUri, oauthServerHttp, clock)

        val userDirectory = UserDirectory(ClientFilters.RequestTracing()
            .then(Auditor.Outgoing(clock, events))
            .then(userDirectoryHttp))

        val entryLogger = EntryLogger(ClientFilters.RequestTracing()
            .then(Auditor.Outgoing(clock, events))
            .then(entryLoggerHttp), clock)

        // we compose the various route blocks together here
        val app = routes(
            Api(userDirectory, entryLogger, inhabitants, oAuthProvider),
            Diagnostic(clock),
            Web(clock, oAuthProvider, userDirectory),
            static(Classpath("public"))
        )

        // Create the application "stack", including inbound auditing
        return Auditor.Incoming(clock, events)
            .then(ServerFilters.CatchAll())
            .then(ServerFilters.HandleUpstreamRequestFailed())
            .then(ServerFilters.RequestTracing())
            .then(app)
    }
}


