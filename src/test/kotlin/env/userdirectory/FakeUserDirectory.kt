package env.userdirectory

import org.http4k.chaos.withChaosEngine
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.DELETE
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import org.http4k.lens.FormField
import org.http4k.lens.Path
import org.http4k.lens.Validator.Strict
import org.http4k.lens.int
import org.http4k.lens.webForm
import org.http4k.routing.bind
import org.http4k.routing.routes
import verysecuresystems.EmailAddress
import verysecuresystems.Id
import verysecuresystems.User
import verysecuresystems.Username
import java.util.Random

class FakeUserDirectory : HttpHandler {

    private val users = mutableMapOf<Id, User>()

    fun contains(newUser: User) = users.put(newUser.id, newUser)

    private fun create(): HttpHandler {
        val email = FormField.map(::EmailAddress, EmailAddress::value).required("email")
        val username = FormField.map(::Username, Username::value).required("username")
        val form = Body.webForm(Strict, email, username).toLens()
        val user = Body.auto<User>().toLens()

        return {
            println(it)
            val data = form(it)
            val newUser = User(Id(Random().nextInt()), username(data), email(data))
            users[newUser.id] = newUser
            Response(CREATED).with(user of newUser)
        }
    }

    private fun delete(): HttpHandler {
        val id = Path.int().map(::Id, Id::value).of("id")
        return {
            users.remove(id(it))?.let { Response(ACCEPTED) } ?: Response(NOT_FOUND)
        }
    }

    private fun list(): HttpHandler = {
        val userList = Body.auto<Array<User>>().toLens()
        Response(OK).with(userList of users.values.toTypedArray())
    }

    private fun lookup(): HttpHandler {
        val username = Path.map(::Username, Username::value).of("username")
        val user = Body.auto<User>().toLens()

        return {
            val searchFor = username(it)
            users.values.firstOrNull { it.name == searchFor }
                ?.let { Response(OK).with(user of it) }
                ?: Response(NOT_FOUND)
        }
    }

    private val app = routes(
        "/user" bind GET to list(),
        "/user" bind POST to create(),
        "/user/{id}" bind DELETE to delete(),
        "/user/{username}" bind GET to lookup()
    ).withChaosEngine()

    override fun invoke(p1: Request) = app(p1)
}