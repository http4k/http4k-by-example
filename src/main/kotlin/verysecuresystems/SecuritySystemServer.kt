package verysecuresystems

import org.http4k.client.OkHttp
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.events.AutoMarshallingEvents
import org.http4k.format.Jackson
import org.http4k.lens.int
import org.http4k.lens.uri
import org.http4k.server.Undertow
import org.http4k.server.asServer
import verysecuresystems.Settings.ENTRY_LOGGER_URL
import verysecuresystems.Settings.OAUTH_SERVER_URL
import verysecuresystems.Settings.PORT
import verysecuresystems.Settings.SECURITY_SERVER_URL
import verysecuresystems.Settings.USER_DIRECTORY_URL
import java.time.Clock

/**
 * Responsible for setting up real HTTP servers and clients to downstream services via HTTP
 */
fun SecuritySystemServer(env: Environment) =
    SecuritySystem(Clock.systemUTC(), AutoMarshallingEvents(Jackson),
        OkHttp(),
        SECURITY_SERVER_URL(env),
        OAUTH_SERVER_URL(env),
        USER_DIRECTORY_URL(env),
        ENTRY_LOGGER_URL(env)
    ).asServer(Undertow(PORT(env)))

object Settings {
    val PORT = EnvironmentKey.int().required("PORT")
    val SECURITY_SERVER_URL = EnvironmentKey.uri().required("SECURITY_SERVER_URL")
    val OAUTH_SERVER_URL = EnvironmentKey.uri().required("OAUTH_SERVER_URL")
    val USER_DIRECTORY_URL = EnvironmentKey.uri().required("USER_DIRECTORY_URL")
    val ENTRY_LOGGER_URL = EnvironmentKey.uri().required("ENTRY_LOGGER_URL")
}