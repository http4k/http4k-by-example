package env

import env.entrylogger.FakeEntryLogger
import env.userdirectory.FakeUserDirectory
import org.http4k.core.Event
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.with
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

    val app = SecuritySystem(
        clock,
        { events.add(it) },
        userDirectory,
        entryLogger
    )}

private val username = Query.optional("username")
private val key = Header.required("key")

fun TestEnvironment.enterBuilding(user: String?, secret: String): Response =
    app(Request(POST, "/api/knock").with(username of user, key of secret))

fun TestEnvironment.exitBuilding(user: String?, secret: String): Response =
    app(Request(POST, "/api/bye").with(username of user, key of secret))

fun TestEnvironment.checkInhabitants(secret: String): Response =
    app(Request(GET, "/api/whoIsThere").with(key of secret))
