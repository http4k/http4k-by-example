package verysecuresystems

import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.events.EventFilters.AddTimestamp
import org.http4k.events.EventFilters.AddZipkinTraces
import org.http4k.events.Events
import org.http4k.events.then
import org.http4k.filter.ClientFilters.RequestTracing
import org.http4k.filter.ClientFilters.SetHostFrom
import org.http4k.filter.HandleRemoteRequestFailed
import org.http4k.filter.ServerFilters
import org.http4k.routing.ResourceLoader.Companion.Classpath
import org.http4k.routing.routes
import org.http4k.routing.static
import verysecuresystems.api.Api
import verysecuresystems.diagnostic.Auditor
import verysecuresystems.diagnostic.Diagnostic
import verysecuresystems.external.EntryLogger
import verysecuresystems.external.UserDirectory
import verysecuresystems.oauth.SecurityServerOAuthProvider
import verysecuresystems.web.Web
import java.time.Clock

/**
 * Sets up the business-level API for the application. Note that the generic clients on the constructor allow us to
 * inject non-HTTP versions of the downstream dependencies so we can run tests without starting up real HTTP servers.
 */
fun SecuritySystem(clock: Clock,
                   events: Events,
                   http: HttpHandler,
                   oauthCallbackUri: Uri,
                   oauthServerUri: Uri,
                   userDirectoryUri: Uri,
                   entryLoggerUri: Uri): HttpHandler {
    val timedEvents = AddZipkinTraces()
        .then(AddTimestamp(clock))
        .then(events)

    val timedHttp = RequestTracing().then(Auditor.Outgoing(timedEvents)).then(http)

    val inhabitants = Inhabitants()
    val oAuthProvider = SecurityServerOAuthProvider(oauthCallbackUri, oauthServerUri, SetHostFrom(oauthServerUri).then(timedHttp), clock)

    val userDirectory = UserDirectory(SetHostFrom(userDirectoryUri).then(timedHttp))

    val entryLogger = EntryLogger(SetHostFrom(entryLoggerUri).then(timedHttp), clock)

    // we compose the various route blocks together here
    val app = routes(
        Api(userDirectory, entryLogger, inhabitants, oAuthProvider),
        Diagnostic(clock),
        Web(clock, oAuthProvider, userDirectory),
        static(Classpath("public"))
    )

    // Create the application "stack", including inbound auditing
    return ServerFilters.CatchAll()
        .then(ServerFilters.RequestTracing())
        .then(Auditor.Incoming(timedEvents))
        .then(ServerFilters.HandleRemoteRequestFailed())
        .then(app)
}

