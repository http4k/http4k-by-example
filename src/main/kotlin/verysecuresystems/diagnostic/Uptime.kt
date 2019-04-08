package verysecuresystems.diagnostic

import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import java.time.Clock

object Uptime {
    operator fun invoke(clock: Clock): RoutingHttpHandler {
        val startTime = clock.instant().toEpochMilli()
        return "/uptime" bind Method.GET to {
            Response(Status.OK).body("uptime is: ${(clock.instant().toEpochMilli() - startTime) / 1000}s")
        }
    }
}