package verysecuresystems.diagnostic

import org.http4k.contract.Route
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status

object Ping {
    fun route() = Route("Ping").at(Method.GET) / "ping" bind { Response(Status.OK).body("pong") }
}