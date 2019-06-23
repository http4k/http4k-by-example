package verysecuresystems.oauth

import org.http4k.core.Credentials
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.FORBIDDEN
import org.http4k.core.Uri
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.cookie
import org.http4k.core.cookie.invalidateCookie
import org.http4k.security.AccessTokenContainer
import org.http4k.security.CrossSiteRequestForgeryToken
import org.http4k.security.OAuthPersistence
import org.http4k.security.OAuthProvider
import org.http4k.security.OAuthProviderConfig
import java.time.Clock
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId

object SecuritySystemOAuthProvider {
    operator fun invoke(clock: Clock, oauthProviderHttp: HttpHandler, oauthServerUri: Uri) = OAuthProvider(
        OAuthProviderConfig(oauthServerUri, "/", "/oauth2/token",
            Credentials("securityServer", "securityServerSecret")),
        oauthProviderHttp,
        Uri.of("/oauth-callback"),
        listOf(),
        SecurityServerOAuthPersistence(clock)
    )
}

private class SecurityServerOAuthPersistence(private val clock: Clock = Clock.systemUTC()) : OAuthPersistence {
    private val csrfName = "securityServerCsrf"
    private val accessTokenCookieName = "securityServerAccessToken"

    override fun retrieveCsrf(request: Request) = request.cookie(csrfName)?.value?.let(::CrossSiteRequestForgeryToken)

    override fun retrieveToken(request: Request) = request.cookie(accessTokenCookieName)?.value?.let(::AccessTokenContainer)

    override fun assignCsrf(redirect: Response, csrf: CrossSiteRequestForgeryToken) = redirect.cookie(expiring(csrfName, csrf.value))

    override fun assignToken(request: Request, redirect: Response, accessToken: AccessTokenContainer) = redirect.cookie(expiring(accessTokenCookieName, accessToken.value)).invalidateCookie(csrfName)

    override fun authFailureResponse() = Response(FORBIDDEN).invalidateCookie(csrfName).invalidateCookie(accessTokenCookieName)

    private fun expiring(name: String, value: String) = Cookie(name, value, expires = LocalDateTime.ofInstant(clock.instant().plus(Duration.ofHours(1)), ZoneId.of("GMT")))
}