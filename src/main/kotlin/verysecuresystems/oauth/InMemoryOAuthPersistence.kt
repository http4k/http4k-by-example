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
import java.util.UUID

class InMemoryOAuthPersistence(private val clock: Clock) : OAuthPersistence {
    private val csrfName = "securityServerCsrf"
    private val clientAuthCookie = "securityServerAuth"
    private val userTokens = mutableMapOf<String, AccessToken>()

    override fun retrieveCsrf(request: Request) = request.cookie(csrfName)?.value?.let(::CrossSiteRequestForgeryToken)

    override fun retrieveToken(request: Request) = request.header("Authorization")
        ?.removePrefix("Bearer ")
        ?.let(::AccessToken)
        ?.takeIf(userTokens::containsValue)
        ?: request.cookie(clientAuthCookie)?.value?.let { userTokens[it] }

    override fun assignCsrf(redirect: Response, csrf: CrossSiteRequestForgeryToken) = redirect.cookie(expiring(csrfName, csrf.value))

    override fun assignToken(request: Request, redirect: Response, accessToken: AccessToken) =
        UUID.randomUUID().let {
            userTokens[it.toString()] = accessToken
            redirect.cookie(expiring(clientAuthCookie, it.toString())).invalidateCookie(csrfName)
        }

    override fun authFailureResponse() = Response(FORBIDDEN).invalidateCookie(csrfName).invalidateCookie(clientAuthCookie)

    private fun expiring(name: String, value: String) = Cookie(name, value,
        path = "/",
        expires = LocalDateTime.ofInstant(clock.instant().plus(Duration.ofHours(3)), ZoneId.of("GMT")))
}