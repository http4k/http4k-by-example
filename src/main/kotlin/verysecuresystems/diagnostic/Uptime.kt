package verysecuresystems.diagnostic

import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import java.time.Clock

fun Uptime(clock: Clock): RoutingHttpHandler {
    val startTime = clock.instant().toEpochMilli()
    return "/uptime" bind GET to {
        Response(OK).body("uptime is: ${(clock.instant().toEpochMilli() - startTime) / 1000}s")
    }
}