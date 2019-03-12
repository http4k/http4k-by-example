package env

import org.http4k.contract.contract
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import verysecuresystems.Id
import verysecuresystems.User
import verysecuresystems.external.UserDirectory.Companion.Create
import verysecuresystems.external.UserDirectory.Companion.Delete
import verysecuresystems.external.UserDirectory.Companion.Lookup
import verysecuresystems.external.UserDirectory.Companion.UserList
import java.util.Random

class FakeUserDirectory : HttpHandler {

    private val users = mutableMapOf<Id, User>()

    fun contains(newUser: User) = users.put(newUser.id, newUser)

    private val app = contract {
        routes += Create.endpoint to {
            val form = Create.form(it)
            val newUser = User(Id(Random().nextInt()), Create.username(form), Create.email(form))
            users[newUser.id] = newUser
            Response(CREATED).with(Create.user of newUser)
        }
        routes += Delete.endpoint to { id ->
            {
                users.remove(id)?.let {
                    Response(ACCEPTED).with(Delete.response of it)
                } ?: Response(NOT_FOUND)
            }
        }
        routes += Lookup.route to { username ->
            {
                users.values.firstOrNull { it.name == username }
                    ?.let { Response(OK).with(Lookup.user of it) }
                    ?: Response(NOT_FOUND)
            }
        }
        routes += UserList.route to {
            Response(OK).with(UserList.users of users.values.toTypedArray())
        }
    }

    override fun invoke(p1: Request) = app(p1)
}