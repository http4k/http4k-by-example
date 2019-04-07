package verysecuresystems

import org.http4k.client.OkHttp
import org.http4k.core.Uri
import org.http4k.server.Http4kServer
import org.http4k.server.Undertow
import org.http4k.server.asServer
import java.time.Clock

/**
 * Responsible for setting up real HTTP servers and clients to downstream services via HTTP
 */
object SecuritySystemServer {
    operator fun invoke(port: Int, userDirectoryUrl: Uri, entryLoggerUrl: Uri): Http4kServer {
        val client = OkHttp()
        return SecuritySystem(Clock.systemUTC(), ::println, userDirectoryUrl to client, entryLoggerUrl to client)
            .asServer(Undertow(port))
    }
}
