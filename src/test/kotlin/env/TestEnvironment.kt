package env

import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import verysecuresystems.Event
import verysecuresystems.SecuritySystem
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class TestEnvironment {

    val clock = Clock.fixed(Instant.ofEpochMilli(0), ZoneId.systemDefault())!!

    val userDirectory = FakeUserDirectory()
    val entryLogger = FakeEntryLogger()

    val events = mutableListOf<Event>()

    val app = SecuritySystem(
        clock,
        { events.add(it) },
        userDirectory.app,
        entryLogger.app
    )}

fun TestEnvironment.enterBuilding(user: String?, secret: String): Response {
    val query = user?.let { "username=" + it } ?: ""
    return app(Request(POST, "/api/knock?" + query).header("key", secret))
}

fun TestEnvironment.exitBuilding(user: String?, secret: String): Response {
    val query = user?.let { "username=" + it } ?: ""
    return app(Request(POST, "/api/bye?" + query).header("key", secret))
}

fun TestEnvironment.checkInhabitants(secret: String): Response =
    app(Request(GET, "/api/whoIsThere").header("key", secret))
