package verysecuresystems.external

import org.http4k.contract.Route
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.DELETE
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import org.http4k.lens.FormField
import org.http4k.lens.FormValidator.Strict
import org.http4k.lens.Path
import org.http4k.lens.WebForm
import org.http4k.lens.int
import org.http4k.lens.webForm
import verysecuresystems.EmailAddress
import verysecuresystems.Id
import verysecuresystems.User
import verysecuresystems.Username

class UserDirectory(private val client: HttpHandler) {
    fun create(name: Username, inEmail: EmailAddress): User =
        client(
            Create.route.newRequest()
                .with(Create.form of
                    WebForm().with(
                        Create.email of inEmail,
                        Create.username of name)),
            Create.response)

    fun delete(id: Id): Unit =
        client(Delete.route.newRequest().with(Delete.id of id)).let {
            if (it.status != ACCEPTED) {
                throw RemoteSystemProblem("user directory", it.status)
            }
        }

    fun list(): List<User> = client(UserList.route.newRequest(), UserList.response)

    fun lookup(username: Username): User? =
        client(Lookup.route.newRequest()
            .with(Lookup.username of username)).let {
            if (it.status == NOT_FOUND) {
                return null
            } else if (it.status != OK) {
                throw RemoteSystemProblem("user directory", it.status)
            } else {
                Lookup.response(it)
            }
        }

    companion object {
        object Create {
            val email = FormField.map(::EmailAddress, EmailAddress::value).required("email")
            val username = FormField.map(::Username, Username::value).required("username")
            val form = Body.webForm(Strict, email, username).toLens()
            val route = Route().body(form).at(POST) / "user"
            val response = Body.auto<User>().toLens()
        }

        object Delete {
            val id = Path.int().map(::Id, Id::value).of("id")
            val route = Route().at(DELETE) / "user" / id
            val response = Body.auto<User>().toLens()
        }

        object UserList {
            val route = Route().at(GET) / "user"
            val response = Body.auto<Array<User>>().map(Array<User>::toList, List<User>::toTypedArray).toLens()
        }

        object Lookup {
            val username = Path.map(::Username, Username::value).of("username")
            val route = Route().at(GET) / "user" / username
            val response = Body.auto<User>().toLens()
        }

    }
}