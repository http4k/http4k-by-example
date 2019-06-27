package functional

import org.http4k.security.AccessToken
import verysecuresystems.oauth.InsecureAccessTokenChecker

object AccessTokens {
  val valid = AccessToken(InsecureAccessTokenChecker.VALID_PREFIX + "token")
  val invalid = AccessToken("token")
}