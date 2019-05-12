package env

import org.http4k.cloudnative.env.Environment
import org.http4k.core.Uri
import org.http4k.server.Undertow
import org.http4k.server.asServer
import verysecuresystems.EmailAddress
import verysecuresystems.Id
import verysecuresystems.SecuritySystemServer
import verysecuresystems.Settings.ENTRY_LOGGER_URL
import verysecuresystems.Settings.PORT
import verysecuresystems.Settings.USER_DIRECTORY_URL
import verysecuresystems.User
import verysecuresystems.Username

fun main() {
    val userDirectoryPort = 10000
    val entryLoggerPort = 11000

    val userDirectory = FakeUserDirectory().apply {
        contains(User(Id(0), Username("Bob"), EmailAddress("bob@bob.com")))
        contains(User(Id(1), Username("Rita"), EmailAddress("rita@bob.com")))
        contains(User(Id(2), Username("Sue"), EmailAddress("sue@bob.com")))
    }

    userDirectory.asServer(Undertow(userDirectoryPort)).start()
    FakeEntryLogger().asServer(Undertow(entryLoggerPort)).start()

    val env = Environment.defaults(PORT of 9000,
        USER_DIRECTORY_URL of Uri.of("http://localhost:$userDirectoryPort"),
        ENTRY_LOGGER_URL of Uri.of("http://localhost:$entryLoggerPort")
    )
    SecuritySystemServer(env).start()
}
