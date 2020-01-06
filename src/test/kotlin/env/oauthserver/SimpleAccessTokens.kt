package env.oauthserver

import com.natpryce.Failure
import com.natpryce.Success
import org.http4k.security.AccessToken
import org.http4k.security.oauth.server.AccessTokens
import org.http4k.security.oauth.server.AuthorizationCode
import org.http4k.security.oauth.server.ClientId
import org.http4k.security.oauth.server.TokenRequest
import org.http4k.security.oauth.server.UnsupportedGrantType

class SimpleAccessTokens : AccessTokens {
    override fun create(authorizationCode: AuthorizationCode) =
        Success(AccessToken(ACCESS_TOKEN_PREFIX + authorizationCode.value.reversed()))

    override fun create(clientId: ClientId, tokenRequest: TokenRequest) = Failure(UnsupportedGrantType("client_credentials"))
}

const val ACCESS_TOKEN_PREFIX = "ACCESS_TOKEN_"
