package verysecuresystems.oauth

import org.http4k.security.AccessToken

/**
 * Checks that the passed BearerToken is authentic. (eg. signed with a particular key)
 */
interface TokenChecker {
    fun check(accessToken: AccessToken): Boolean
}

/**
 * We aren't doing any crypto in this example, so just check the key for the prefix.
 */
object InsecureTokenChecker : TokenChecker {
    override fun check(accessToken: AccessToken) = accessToken.value.startsWith("ACCESS_TOKEN")
}