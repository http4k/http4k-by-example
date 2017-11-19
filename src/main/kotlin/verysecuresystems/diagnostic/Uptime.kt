package verysecuresystems.diagnostic

import org.http4k.contract.ContractRoute
import org.http4k.contract.bindContract
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status
import java.time.Clock

object Uptime {
    fun route(clock: Clock): ContractRoute {
        val startTime = clock.instant().toEpochMilli()
        return "/uptime" bindContract GET to {
            Response(Status.OK).body("uptime is: ${(clock.instant().toEpochMilli() - startTime) / 1000}s")
        }
    }
}