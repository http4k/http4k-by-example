package env

import env.oauthserver.SimpleAccessTokens
import org.http4k.security.AccessToken
import org.http4k.security.oauth.server.AccessTokens
import org.http4k.security.oauth.server.AuthorizationCode
import org.http4k.security.oauth.server.ClientId

class RecordingAccessTokens : AccessTokens {
    private val issued = mutableListOf<AccessToken>()

    private val delegate = SimpleAccessTokens()

    fun issuedToken() = issued.first().also { issued.clear() }

    override fun create(clientId: ClientId) = delegate.create(clientId)

    override fun create(authorizationCode: AuthorizationCode) =
        delegate.create(authorizationCode).also {
            issued += it.value
        }
}