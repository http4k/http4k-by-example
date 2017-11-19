package verysecuresystems.diagnostic

import org.http4k.contract.bindContract
import org.http4k.contract.meta
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status

object Ping {
    fun route() = "/ping" meta {
        summary = "ping"
    } bindContract Method.GET to { Response(Status.OK).body("pong") }
}