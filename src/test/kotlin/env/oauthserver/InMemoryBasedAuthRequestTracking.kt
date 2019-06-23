package env.oauthserver

import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.cookie
import org.http4k.security.oauth.server.AuthRequest
import org.http4k.security.oauth.server.AuthRequestTracking
import java.util.UUID

class InMemoryBasedAuthRequestTracking : AuthRequestTracking {
    private val cookieName = "Id"

    private val requests = mutableMapOf<String, AuthRequest>()

    override fun trackAuthRequest(request: Request, authRequest: AuthRequest, response: Response): Response {
        val id = UUID.randomUUID().toString()
        requests[id] = authRequest
        return response.cookie(Cookie(cookieName, id))
    }

    override fun resolveAuthRequest(request: Request) = requests[request.cookie(cookieName)?.value]
}