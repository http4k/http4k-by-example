package verysecuresystems.diagnostic

import org.http4k.contract.Route
import org.http4k.contract.ServerRoute
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import java.time.Clock

object Uptime {
    fun route(clock: Clock): ServerRoute {
        val startTime = clock.instant().toEpochMilli()
        return Route("Uptime monitor").at(Method.GET) / "uptime" bind {
            Response(Status.OK).body("uptime is: ${(clock.instant().toEpochMilli() - startTime) / 1000}s")
        }
    }
}