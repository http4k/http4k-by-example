package functional

import org.http4k.security.AccessToken

object AccessTokens {
  val valid = AccessToken("token")
  val invalid = AccessToken("token")
}