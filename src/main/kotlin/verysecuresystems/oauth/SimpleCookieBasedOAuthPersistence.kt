package verysecuresystems.oauth

import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.FORBIDDEN
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.cookie
import org.http4k.core.cookie.invalidateCookie
import org.http4k.security.AccessToken
import org.http4k.security.CrossSiteRequestForgeryToken
import org.http4k.security.OAuthPersistence
import java.time.Clock
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId

class SimpleCookieBasedOAuthPersistence(private val tokenChecker: AccessTokenChecker, private val clock: Clock) : OAuthPersistence {
    private val csrfName = "securityServerCsrf"
    private val accessTokenCookieName = "securityServerAccessToken"

    override fun retrieveCsrf(request: Request) = request.cookie(csrfName)?.value?.let(::CrossSiteRequestForgeryToken)

    override fun retrieveToken(request: Request) = request.authToken()
        ?.let(::AccessToken)
        ?.takeIf(tokenChecker)

    override fun assignCsrf(redirect: Response, csrf: CrossSiteRequestForgeryToken) = redirect.cookie(expiring(csrfName, csrf.value))

    override fun assignToken(request: Request, redirect: Response, accessToken: AccessToken) =
        redirect.cookie(expiring(accessTokenCookieName, accessToken.value)).invalidateCookie(csrfName)

    override fun authFailureResponse() = Response(FORBIDDEN).invalidateCookie(csrfName).invalidateCookie(accessTokenCookieName)

    private fun expiring(name: String, value: String) = Cookie(name, value,
        path = "/",
        expires = LocalDateTime.ofInstant(clock.instant().plus(Duration.ofHours(3)), ZoneId.of("GMT")))

    private fun Request.authToken() = header("Authorization")
        ?.removePrefix("Bearer ")
        ?: cookie(accessTokenCookieName)?.value
}