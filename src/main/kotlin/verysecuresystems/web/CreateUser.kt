package verysecuresystems.web

import org.http4k.core.Body
import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.SEE_OTHER
import org.http4k.core.then
import org.http4k.lens.FormField
import org.http4k.lens.Validator.Feedback
import org.http4k.lens.nonEmptyString
import org.http4k.lens.webForm
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.template.TemplateRenderer
import verysecuresystems.EmailAddress
import verysecuresystems.Username
import verysecuresystems.external.UserDirectory

/**
 * Handles the creation of a new user, using standardised http4k form lenses
 * configured to feedback errors to the user.
 */
fun CreateUser(renderer: TemplateRenderer, userDirectory: UserDirectory): RoutingHttpHandler {
    val username = FormField.nonEmptyString().map(::Username, Username::value).required("username")
    val email = FormField.nonEmptyString().map(::EmailAddress, EmailAddress::value).required("email")
    val form = Body.webForm(Feedback, username, email).toLens()

    return "/create" bind POST to SetHtmlContentType.then {
        val webForm = form(it)
        if (webForm.errors.isEmpty()) {
            userDirectory.create(username(webForm), email(webForm))
            Response(SEE_OTHER).header("location", "/users")
        } else {
            Response(OK).body(renderer(ListUsersView(userDirectory.list(), webForm)))
        }
    }
}