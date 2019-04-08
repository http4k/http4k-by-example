package verysecuresystems.external

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.then
import org.http4k.core.with
import org.http4k.filter.ClientFilters
import org.http4k.filter.HandleUpstreamRequestFailed
import org.http4k.format.Jackson.auto
import verysecuresystems.UserEntry
import verysecuresystems.Username
import java.time.Clock


class EntryLogger(http: HttpHandler, private val clock: Clock) {

    private val http = ClientFilters.HandleUpstreamRequestFailed().then(http)

    private val body = Body.auto<UserEntry>().toLens()
    private val userEntry = Body.auto<UserEntry>().toLens()
    private val userEntries = Body.auto<List<UserEntry>>().toLens()

    fun enter(username: Username) =
        userEntry(
            http(Request(POST, "/entry")
                .with(body of UserEntry(username.value, true, clock.instant().toEpochMilli()))
            )
        )

    fun exit(username: Username) =
        userEntry(
            http(
                Request(POST, "/exit")
                    .with(body of UserEntry(username.value, false, clock.instant().toEpochMilli()))
            )
        )

    fun list() = userEntries(http(Request(GET, "/list")))
}