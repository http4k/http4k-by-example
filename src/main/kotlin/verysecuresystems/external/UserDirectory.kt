package verysecuresystems.external

import org.http4k.contract.Route
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.DELETE
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.format.Jackson.auto
import org.http4k.lens.FormField
import org.http4k.lens.FormValidator.Strict
import org.http4k.lens.Path
import org.http4k.lens.int
import org.http4k.lens.webForm
import verysecuresystems.EmailAddress
import verysecuresystems.Id
import verysecuresystems.User
import verysecuresystems.Username

class UserDirectory(client: HttpHandler) {
    fun create(name: Username, inEmail: EmailAddress): User = TODO()
    fun delete(id: Id): Unit = TODO()
    fun list(): List<User> = TODO()
    fun lookup(username: Username): User? = TODO()

    companion object {
        object Contract {
            object Create {
                val email = FormField.map(::EmailAddress).required("email")
                val username = FormField.map(::Username).required("username")
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
                val response = Body.auto<List<User>>().toLens()
            }

            object Lookup {
                val username = Path.map(::Username).of("username")
                val route = Route().at(GET) / "user" / username
                val response = Body.auto<User>().toLens()
            }
        }

    }
}