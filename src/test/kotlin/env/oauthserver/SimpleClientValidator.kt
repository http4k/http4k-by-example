package env.oauthserver

import org.http4k.core.Credentials
import org.http4k.core.Uri
import org.http4k.security.oauth.server.ClientId
import org.http4k.security.oauth.server.ClientValidator

data class OAuthClientData(val credentials: Credentials, val redirectionUri: Uri)

object SimpleClientValidator {
    operator fun invoke(vararg clientData: OAuthClientData) = object : ClientValidator {
        override fun validateClientId(clientId: ClientId) =
            clientData.any { clientId.value == it.credentials.user }

        override fun validateCredentials(clientId: ClientId, clientSecret: String) =
            clientData.any { Credentials(clientId.value, clientSecret) == it.credentials }

        override fun validateRedirection(clientId: ClientId, redirectionUri: Uri) =
            clientData.any { validateClientId(clientId) && redirectionUri == it.redirectionUri }
    }
}