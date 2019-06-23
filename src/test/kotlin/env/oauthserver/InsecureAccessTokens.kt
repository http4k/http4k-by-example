package env.oauthserver

import com.natpryce.Failure
import com.natpryce.Success
import org.http4k.security.AccessTokenContainer
import org.http4k.security.oauth.server.AccessTokens
import org.http4k.security.oauth.server.AuthorizationCode
import org.http4k.security.oauth.server.ClientId
import org.http4k.security.oauth.server.UnsupportedGrantType
import java.util.UUID

class InsecureAccessTokens : AccessTokens {
    override fun create(clientId: ClientId) = Failure(UnsupportedGrantType("client_credentials"))

    override fun isValid(accessToken: AccessTokenContainer) = true

    override fun create(authorizationCode: AuthorizationCode) =
        Success(AccessTokenContainer(UUID.randomUUID().toString()))
}