import org.http4k.core.Response
import org.http4k.core.Status
import verysecuresystems.Event
import verysecuresystems.SecuritySystem
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class TestEnvironment {

    val clock = Clock.fixed(Instant.ofEpochMilli(0), ZoneId.systemDefault())!!

//  val userDirectory = FakeUserDirectory()
//  val entryLogger = FakeEntryLogger()
//
//  val userDirectoryHttp = OverridableHttpService[Response](userDirectory)
//  val entryLoggerHttp = OverridableHttpService[Response](entryLogger)

    val events = mutableListOf<Event>()

    val app = SecuritySystem(
        clock,
        { events.add(it) },
        { Response(Status.OK) },
        { Response(Status.OK) }
    )
}