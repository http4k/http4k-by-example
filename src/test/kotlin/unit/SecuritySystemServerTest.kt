package unit

import org.http4k.cloudnative.env.Environment
import org.http4k.core.Uri
import org.junit.jupiter.api.Test
import verysecuresystems.SecuritySystemServer
import verysecuresystems.Settings

class SecuritySystemServerTest {

    @Test
    fun `can start and stop server`() {
        val securityServerPort = 9000
        val userDirectoryPort = 10000
        val entryLoggerPort = 11000
        val oauthServerPort = 12000

        val env = Environment.defaults(Settings.PORT of securityServerPort,
            Settings.SECURITY_SERVER_URL of Uri.of("http://localhost:$securityServerPort"),
            Settings.USER_DIRECTORY_URL of Uri.of("http://localhost:$userDirectoryPort"),
            Settings.ENTRY_LOGGER_URL of Uri.of("http://localhost:$entryLoggerPort"),
            Settings.OAUTH_SERVER_URL of Uri.of("http://localhost:$oauthServerPort")
        )

        SecuritySystemServer(env).start().stop()
    }
}