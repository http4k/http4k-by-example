package verysecuresystems

import org.http4k.client.ApacheClient
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters.SetHostFrom
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer
import java.time.Clock

/**
 * Responsible for setting up real HTTP servers and clients to downstream services via HTTP
 */
object SecuritySystemServer {
    operator fun invoke(port: Int, userDirectoryUrl: String, entryLoggerUrl: String): Http4kServer {
        val client = ApacheClient()
        val app = SecuritySystem(
            Clock.systemUTC(),
            ::println,
            SetHostFrom(Uri.of(userDirectoryUrl)).then(client),
            SetHostFrom(Uri.of(entryLoggerUrl)).then(client)
        )
        return app.asServer(Jetty(port))
    }
}


