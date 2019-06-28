package verysecuresystems.oauth

import org.http4k.security.AccessToken

interface TokenChecker {
    fun check(accessToken: AccessToken): Boolean
}

// Normally you need to check the contents of an access token to ensure it's ok (eg. signed with a particular key)
object InsecureTokenChecker : TokenChecker {
    override fun check(accessToken: AccessToken) = accessToken.value.startsWith("ACCESS_TOKEN")
}