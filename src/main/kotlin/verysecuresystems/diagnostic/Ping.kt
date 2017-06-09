package verysecuresystems.diagnostic

import org.http4k.contract.RouteMeta
import org.http4k.contract.bind
import org.http4k.contract.meta
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status

object Ping {
    fun route() = "/ping" to Method.GET bind { Response(Status.OK).body("pong") } meta RouteMeta("ping")
}