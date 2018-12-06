package env

import org.http4k.server.Jetty
import org.http4k.server.asServer
import verysecuresystems.*

fun main(args: Array<String>) {
    val serverPort = 9000
    val userDirectoryPort = 10000
    val entryLoggerPort = 11000

    val userDirectory = FakeUserDirectory().apply {
        contains(User(Id(0), Username("Bob"), EmailAddress("bob@bob.com")))
        contains(User(Id(1), Username("Rita"), EmailAddress("rita@bob.com")))
        contains(User(Id(2), Username("Sue"), EmailAddress("sue@bob.com")))
    }

    userDirectory.app.asServer(Jetty(userDirectoryPort)).start()
    FakeEntryLogger().app.asServer(Jetty(entryLoggerPort)).start()

    SecuritySystemServer(serverPort,
        "http://localhost:$userDirectoryPort",
        "http://localhost:$entryLoggerPort")
        .start().block()
}
