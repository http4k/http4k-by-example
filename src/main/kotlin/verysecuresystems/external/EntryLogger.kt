package verysecuresystems.external

import org.http4k.contract.bindContract
import org.http4k.contract.meta
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

    fun enter(username: Username) = with(Entry) {
        client.perform(
                route.newRequest()
                        .with(body of UserEntry(username.value, true, clock.instant().toEpochMilli())),
                response)
    }

    fun exit(username: Username) = with(Exit) {
        client.perform(
                route.newRequest()
                        .with(body of UserEntry(username.value, false, clock.instant().toEpochMilli())),
                response)
    }

    fun list(): List<UserEntry> = client.perform(LogList.route.newRequest(), LogList.response)

    companion object {
        object Entry {
            val body = Body.auto<UserEntry>().toLens()
            val route = "/entry" meta { receiving(body to UserEntry("user", true, 1234)) } bindContract POST
            val response = Body.auto<UserEntry>().toLens()
        }

        object Exit {
            val body = Body.auto<UserEntry>().toLens()
            val route = "/exit" meta { receiving(body to UserEntry("user", true, 1234)) } bindContract POST
            val response = Body.auto<UserEntry>().toLens()
        }

        object LogList {
            val body = Body.auto<List<UserEntry>>().toLens()
            val route = "/list" bindContract GET
            val response = Body.auto<List<UserEntry>>().toLens()
        }
    }

}