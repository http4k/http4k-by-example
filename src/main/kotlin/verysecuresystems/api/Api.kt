package verysecuresystems.api

import org.http4k.contract.ApiInfo
import org.http4k.contract.ApiKey
import org.http4k.contract.BasePath
import org.http4k.contract.Module
import org.http4k.contract.RouteModule
import org.http4k.contract.Swagger
import org.http4k.filter.CorsPolicy.Companion.UnsafeGlobalPermissive
import org.http4k.filter.ServerFilters.Cors
import org.http4k.format.Jackson
import org.http4k.lens.Header
import verysecuresystems.Inhabitants
import verysecuresystems.external.EntryLogger
import verysecuresystems.external.UserDirectory

object Api {
    fun module(path: BasePath, userDirectory: UserDirectory, entryLogger: EntryLogger, inhabitants: Inhabitants): Module =
        RouteModule(path, Swagger(ApiInfo("Security server API - the API key is 'realSecret'!", "v1.0"), Jackson), Cors(UnsafeGlobalPermissive))
            .withDescriptionPath { it / "api-docs" }
            .securedBy(ApiKey(Header.required("key"), { key: String -> key == "realSecret" }))
            .withRoute(KnockKnock.route(inhabitants, userDirectory, entryLogger))
            .withRoute(WhoIsThere.route(inhabitants, userDirectory))
            .withRoute(ByeBye.route(inhabitants, entryLogger))
}

