package verysecuresystems.web

import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.lens.WebForm
import org.http4k.routing.bind
import org.http4k.template.TemplateRenderer
import org.http4k.template.ViewModel
import verysecuresystems.User
import verysecuresystems.external.UserDirectory

/**
 * Displays the list of known users using a ViewModel
 */
fun ListUsers(renderer: TemplateRenderer, userDirectory: UserDirectory) =
    "/" bind GET to SetHtmlContentType.then {
        Response(OK).body(renderer(ListUsersView(userDirectory.list(), WebForm())))
    }

data class ListUsersView(val users: List<User>, val form: WebForm) : ViewModel {
    val errors: List<String> = form.errors.map { it.toString() }
}