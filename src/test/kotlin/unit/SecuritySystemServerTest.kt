package unit

import org.http4k.cloudnative.env.Environment
import org.http4k.core.Uri
import org.junit.jupiter.api.Test
import verysecuresystems.SecuritySystemServer
import verysecuresystems.Settings
import verysecuresystems.Settings.ENTRY_LOGGER_URL
import verysecuresystems.Settings.OAUTH_SERVER_URL
import verysecuresystems.Settings.SECURITY_SERVER_URL
import verysecuresystems.Settings.USER_DIRECTORY_URL

class SecuritySystemServerTest {

    @Test
    fun `can start and stop server`() {
        val securityServerPort = 9000
        val userDirectoryPort = 10000
        val entryLoggerPort = 11000
        val oauthServerPort = 12000

        val env = Environment.defaults(Settings.PORT of securityServerPort,
            SECURITY_SERVER_URL of Uri.of("http://localhost:$securityServerPort"),
            USER_DIRECTORY_URL of Uri.of("http://localhost:$userDirectoryPort"),
            ENTRY_LOGGER_URL of Uri.of("http://localhost:$entryLoggerPort"),
            OAUTH_SERVER_URL of Uri.of("http://localhost:$oauthServerPort")
        )

        SecuritySystemServer(env).start().stop()
    }
}