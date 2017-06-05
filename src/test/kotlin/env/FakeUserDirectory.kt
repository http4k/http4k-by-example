package env

import org.http4k.core.Response
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.routing.contract
import org.http4k.routing.handler
import verysecuresystems.Id
import verysecuresystems.User
import verysecuresystems.external.UserDirectory
import verysecuresystems.external.UserDirectory.Companion.Create
import verysecuresystems.external.UserDirectory.Companion.Delete
import verysecuresystems.external.UserDirectory.Companion.Lookup
import verysecuresystems.external.UserDirectory.Companion.UserList
import java.util.*

class FakeUserDirectory {

    private val users = mutableMapOf<Id, User>()

    fun contains(newUser: User) = users.put(newUser.id, newUser)

    val app = contract(
        UserDirectory.Companion.Create.route handler {
            val form = Create.form(it)
            val newUser = User(Id(Random().nextInt()), Create.username(form), Create.email(form))
            users.put(newUser.id, newUser)
            Response(CREATED).with(Create.response of newUser)
        },
        Delete.route handler {
            id ->
            {
                users.remove(id)?.let {
                    Response(ACCEPTED).with(Delete.response of it)
                } ?: Response(NOT_FOUND)
            }
        },
        Lookup.route handler {
            username ->
            {
                users.values
                    .filter { it.name == username }
                    .firstOrNull()
                    ?.let { Response(OK).with(Lookup.response of it) }
                    ?: Response(NOT_FOUND)
            }
        },
        UserList.route handler {
            Response(OK).with(UserList.response of users.values.toTypedArray())
        })
}