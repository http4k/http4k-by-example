package verysecuresystems.diagnostic

import org.http4k.contract.BasePath
import org.http4k.contract.Module
import org.http4k.contract.RouteModule
import org.http4k.contract.SimpleJson
import org.http4k.format.Jackson
import java.time.Clock

object Diagnostic {
    fun module(path: BasePath, clock: Clock): Module = RouteModule(path, SimpleJson(Jackson))
        .withRoute(Ping.route())
        .withRoute(Uptime.route(clock))
}
