package verysecuresystems.diagnostic

import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.routes
import java.time.Clock

object Diagnostic {
    fun router(clock: Clock): RoutingHttpHandler = routes(Ping.route(), Uptime.route(clock))
}
