package env

import org.http4k.contract.contract
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
import java.util.*

class FakeUserDirectory {

    private val users = mutableMapOf<Id, User>()

    fun contains(newUser: User) = users.put(newUser.id, newUser)

    val app = contract(
            Create.route to {
                val form = Create.form(it)
                val newUser = User(Id(Random().nextInt()), Create.username(form), Create.email(form))
                users[newUser.id] = newUser
                Response(CREATED).with(Create.response of newUser)
            },
            Delete.route to { id ->
                {
                    users.remove(id)?.let {
                        Response(ACCEPTED).with(Delete.response of it)
                    } ?: Response(NOT_FOUND)
                }
            },
            Lookup.route to { username ->
                {
                    users.values.firstOrNull { it.name == username }
                            ?.let { Response(OK).with(Lookup.response of it) }
                            ?: Response(NOT_FOUND)
                }
            },
            UserList.route to {
                Response(OK).with(UserList.response of users.values.toTypedArray())
            })
}