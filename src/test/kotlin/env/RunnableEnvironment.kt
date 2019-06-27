package env

import env.entrylogger.FakeEntryLogger
import env.oauthserver.OAuthClientData
import env.oauthserver.SimpleOAuthServer
import env.userdirectory.FakeUserDirectory
import org.http4k.cloudnative.env.Environment
import org.http4k.core.Credentials
import org.http4k.core.Uri
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import verysecuresystems.EmailAddress
import verysecuresystems.Id
import verysecuresystems.SecuritySystemServer
import verysecuresystems.Settings.ENTRY_LOGGER_URL
import verysecuresystems.Settings.OAUTH_SERVER_URL
import verysecuresystems.Settings.PORT
import verysecuresystems.Settings.SECURITY_SERVER_URL
import verysecuresystems.Settings.USER_DIRECTORY_URL
import verysecuresystems.User
import verysecuresystems.Username

fun main() {
    val securityServerPort = 9000
    val userDirectoryPort = 10000
    val entryLoggerPort = 11000
    val oauthServerPort = 12000

    FakeUserDirectory().apply {
        contains(User(Id(0), Username("Bob"), EmailAddress("bob@bob.com")))
        contains(User(Id(1), Username("Rita"), EmailAddress("rita@bob.com")))
        contains(User(Id(2), Username("Sue"), EmailAddress("sue@bob.com")))
    }.asServer(SunHttp(userDirectoryPort)).start()

    FakeEntryLogger().asServer(SunHttp(entryLoggerPort)).start()

    SimpleOAuthServer(
        Credentials("user", "password"),
        OAuthClientData(Credentials("securityServer", "securityServerSecret"),
            Uri.of("http://localhost:$securityServerPort/openapi/oauth2-redirect.html")
        ),
        OAuthClientData(Credentials("securityServer", "securityServerSecret"),
            Uri.of("http://localhost:$securityServerPort/api/oauth/callback")
        )
    ).asServer(SunHttp(oauthServerPort)).start()

    val env = Environment.defaults(PORT of securityServerPort,
        SECURITY_SERVER_URL of Uri.of("http://localhost:$securityServerPort"),
        USER_DIRECTORY_URL of Uri.of("http://localhost:$userDirectoryPort"),
        ENTRY_LOGGER_URL of Uri.of("http://localhost:$entryLoggerPort"),
        OAUTH_SERVER_URL of Uri.of("http://localhost:$oauthServerPort")
    )
    SecuritySystemServer(env).start()
}
