package verysecuresystems.oauth

import org.http4k.security.AccessToken

typealias AccessTokenChecker = (AccessToken) -> Boolean

object InsecureAccessTokenChecker : AccessTokenChecker {
    const val VALID_PREFIX = "AUTH_TOKEN_AUTH_CODE_"
    override operator fun invoke(token: AccessToken): Boolean {
        return token.value.startsWith(VALID_PREFIX)
    }
}