package verysecuresystems.web

import org.http4k.contract.Route
import org.http4k.contract.ServerRoute
import org.http4k.core.Body
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.TEMPORARY_REDIRECT
import org.http4k.core.then
import org.http4k.lens.FormField
import org.http4k.lens.FormValidator
import org.http4k.lens.WebForm
import org.http4k.lens.int
import org.http4k.lens.webForm
import org.http4k.template.TemplateRenderer
import org.http4k.template.ViewModel
import verysecuresystems.EmailAddress
import verysecuresystems.Id
import verysecuresystems.User
import verysecuresystems.Username
import verysecuresystems.external.UserDirectory

data class ManageUsersView(val users: List<User>, val form: WebForm) : ViewModel {
    val errors: List<String> = form.errors.map { it.toString() }
}

object ManageUsers {

    fun routes(renderer: TemplateRenderer, userDirectory: UserDirectory): List<ServerRoute> = listOf(
        view(renderer, userDirectory),
        create(renderer, userDirectory),
        delete(userDirectory)
    )

    private fun view(renderer: TemplateRenderer, userDirectory: UserDirectory) =
        Route().at(GET) / "users" bind SetHtmlContentType.then {
            Response(OK)
                .body(renderer(ManageUsersView(userDirectory.list(), WebForm())))
        }

    private fun create(renderer: TemplateRenderer, userDirectory: UserDirectory): ServerRoute {
        val username = FormField.map(::Username, Username::value).required("username")
        val email = FormField.map(::EmailAddress, EmailAddress::value).required("email")
        val form = Body.webForm(FormValidator.Feedback, username, email).toLens()

        return Route().at(POST) / "users" / "create" bind SetHtmlContentType.then {
            val webForm = form(it)
            if (webForm.errors.isEmpty()) {
                userDirectory.create(username(webForm), email(webForm))
                Response(TEMPORARY_REDIRECT).header("location", ".")
            } else {
                Response(OK).body(renderer(ManageUsersView(userDirectory.list(), webForm)))
            }
        }
    }

    private fun delete(userDirectory: UserDirectory): ServerRoute {
        val id = FormField.int().map(::Id, Id::value).required("id")
        val form = Body.webForm(FormValidator.Feedback, id).toLens()

        return Route().at(POST) / "users" / "delete" bind {
            userDirectory.delete(id(form(it)))
            Response(TEMPORARY_REDIRECT).header("location", ".")
        }
    }
}
