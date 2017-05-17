fun main() {
  val serverPort = 9999
  val userDirectoryPort = 10000
  val entryLoggerPort = 10001

//  val userDirectory = FakeUserDirectory()
//  userDirectory.contains(User(Id(0), Username("Bob"), EmailAddress("bob@bob.com")))
//  userDirectory.contains(User(Id(1), Username("Rita"), EmailAddress("rita@bob.com")))
//  userDirectory.contains(User(Id(2), Username("Sue"), EmailAddress("sue@bob.com")))
//
//  TestHttpServer(userDirectoryPort, userDirectory).start()
//  TestHttpServer(entryLoggerPort, FakeEntryLogger()).start()
//
//  SecuritySystemServer(Port(serverPort),
//    Host.localhost.toAuthority(Port(userDirectoryPort)),
//    Host.localhost.toAuthority(Port(entryLoggerPort))
//  ).start()
//
//  Thread.currentThread().join()
}


/*
interface RunningTestEnvironment {
    val env = TestEnvironment()

    override protected def beforeEach() =
    {
        env = TestEnvironment()
    }
}
import java.time.{Clock, Instant, ZoneId}

class TestEnvironment() {

  val clock = Clock.fixed(Instant.ofEpochMilli(0), ZoneId.systemDefault())

  val userDirectory = FakeUserDirectory()
  val entryLogger = FakeEntryLogger()

  val userDirectoryHttp = OverridableHttpService[Response](userDirectory)
  val entryLoggerHttp = OverridableHttpService[Response](entryLogger)

  val events = mutable.MutableList[Event]()

  private val securitySystemSvc = SecuritySystem(
    userDirectoryHttp.service,
    entryLoggerHttp.service,
    events += _,
    clock).service

  def responseTo(request: Request) = {
    val msg = Await.result(securitySystemSvc(request))
    ResponseStatusAndContent(msg.status, msg.headerMap.getOrElse("Content-type", null), msg.contentString)
  }
}
 */