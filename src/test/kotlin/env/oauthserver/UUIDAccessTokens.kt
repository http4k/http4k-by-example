package env.oauthserver

import com.natpryce.Failure
import com.natpryce.Success
import org.http4k.security.AccessToken
import org.http4k.security.oauth.server.AccessTokens
import org.http4k.security.oauth.server.AuthorizationCode
import org.http4k.security.oauth.server.ClientId
import org.http4k.security.oauth.server.UnsupportedGrantType

class UUIDAccessTokens : AccessTokens {
    override fun create(clientId: ClientId) = Failure(UnsupportedGrantType("client_credentials"))

    override fun create(authorizationCode: AuthorizationCode): Success<AccessToken> {
        return Success(AccessToken(PREFIX + authorizationCode.value))
    }

    companion object {
        const val PREFIX = "AUTH_TOKEN_"
    }
}