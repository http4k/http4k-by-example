package env.oauthserver

import com.natpryce.Success
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.security.oauth.server.AuthRequest
import org.http4k.security.oauth.server.AuthorizationCode
import org.http4k.security.oauth.server.AuthorizationCodeDetails
import org.http4k.security.oauth.server.AuthorizationCodes
import java.time.Clock
import java.time.temporal.ChronoUnit.DAYS
import java.util.UUID

class InMemoryAuthorizationCodes(private val clock: Clock) : AuthorizationCodes {
    private val codes = mutableMapOf<AuthorizationCode, AuthorizationCodeDetails>()

    override fun detailsFor(code: AuthorizationCode): AuthorizationCodeDetails =
        codes[code]?.also {
            codes -= code
        } ?: error("code not stored")

    override fun create(request: Request, authRequest: AuthRequest, response: Response) =
        Success(AuthorizationCode("AUTH_CODE_" + UUID.randomUUID().toString()).also {
            codes[it] = AuthorizationCodeDetails(authRequest.client, authRequest.redirectUri, clock.instant().plus(1, DAYS), authRequest.responseType)
        })
}