package env.oauthserver

import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Uri
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.cookie
import org.http4k.core.with
import org.http4k.security.oauth.server.AuthRequest
import org.http4k.security.oauth.server.AuthRequestTracking
import org.http4k.security.oauth.server.OAuthServer

class ExampleAuthRequestTracking : AuthRequestTracking {
    private val cookieName = "OauthFlowId"

    override fun trackAuthRequest(request: Request, authRequest: AuthRequest, response: Response) =
        with(OAuthServer) {
            response.cookie(Cookie(cookieName, Request(GET, "dummy")
                .with(clientId of authRequest.client)
                .with(redirectUri of authRequest.redirectUri)
                .with(scopes of authRequest.scopes)
                .with(state of authRequest.state)
                .with(responseType of authRequest.responseType)
                .uri.query))
        }

    override fun resolveAuthRequest(request: Request) =
        with(OAuthServer) {
            request.cookie(cookieName)?.value
                ?.let { Request(GET, Uri.of("dummy").query(it)) }
                ?.let {
                    AuthRequest(
                        clientId(it),
                        scopes(it) ?: listOf(),
                        redirectUri(it),
                        state(it),
                        responseType(it)
                    )
                }
        }
}