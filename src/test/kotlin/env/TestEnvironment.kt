package env

import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
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
        { Response(OK) },
        { Response(OK) }
    )
}

fun TestEnvironment.enterBuilding(user: String?, secret: String): Response {
    val query = user?.let { "username=" + it } ?: ""
    val request = Request(POST, "/security/knock?" + query)
    request.header("key", secret)
    return app(request)
}

fun TestEnvironment.exitBuilding(user: String?, secret: String): Response {
    val query = user?.let { "username=" + it } ?: ""
    val request = Request(POST, "/security/bye?" + query)
    request.header("key", secret)
    return app(request)
}

fun TestEnvironment.checkInhabitants(secret: String): Response {
    val request = Request(GET, "/security/whoIsThere")
    request.header("key", secret)
    return app(request)
}
