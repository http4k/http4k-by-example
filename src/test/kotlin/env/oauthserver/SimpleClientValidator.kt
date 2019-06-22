package env.oauthserver

import org.http4k.core.Credentials
import org.http4k.core.Uri
import org.http4k.security.oauth.server.ClientId
import org.http4k.security.oauth.server.ClientValidator

object SimpleClientValidator {
    operator fun invoke(credentials: Credentials, validRedirectionUri: Uri) = object : ClientValidator {
        override fun validateClientId(clientId: ClientId) = clientId.value == credentials.user

        override fun validateCredentials(clientId: ClientId, clientSecret: String) =
            Credentials(clientId.value, clientSecret) == credentials

        override fun validateRedirection(clientId: ClientId, redirectionUri: Uri) = validateClientId(clientId) &&
            redirectionUri == validRedirectionUri
    }
}