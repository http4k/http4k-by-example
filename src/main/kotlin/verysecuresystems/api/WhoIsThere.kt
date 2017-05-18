package verysecuresystems.api

import org.http4k.contract.Route
import org.http4k.contract.ServerRoute
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import verysecuresystems.Inhabitants
import verysecuresystems.User
import verysecuresystems.external.UserDirectory

object WhoIsThere {
    private val users = Body.auto<List<User>>().required()

    fun route(inhabitants: Inhabitants, userDirectory: UserDirectory): ServerRoute {
        val listUsers: HttpHandler = {
            Response(OK).with(users to inhabitants.mapNotNull(userDirectory::lookup))
        }

        return Route("List current users in the building")
//            .returning(Ok(encode(Seq(User(Id(1), Username("A user"), EmailAddress("user@bob.com"))))))
            .at(GET) / "whoIsThere" bind listUsers
    }
}