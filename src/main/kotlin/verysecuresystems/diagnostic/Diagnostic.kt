package verysecuresystems.diagnostic

import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.routes
import java.time.Clock

object Diagnostic {
    operator fun invoke(clock: Clock): RoutingHttpHandler = routes(Ping(), Uptime(clock))
}
