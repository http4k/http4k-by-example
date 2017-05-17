package verysecuresystems

import org.http4k.contract.Root
import org.http4k.contract.RouteModule
import org.http4k.core.HttpHandler
import org.http4k.core.then
import org.http4k.filter.CorsPolicy.Companion.UnsafeGlobalPermissive
import org.http4k.filter.DebuggingFilters.PrintRequestAndResponse
import org.http4k.filter.ServerFilters.Cors

object SecuritySystem {

    operator fun invoke(userDirectoryClient: HttpHandler,
                        entryLoggerClient: HttpHandler): HttpHandler {

        val globalFilter = PrintRequestAndResponse().then(Cors(UnsafeGlobalPermissive))

        return globalFilter.then(
            RouteModule(Root)
                .toHttpHandler()
        )
    }
}