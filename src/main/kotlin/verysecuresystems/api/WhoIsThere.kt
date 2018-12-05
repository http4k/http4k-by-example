package verysecuresystems.api

import org.http4k.contract.ContractRoute
import org.http4k.contract.bindContract
import org.http4k.contract.meta
import org.http4k.core.*
import org.http4k.core.Status.Companion.OK
import org.http4k.format.Jackson.auto
import verysecuresystems.*
import verysecuresystems.external.UserDirectory

object WhoIsThere {
    private val users = Body.auto<Array<User>>().toLens()

    fun route(inhabitants: Inhabitants, userDirectory: UserDirectory): ContractRoute {
        val listUsers: HttpHandler = {
            Response(OK).with(users of inhabitants.mapNotNull(userDirectory::lookup).toTypedArray())
        }

        return "/whoIsThere" meta {
                summary = "List current users in the building"
                returning("Inhabitant list" to
                    Response(OK)
                        .with(users of arrayOf(User(Id(1), Username("A user"), EmailAddress("user@bob.com")))))

            } bindContract Method.GET to listUsers
    }
}