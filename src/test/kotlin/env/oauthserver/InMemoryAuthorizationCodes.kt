package env.oauthserver

import com.natpryce.Result
import com.natpryce.Success
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.security.oauth.server.AuthRequest
import org.http4k.security.oauth.server.AuthorizationCode
import org.http4k.security.oauth.server.AuthorizationCodeDetails
import org.http4k.security.oauth.server.AuthorizationCodes
import org.http4k.security.oauth.server.UserRejectedRequest
import java.time.Clock
import java.util.UUID

object InMemoryAuthorizationCodes {
    operator fun invoke(clock: Clock) =
        object : AuthorizationCodes {
            private val codes = mutableMapOf<AuthorizationCode, AuthorizationCodeDetails>()

            override fun detailsFor(code: AuthorizationCode) =
                codes[code] ?: error("code not stored")

            override fun create(request: Request, authRequest: AuthRequest, response: Response): Result<AuthorizationCode, UserRejectedRequest> {
                return Success(AuthorizationCode(UUID.randomUUID().toString()).also {
                    codes[it] = AuthorizationCodeDetails(authRequest.client, authRequest.redirectUri, clock.instant(), authRequest.responseType)
                })
            }
        }
}