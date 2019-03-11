package env

import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Uri
import org.http4k.core.with
import org.http4k.lens.Header
import org.http4k.lens.Query
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
            Uri.of("http://userDirectory") to userDirectory.app,
            Uri.of("http://entryLogger") to entryLogger.app
    )
}

private val username = Query.optional("username")
private val key = Header.required("key")

fun TestEnvironment.enterBuilding(user: String?, secret: String): Response {
    val app1 = app(Request(POST, "/api/knock").with(username of user, key of secret))
    println(app1)
    return app1
}

fun TestEnvironment.exitBuilding(user: String?, secret: String): Response {
    return app(Request(POST, "/api/bye").with(username of user, key of secret))
}

fun TestEnvironment.checkInhabitants(secret: String): Response =
        app(Request(GET, "/api/whoIsThere").with(key of secret))
