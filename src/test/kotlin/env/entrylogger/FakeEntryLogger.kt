package env.entrylogger

import org.http4k.chaos.withChaosEngine
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import org.http4k.routing.bind
import org.http4k.routing.routes
import verysecuresystems.UserEntry

class FakeEntryLogger : HttpHandler {

    val entries = mutableListOf<UserEntry>()

    private fun list(): HttpHandler {
        val userEntry = Body.auto<UserEntry>().toLens()
        return { req ->
            val entry = userEntry(req)
            entries += entry
            Response(CREATED).with(userEntry of entry)
        }
    }

    private fun entry(): HttpHandler {
        val userEntry = Body.auto<UserEntry>().toLens()
        return { req ->
            val entry = userEntry(req)
            entries += entry
            Response(CREATED).with(userEntry of entry)
        }
    }

    private fun exit(): HttpHandler {
        val userEntry = Body.auto<UserEntry>().toLens()
        return { req ->
            val entry = userEntry(req)
            entries += entry
            Response(ACCEPTED).with(userEntry of entry)
        }
    }

    private val app = routes(
        "/list" bind GET to list(),
        "/entry" bind POST to entry(),
        "/exit" bind POST to exit()
    ).withChaosEngine()

    override fun invoke(p1: Request) = app(p1)
}
