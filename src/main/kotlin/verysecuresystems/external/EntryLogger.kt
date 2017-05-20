package verysecuresystems.external

import org.http4k.contract.Route
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import verysecuresystems.UserEntry
import verysecuresystems.Username
import java.time.Clock


class EntryLogger(private val client: HttpHandler, private val clock: Clock) {

    fun enter(username: Username): UserEntry =
        client(
            Entry.route.newRequest()
                .with(Entry.body of UserEntry(username.value, true, clock.instant().toEpochMilli())),
            Entry.response)

    fun exit(username: Username): UserEntry =
        client(
            Exit.route.newRequest()
                .with(Exit.body of UserEntry(username.value, false, clock.instant().toEpochMilli())),
            Exit.response)


    fun list(): List<UserEntry> = client(LogList.route.newRequest(), LogList.response)

    companion object {

        object Entry {
            val body = Body.auto<UserEntry>().toLens()
            val route = Route().body(body).at(POST) / "entry"
            val response = Body.auto<UserEntry>().toLens()
        }

        object Exit {
            val body = Body.auto<UserEntry>().toLens()
            val route = Route().body(body).at(POST) / "exit"
            val response = Body.auto<UserEntry>().toLens()
        }

        object LogList {
            val body = Body.auto<List<UserEntry>>().toLens()
            val route = Route().at(GET) / "list"
            val response = Body.auto<List<UserEntry>>().toLens()
        }

    }

}