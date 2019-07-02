package verysecuresystems.oauth

import org.http4k.core.Credentials
import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.security.OAuthProvider
import org.http4k.security.OAuthProviderConfig
import java.time.Clock

/**
 * OAuthProvider configured to callback to this server.
 */
fun SecurityServerOAuthProvider(securityServerUri: Uri,
                                oauthServerUri: Uri,
                                oauthServerHttp: HttpHandler,
                                clock: Clock) =
    OAuthProvider(
        OAuthProviderConfig(oauthServerUri, "/", "/oauth2/token",
            Credentials("securityServer", "securityServerSecret")),
        oauthServerHttp,
        securityServerUri.path("/api/oauth/callback"),
        emptyList(),
        InMemoryOAuthPersistence(clock, InsecureTokenChecker)
    )