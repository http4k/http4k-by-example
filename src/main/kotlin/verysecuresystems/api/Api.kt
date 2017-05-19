package verysecuresystems.api

import org.http4k.contract.ApiKey
import org.http4k.contract.BasePath
import org.http4k.contract.Module
import org.http4k.contract.RouteModule
import org.http4k.contract.SimpleJson
import org.http4k.filter.CorsPolicy
import org.http4k.filter.ServerFilters
import org.http4k.format.Jackson
import org.http4k.lens.Header
import verysecuresystems.Inhabitants
import verysecuresystems.external.EntryLogger
import verysecuresystems.external.UserDirectory

object Api {
    private val corsFilter = ServerFilters.Cors(CorsPolicy.UnsafeGlobalPermissive)

    private val apiKey = ApiKey(Header.required("key"), { key: String -> key == "realSecret" })

    fun module(path: BasePath, userDirectory: UserDirectory, entryLogger: EntryLogger, inhabitants: Inhabitants): Module =
        RouteModule(path, SimpleJson(Jackson), corsFilter)
            .securedBy(apiKey)
            .withRoute(KnockKnock.route(inhabitants, userDirectory, entryLogger))
            .withRoute(WhoIsThere.route(inhabitants, userDirectory))
            .withRoute(ByeBye.route(inhabitants, entryLogger))
}

