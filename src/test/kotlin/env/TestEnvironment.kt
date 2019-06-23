package env

import env.entrylogger.FakeEntryLogger
import env.userdirectory.FakeUserDirectory
import org.http4k.core.Event
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.core.with
import org.http4k.filter.DebuggingFilters
import org.http4k.lens.Header
import org.http4k.lens.Query
import verysecuresystems.SecuritySystem
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class TestEnvironment {
    val clock = Clock.fixed(Instant.ofEpochMilli(0), ZoneId.of("UTC"))!!

    val userDirectory = FakeUserDirectory()
    val entryLogger = FakeEntryLogger()

    private val events = mutableListOf<Event>()

    val app = DebuggingFilters.PrintRequestAndResponse()
            .then(
                SecuritySystem(
                    clock,
                    { events.add(it) },
                    Uri.of("http://oauth"),
                    { Response(OK) },
                    userDirectory,
                    entryLogger
                )
            )
}

private val username = Query.optional("username")
private val authorization = Header.optional("Authorization")

fun TestEnvironment.enterBuilding(user: String?, token: String): Response =
    app(Request(POST, "/api/knock").with(username of user, authorization of "Bearer $token"))

fun TestEnvironment.exitBuilding(user: String?, token: String): Response =
    app(Request(POST, "/api/bye").with(username of user, authorization of "Bearer $token"))

fun TestEnvironment.checkInhabitants(token: String): Response =
    app(Request(GET, "/api/whoIsThere").with(authorization of "Bearer $token"))
