package verysecuresystems.external

import org.http4k.contract.bindContract
import org.http4k.contract.meta
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.then
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import verysecuresystems.UserEntry
import verysecuresystems.Username
import verysecuresystems.util.RequireSuccess
import java.time.Clock


class EntryLogger(http: HttpHandler, private val clock: Clock) {

    private val http = RequireSuccess.then(http)

    fun enter(username: Username) = with(Entry) {
        response(
            http(route.newRequest()
                .with(body of UserEntry(username.value, true, clock.instant().toEpochMilli()))
            )
        )
    }

    fun exit(username: Username) = with(Exit) {
        response(
            http(
                route.newRequest()
                    .with(body of UserEntry(username.value, false, clock.instant().toEpochMilli()))
            )
        )
    }

    fun list() = with(LogList) { response(http(route.newRequest())) }

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