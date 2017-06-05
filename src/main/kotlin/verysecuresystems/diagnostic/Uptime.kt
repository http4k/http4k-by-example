package verysecuresystems.diagnostic

import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.ServerRoute
import org.http4k.routing.handler
import java.time.Clock

object Uptime {
    fun route(clock: Clock): ServerRoute {
        val startTime = clock.instant().toEpochMilli()
        return "/uptime" to Method.GET handler {
            Response(Status.OK).body("uptime is: ${(clock.instant().toEpochMilli() - startTime) / 1000}s")
        }
    }
}