package verysecuresystems.diagnostic

import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind

object Ping {
    fun route() = "/ping" bind GET to { Response(OK).body("pong") }
}