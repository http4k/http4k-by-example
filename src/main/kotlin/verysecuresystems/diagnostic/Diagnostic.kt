package verysecuresystems.diagnostic

import org.http4k.contract.SimpleJson
import org.http4k.format.Jackson
import org.http4k.routing.contract
import java.time.Clock

object Diagnostic {
    fun router(clock: Clock) = contract(SimpleJson(Jackson))
        .withRoute(Ping.route())
        .withRoute(Uptime.route(clock))
}
