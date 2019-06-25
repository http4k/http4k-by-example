package verysecuresystems.oauth

import org.http4k.security.AccessToken

typealias AccessTokenChecker = (AccessToken) -> Boolean

object InsecureAccessTokenChecker : AccessTokenChecker {
    override operator fun invoke(token: AccessToken): Boolean = token.value.startsWith("AUTH_TOKEN_AUTH_CODE_")
}