package env

import org.http4k.contract.bindContract
import org.http4k.contract.contract
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import verysecuresystems.UserEntry

class FakeEntryLogger : HttpHandler {

    private val userEntry = Body.auto<UserEntry>().toLens()
    private val userEntries = Body.auto<List<UserEntry>>().toLens()

    val entries = mutableListOf<UserEntry>()

    private val app = contract {
        routes += "/entry" bindContract Method.POST to { req: Request ->
            val entry = userEntry(req)
            entries += entry
            Response(CREATED).with(userEntry of entry)
        }
        routes += "/exit" bindContract Method.POST to { req: Request ->
            val entry = userEntry(req)
            entries += entry
            Response(ACCEPTED).with(userEntry of entry)
        }
        routes += "/list" bindContract Method.GET to {
            Response(Status.OK).with(userEntries of entries)
        }
    }

    override fun invoke(p1: Request) = app(p1)
}
