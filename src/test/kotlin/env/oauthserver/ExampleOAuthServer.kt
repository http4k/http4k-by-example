package env.oauthserver

import env.oauthserver.ExampleOAuthServer.Form.formLens
import env.oauthserver.ExampleOAuthServer.Form.password
import env.oauthserver.ExampleOAuthServer.Form.username
import org.http4k.core.Body
import org.http4k.core.Credentials
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.SEE_OTHER
import org.http4k.core.then
import org.http4k.core.with
import org.http4k.filter.CorsPolicy
import org.http4k.filter.DebuggingFilters
import org.http4k.filter.ServerFilters.Cors
import org.http4k.format.Jackson
import org.http4k.lens.FormField
import org.http4k.lens.Header
import org.http4k.lens.Validator.Strict
import org.http4k.lens.webForm
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.security.oauth.server.OAuthServer
import java.time.Clock

object ExampleOAuthServer {
    operator fun invoke(credentials: Credentials, vararg oAuthClientData: OAuthClientData): HttpHandler {
        val clock = Clock.systemUTC()
        val server = OAuthServer(
            "/oauth2/token",
            ExampleAuthRequestTracking(),
            SimpleClientValidator(*oAuthClientData),
            InsecureAuthorizationCodes(clock),
            InsecureAccessTokens(),
            Jackson,
            clock
        )

        val userAuth = UserAuthentication(credentials)

        // this CORS filter is here to allow interactions from the OpenAPI UI (running in a browser)
        return Cors(CorsPolicy(listOf("*"), listOf("*"), Method.values().toList()))
            .then(DebuggingFilters.PrintRequestAndResponse())
            .then(
                routes(
                    server.tokenRoute,
                    "/" bind routes(
                        GET to server.authenticationStart.then { Response(OK).body(LOGIN_PAGE) },
                        POST to { request ->
                            val form = formLens(request)
                            if (userAuth.authenticate(Credentials(username(form), password(form)))) {
                                server.authenticationComplete(request)
                            } else Response(SEE_OTHER).with(Header.LOCATION of request.uri)
                        }
                    )
                )
            )
    }

    private object Form {
        val username = FormField.required("username")
        val password = FormField.required("password")
        val formLens = Body.webForm(Strict, username, password).toLens()
    }
}

private const val LOGIN_PAGE = """
    <html>
    <form method="POST">
        <input id="username" type="text" value="user" name="username"><br>
        <input id="password" type="password" value="password" name="password"><br>
        <button type="submit">Login</button>
    </form>
    </html>
"""

