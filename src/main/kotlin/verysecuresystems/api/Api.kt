package verysecuresystems.api

import org.http4k.contract.ApiInfo
import org.http4k.contract.ApiKey
import org.http4k.contract.Swagger
import org.http4k.core.then
import org.http4k.filter.CorsPolicy
import org.http4k.filter.ServerFilters
import org.http4k.format.Jackson
import org.http4k.lens.Header
import org.http4k.routing.contract
import verysecuresystems.Inhabitants
import verysecuresystems.external.EntryLogger
import verysecuresystems.external.UserDirectory

object Api {
    fun router(userDirectory: UserDirectory, entryLogger: EntryLogger, inhabitants: Inhabitants) =
        ServerFilters.Cors(CorsPolicy.UnsafeGlobalPermissive)
            .then(contract(Swagger(ApiInfo("Security server API - the API key is 'realSecret'!", "v1.0"), Jackson),
                "/api-docs", ApiKey(Header.required("key"), { key: String -> key == "realSecret" }))
                .withRoute(KnockKnock.route(inhabitants, userDirectory, entryLogger))
                .withRoute(WhoIsThere.route(inhabitants, userDirectory))
                .withRoute(ByeBye.route(inhabitants, entryLogger)))
}

