package verysecuresystems.api

import org.http4k.contract.ApiInfo
import org.http4k.contract.ApiKey
import org.http4k.contract.OpenApi
import org.http4k.contract.contract
import org.http4k.core.then
import org.http4k.filter.CorsPolicy
import org.http4k.filter.ServerFilters
import org.http4k.format.Jackson
import org.http4k.lens.Header
import org.http4k.routing.RoutingHttpHandler
import verysecuresystems.Inhabitants
import verysecuresystems.external.EntryLogger
import verysecuresystems.external.UserDirectory

object Api {
    fun router(userDirectory: UserDirectory, entryLogger: EntryLogger, inhabitants: Inhabitants): RoutingHttpHandler =
        ServerFilters.Cors(CorsPolicy.UnsafeGlobalPermissive)
            .then(contract {
                renderer = OpenApi(ApiInfo("Security server API - the API key is 'realSecret'!", "v1.0"), Jackson)
                descriptionPath = "/api-docs"
                security = ApiKey(Header.required("key"), { key: String -> key == "realSecret" })
                routes += KnockKnock(inhabitants, userDirectory, entryLogger)
                routes += WhoIsThere(inhabitants, userDirectory)
                routes += ByeBye(inhabitants, entryLogger)
            })
}

