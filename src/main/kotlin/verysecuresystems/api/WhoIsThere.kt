package verysecuresystems.api

import org.http4k.contract.ContractRoute
import org.http4k.contract.meta
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import verysecuresystems.EmailAddress
import verysecuresystems.Id
import verysecuresystems.User
import verysecuresystems.Username

/**
 * Retrieves a list of the users inside the building.
 */
fun WhoIsThere(inhabitants: Iterable<Username>,
               lookup: (Username) -> User?): ContractRoute {
    val users = Body.auto<List<User>>().toLens()

    val listUsers: HttpHandler = {
        Response(OK).with(users of inhabitants.mapNotNull(lookup))
    }

    return "/whoIsThere" meta {
        summary = "List current users in the building"
        returning(OK, users to listOf(User(Id(1), Username("A user"), EmailAddress("user@bob.com"))), "Inhabitant list")
    } bindContract Method.GET to listUsers
}