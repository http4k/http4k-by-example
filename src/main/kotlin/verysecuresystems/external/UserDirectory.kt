package verysecuresystems.external

import org.http4k.contract.bindContract
import org.http4k.contract.div
import org.http4k.contract.meta
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.DELETE
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Uri
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import org.http4k.lens.FormField
import org.http4k.lens.Path
import org.http4k.lens.Validator.Strict
import org.http4k.lens.WebForm
import org.http4k.lens.int
import org.http4k.lens.webForm
import verysecuresystems.EmailAddress
import verysecuresystems.Id
import verysecuresystems.User
import verysecuresystems.Username

class UserDirectory(private val http: HttpHandler) {

    fun create(name: Username, inEmail: EmailAddress) = with(Create) {
        http.perform(
            route.newRequest(Uri.of(""))
                .with(form of WebForm()
                    .with(email of inEmail, username of name)),
            response)
    }

    fun delete(idToDelete: Id) = with(Delete) {
        http(route.newRequest(Uri.of("")).with(id of idToDelete)).let {
            if (it.status != ACCEPTED) throw RemoteSystemProblem("user directory", it.status)
        }
    }

    fun list(): List<User> = with(UserList) {
        val request = route.newRequest(Uri.of(""))
        http.perform(request, response).asList()
    }

    fun lookup(search: Username): User? = with(Lookup) {
        http(route.newRequest(Uri.of(""))
            .with(username of search))
            .let {
                when {
                    it.status == NOT_FOUND -> return null
                    it.status != OK -> throw RemoteSystemProblem("user directory", it.status)
                    else -> response(it)
                }
            }
    }

    companion object {
        object Create {
            val email = FormField.map(::EmailAddress, EmailAddress::value).required("email")
            val username = FormField.map(::Username, Username::value).required("username")
            val form = Body.webForm(Strict, email, username).toLens()
            val route = "/user" meta { receiving(form) } bindContract POST
            val response = Body.auto<User>().toLens()
        }

        object Delete {
            val id = Path.int().map(::Id, Id::value).of("id")
            val route = "/user" / id bindContract DELETE
            val response = Body.auto<User>().toLens()
        }

        object UserList {
            val route = "/user" bindContract GET
            val response = Body.auto<Array<User>>().toLens()
        }

        object Lookup {
            val username = Path.map(::Username, Username::value).of("username")
            val route = "/user" / username bindContract GET
            val response = Body.auto<User>().toLens()
        }
    }
}