package verysecuresystems.external

import org.http4k.cloudnative.UpstreamRequestFailed
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
import org.http4k.core.then
import org.http4k.core.with
import org.http4k.filter.ClientFilters
import org.http4k.filter.HandleUpstreamRequestFailed
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

class UserDirectory(http: HttpHandler) {

    private val http = ClientFilters.HandleUpstreamRequestFailed().then(http)

    fun create(name: Username, inEmail: EmailAddress) = with(Create) {
        user(
            http(endpoint.newRequest().with(form of WebForm().with(email of inEmail, username of name)))
        )
    }

    fun delete(idToDelete: Id) = with(Delete) {
        with(http(endpoint.newRequest().with(id of idToDelete))) {
            if (status != ACCEPTED) throw UpstreamRequestFailed(status, "user directory")
        }
    }

    fun list(): List<User> = with(UserList) {
        users(http(route.newRequest())).asList()
    }

    fun lookup(endpoint: Username): User? = with(Lookup) {
        with(http(route.newRequest().with(username of endpoint))) {
            when (status) {
                NOT_FOUND -> return null
                OK -> user(this)
                else -> throw UpstreamRequestFailed(status, "user directory")
            }
        }
    }

    companion object {
        object Create {
            val email = FormField.map(::EmailAddress, EmailAddress::value).required("email")
            val username = FormField.map(::Username, Username::value).required("username")
            val form = Body.webForm(Strict, email, username).toLens()
            val endpoint = "/user" meta { receiving(form) } bindContract POST
            val user = Body.auto<User>().toLens()
        }

        object Delete {
            val id = Path.int().map(::Id, Id::value).of("id")
            val endpoint = "/user" / id bindContract DELETE
            val response = Body.auto<User>().toLens()
        }

        object UserList {
            val route = "/user" bindContract GET
            val users = Body.auto<Array<User>>().toLens()
        }

        object Lookup {
            val username = Path.map(::Username, Username::value).of("username")
            val route = "/user" / username bindContract GET
            val user = Body.auto<User>().toLens()
        }
    }
}