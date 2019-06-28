package verysecuresystems.api

import org.http4k.contract.contract
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.contract.security.OAuthSecurity
import org.http4k.core.Method.GET
import org.http4k.core.then
import org.http4k.filter.CorsPolicy.Companion.UnsafeGlobalPermissive
import org.http4k.filter.ServerFilters.Cors
import org.http4k.format.Jackson
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.security.OAuthProvider
import verysecuresystems.Inhabitants
import verysecuresystems.external.EntryLogger
import verysecuresystems.external.UserDirectory

object Api {
    operator fun invoke(userDirectory: UserDirectory,
                        entryLogger: EntryLogger,
                        inhabitants: Inhabitants,
                        oAuthProvider: OAuthProvider
    ) = Cors(UnsafeGlobalPermissive)
        .then(
            "/api" bind routes(
                "/oauth/callback" bind GET to oAuthProvider.callback,
                contract {
                    renderer = OpenApi3(ApiInfo("Security Server API", "v1.0", "This API is secured by an OAuth auth code. Simply click 'Authorize' to start the flow."), Jackson)
                    descriptionPath = "/api-docs"
                    security = OAuthSecurity(oAuthProvider)
                    routes += KnockKnock(userDirectory::lookup, inhabitants::add, entryLogger::enter)
                    routes += WhoIsThere(inhabitants, userDirectory::lookup)
                    routes += ByeBye(inhabitants::remove, entryLogger::exit)
                }
            )
        )
}

