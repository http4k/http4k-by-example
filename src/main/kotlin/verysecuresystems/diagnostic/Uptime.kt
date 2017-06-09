package verysecuresystems.diagnostic

import org.http4k.contract.ContractRoute
import org.http4k.contract.bind
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import java.time.Clock

object Uptime {
    fun route(clock: Clock): ContractRoute {
        val startTime = clock.instant().toEpochMilli()
        return "/uptime" to Method.GET bind {
            Response(Status.OK).body("uptime is: ${(clock.instant().toEpochMilli() - startTime) / 1000}s")
        }
    }
}