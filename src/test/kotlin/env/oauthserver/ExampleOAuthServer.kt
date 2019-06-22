package env.oauthserver

import env.oauthserver.ExampleOAuthServer.Form.formLens
import org.http4k.cloudnative.env.Secret
import org.http4k.core.Body
import org.http4k.core.ContentType.Companion.TEXT_HTML
import org.http4k.core.Credentials
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.FORBIDDEN
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.core.with
import org.http4k.format.Jackson
import org.http4k.lens.FormField
import org.http4k.lens.Query
import org.http4k.lens.Validator
import org.http4k.lens.webForm
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.security.oauth.server.InsecureCookieBasedAuthRequestTracking
import org.http4k.security.oauth.server.OAuthServer
import org.http4k.template.HandlebarsTemplates
import org.http4k.template.ViewModel
import org.http4k.template.viewModel
import verysecuresystems.Username
import java.time.Clock

object ExampleOAuthServer {
    private val view = Body.viewModel(HandlebarsTemplates().CachingClasspath(), TEXT_HTML).toLens()

    operator fun invoke(clock: Clock): HttpHandler {
        val server = OAuthServer(
            "/oauth2/token",
            InsecureCookieBasedAuthRequestTracking(),
            SimpleClientValidator(Credentials("user", "password"), Uri.of("")),
            InMemoryAuthorizationCodes(clock),
            InMemoryAccessTokens(),
            Jackson,
            clock
        )

        return routes(
            server.tokenRoute,
            "/" bind routes(
                GET to server.authenticationStart.then { request -> Response(Status.OK).with(view of LoginView(clientIdQuery(request))) },
                POST to { request ->
                    val form = formLens(request)
                    try {
                        Response(FORBIDDEN)
                    } catch (e: Exception) {
                        Response(FORBIDDEN).with(view of LoginView("failed"))
                    }
                }
            )
        )
    }

    val clientIdQuery = Query.required("client_id")

    object Form {
        val username = FormField.map(::Username).required("username")
        val password = FormField.map(::Secret).required("password")
        val formLens = Body.webForm(Validator.Strict, username, password).toLens()
    }
}

data class LoginView(val message: String? = null) : ViewModel

