package env.oauthserver

import com.natpryce.Result
import org.http4k.security.AccessTokenContainer
import org.http4k.security.oauth.server.AccessTokenError
import org.http4k.security.oauth.server.AccessTokens
import org.http4k.security.oauth.server.AuthorizationCode
import org.http4k.security.oauth.server.AuthorizationCodeAlreadyUsed
import org.http4k.security.oauth.server.ClientId

class InMemoryAccessTokens : AccessTokens {
    override fun create(authorizationCode: AuthorizationCode): Result<AccessTokenContainer, AuthorizationCodeAlreadyUsed> {
        TODO("not implemented")
    }

    override fun create(clientId: ClientId): Result<AccessTokenContainer, AccessTokenError> {
        TODO("not implemented")
    }

    override fun isValid(accessToken: AccessTokenContainer): Boolean {
        TODO("not implemented")
    }
}