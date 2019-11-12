package verysecuresystems.api

import org.http4k.contract.contract
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.contract.security.AuthCodeOAuthSecurity
import org.http4k.core.Method.GET
import org.http4k.format.Jackson
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.security.OAuthProvider
import verysecuresystems.Inhabitants
import verysecuresystems.external.EntryLogger
import verysecuresystems.external.UserDirectory

/**
 * The exposed Server API, protected by Bearer Token (which can be retrieved via
 * OAuth login).
 */
object Api {
    fun getHttpHandler(userDirectory: UserDirectory,
                       entryLogger: EntryLogger,
                       inhabitants: Inhabitants,
                       oAuthProvider: OAuthProvider
    ): RoutingHttpHandler =
            "/api" bind routes(
                    "/oauth/callback" bind GET to oAuthProvider.callback,
                    contract {
                        renderer = OpenApi3(ApiInfo("Security Server API", "v1.0", "This API is secured by an OAuth auth code. Simply click 'Authorize' to start the flow."), Jackson)
                        descriptionPath = "/api-docs"
                        security = AuthCodeOAuthSecurity(oAuthProvider)
                        routes += KnockKnock.getRoute(userDirectory::lookup, inhabitants::add, entryLogger::enter)
                        routes += WhoIsThere.getRoute(inhabitants, userDirectory::lookup)
                        routes += ByeBye.getRoute(inhabitants::remove, entryLogger::exit)
                    }
            )
}

