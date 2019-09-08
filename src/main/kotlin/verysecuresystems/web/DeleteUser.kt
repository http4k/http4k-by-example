package verysecuresystems.web

import org.http4k.core.Body
import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status.Companion.SEE_OTHER
import org.http4k.core.then
import org.http4k.lens.FormField
import org.http4k.lens.Validator.Strict
import org.http4k.lens.int
import org.http4k.lens.webForm
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import verysecuresystems.Id
import verysecuresystems.external.UserDirectory

/**
 * Handles the deletion of an existing user, using standardised http4k form lenses.
 */
fun DeleteUser(userDirectory: UserDirectory): RoutingHttpHandler {
    val id = FormField.int().map(::Id, Id::value).required("id")
    val form = Body.webForm(Strict, id).toLens()

    return "/delete" bind POST to SetHtmlContentType.then {
        userDirectory.delete(id(form(it)))
        Response(SEE_OTHER).header("location", "/users")
    }
}