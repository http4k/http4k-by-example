package env.oauthserver

import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.lens.LensFailure
import org.http4k.security.oauth.server.AuthRequest
import org.http4k.security.oauth.server.AuthRequestTracking
import org.http4k.security.oauth.server.OAuthServer

class InMemoryAuthRequestTracking : AuthRequestTracking {
    private val initiatedRequests = mutableListOf<AuthRequest>()

    override fun trackAuthRequest(request: Request, authRequest: AuthRequest, response: Response) =
        response.also { initiatedRequests += authRequest }

    override fun resolveAuthRequest(request: Request) =
        try {
            with(OAuthServer) {
                val extracted = AuthRequest(
                    clientId(request),
                    scopes(request) ?: listOf(),
                    redirectUri(request),
                    state(request),
                    responseType(request)
                )
                if (initiatedRequests.remove(extracted)) extracted else null
            }
        } catch (e: LensFailure) {
            null
        }
}