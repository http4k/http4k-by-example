package verysecuresystems.diagnostic

import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.RouteMeta
import org.http4k.routing.handler
import org.http4k.routing.meta

object Ping {
    fun route() = "/ping" to Method.GET handler { Response(Status.OK).body("pong") } meta RouteMeta("ping")
}