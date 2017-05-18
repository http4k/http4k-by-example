package verysecuresystems

import org.http4k.contract.Root
import org.http4k.core.HttpHandler
import org.http4k.core.then
import org.http4k.filter.CorsPolicy.Companion.UnsafeGlobalPermissive
import org.http4k.filter.DebuggingFilters.PrintRequestAndResponse
import org.http4k.filter.ServerFilters.Cors
import verysecuresystems.api.Api
import verysecuresystems.external.EntryLogger
import verysecuresystems.external.UserDirectory
import verysecuresystems.web.Web

object SecuritySystem {
    operator fun invoke(userDirectoryClient: HttpHandler,
                        entryLoggerClient: HttpHandler): HttpHandler {

        val userDirectory = UserDirectory(userDirectoryClient)
        val entryLogger = EntryLogger(entryLoggerClient)
        val inhabitants = Inhabitants()

        val app = Api.module(Root / "api", userDirectory, entryLogger, inhabitants)
            .then(Web.module(Root, userDirectory))
            .toHttpHandler()

        return PrintRequestAndResponse()
            .then(Cors(UnsafeGlobalPermissive))
            .then(app)
    }
}