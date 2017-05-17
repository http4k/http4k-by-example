package verysecuresystems

import org.http4k.client.ApacheClient
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters.SetHostFrom
import org.http4k.server.Jetty
import org.http4k.server.asServer

object SecuritySystemServer {
    fun main(args: Array<String>) {
        val port = if (args.isNotEmpty()) args[0].toInt() else 5000
        val client = ApacheClient()
        val app = SecuritySystem(
            SetHostFrom(Uri.of("http://localhost:9001")).then(client),
            SetHostFrom(Uri.of("http://localhost:9002")).then(client)
        )

        app.asServer(Jetty(port)).start().block()
    }
}


