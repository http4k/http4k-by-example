package verysecuresystems.external

import org.http4k.contract.Route
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.DELETE
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
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
            Request(POST, "/user")
                .with(Contract.Create.form of
                    WebForm().with(
                        Contract.Create.email of inEmail,
                        Contract.Create.username of name)),
            Contract.Create.response)

    fun delete(id: Id): Unit = client(Request(DELETE, "/user/${id.value}")).let {
        if (it.status != ACCEPTED) {
            throw RemoteSystemProblem("user directory", it.status)
        }
    }

    fun list(): List<User> = client(Request(GET, "/user"), Contract.UserList.response)

    fun lookup(username: Username): User? = client(Request(GET, "/user/${username.value}")).let {
        if (it.status == NOT_FOUND) {
            return null
        } else if (it.status != OK) {
            throw RemoteSystemProblem("user directory", it.status)
        } else {
            Contract.Lookup.response(it)
        }
    }

    companion object {
        object Contract {
            object Create {
                val email = FormField.map(::EmailAddress, EmailAddress::value).required("email")
                val username = FormField.map(::Username, Username::value).required("username")
                val form = Body.webForm(Strict, email, username).toLens()
                val route = Route().body(form).at(POST) / "user"
                val response = Body.auto<User>().toLens()
            }

            object Delete {
                val id = Path.int().map(::Id).of("id")
                val route = Route().at(DELETE) / "user" / id
                val response = Body.auto<User>().toLens()
            }

            object UserList {
                val route = Route().at(GET) / "user"
                val response = Body.auto<Array<User>>().map(Array<User>::toList, List<User>::toTypedArray).toLens()
            }

            object Lookup {
                val username = Path.map(::Username).of("username")
                val route = Route().at(GET) / "user" / username
                val response = Body.auto<User>().toLens()
            }
        }

    }
}