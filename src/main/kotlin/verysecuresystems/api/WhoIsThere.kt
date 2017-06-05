package verysecuresystems.api

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import org.http4k.routing.RouteMeta
import org.http4k.routing.ServerRoute
import org.http4k.routing.handler
import org.http4k.routing.meta
import verysecuresystems.EmailAddress
import verysecuresystems.Id
import verysecuresystems.Inhabitants
import verysecuresystems.User
import verysecuresystems.Username
import verysecuresystems.external.UserDirectory

object WhoIsThere {
    private val users = Body.auto<List<User>>().toLens()

    fun route(inhabitants: Inhabitants, userDirectory: UserDirectory): ServerRoute {
        val listUsers: HttpHandler = {
            Response(OK).with(users of inhabitants.mapNotNull(userDirectory::lookup))
        }

        return (
            "/whoIsThere"
                to Method.GET
                handler listUsers
                meta RouteMeta("List current users in the building")
                .returning("Inhabitant list" to
                    Response(OK)
                        .with(users of listOf(User(Id(1), Username("A user"), EmailAddress("user@bob.com")))))
            )
    }
}