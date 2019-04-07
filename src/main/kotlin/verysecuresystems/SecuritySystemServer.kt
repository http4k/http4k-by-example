package verysecuresystems

import org.http4k.client.OkHttp
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters
import org.http4k.server.Undertow
import org.http4k.server.asServer
import java.time.Clock

/**
 * Responsible for setting up real HTTP servers and clients to downstream services via HTTP
 */
object SecuritySystemServer {
    operator fun invoke(port: Int, userDirectoryUrl: Uri, entryLoggerUrl: Uri) =
        SecuritySystem(Clock.systemUTC(), ::println,
            ClientFilters.SetHostFrom(userDirectoryUrl).then(OkHttp()),
            ClientFilters.SetHostFrom(entryLoggerUrl).then(OkHttp())
        ).asServer(Undertow(port))
}
