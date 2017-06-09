package verysecuresystems.diagnostic

import org.http4k.contract.SimpleJson
import org.http4k.contract.contract
import org.http4k.format.Jackson
import org.http4k.routing.RoutingHttpHandler
import java.time.Clock

object Diagnostic {
    fun router(clock: Clock): RoutingHttpHandler = contract(SimpleJson(Jackson),
        Ping.route(),
        Uptime.route(clock))
}
