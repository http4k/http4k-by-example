package verysecuresystems.api

import org.http4k.contract.contract
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.contract.security.ApiKeySecurity
import org.http4k.core.then
import org.http4k.filter.CorsPolicy.Companion.UnsafeGlobalPermissive
import org.http4k.filter.ServerFilters.Cors
import org.http4k.lens.Header
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import verysecuresystems.Inhabitants
import verysecuresystems.external.EntryLogger
import verysecuresystems.external.UserDirectory

object Api {
    operator fun invoke(userDirectory: UserDirectory, entryLogger: EntryLogger, inhabitants: Inhabitants): RoutingHttpHandler =
        "/api" bind Cors(UnsafeGlobalPermissive)
            .then(contract {
                renderer = OpenApi3(ApiInfo("Security server API - the API key is 'realSecret'!", "v1.0"))
                descriptionPath = "/api-docs"
                security = ApiKeySecurity(Header.required("key"), { key: String -> key == "realSecret" })
                routes += KnockKnock(userDirectory::lookup, inhabitants::add, entryLogger::enter)
                routes += WhoIsThere(inhabitants, userDirectory::lookup)
                routes += ByeBye(inhabitants::remove, entryLogger::exit)
            })
}

