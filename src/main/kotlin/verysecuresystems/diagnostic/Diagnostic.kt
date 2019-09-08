package verysecuresystems.diagnostic

import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import java.time.Clock

/**
 * The internal monitoring API.
 */
fun Diagnostic(clock: Clock): RoutingHttpHandler = "/internal" bind routes(
    Ping(),
    Uptime(clock),
    "/" bind GET to { Response(OK).body("diagnostic module. visit: /ping or /uptime") }
)
