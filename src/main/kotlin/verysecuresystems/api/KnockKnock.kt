package verysecuresystems.api

import org.http4k.contract.ContractRoute
import org.http4k.contract.contract
import org.http4k.contract.meta
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.CONFLICT
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import org.http4k.lens.Query
import verysecuresystems.Message
import verysecuresystems.User
import verysecuresystems.UserEntry
import verysecuresystems.Username

object KnockKnock {
    operator fun invoke(lookup: (Username) -> User?,
                        add: (Username) -> Boolean,
                        entryLogger: (Username) -> UserEntry): ContractRoute {
        val username = Query.map(::Username).required("username")
        val message = Body.auto<Message>().toLens()

        val userEntry: HttpHandler = {
            lookup(username(it))
                ?.let {
                    if (add(it.name)) {
                        entryLogger(it.name)
                        Response(ACCEPTED).with(message of Message("Access granted"))
                    } else {
                        Response(CONFLICT).with(message of Message("User is already inside building"))
                    }
                }
                ?: Response(NOT_FOUND).with(message of Message("Unknown user"))
        }

        return "/knock" meta {
            queries += username
            summary = "User enters the building"
            returning(ACCEPTED to "Access granted")
            returning(NOT_FOUND to "Unknown user")
            returning(CONFLICT to "User is already inside building")
            returning(UNAUTHORIZED to "Incorrect key")
        } bindContract POST to userEntry
    }
}


fun main() {
    val a = contract {
        routes += "/" meta {
            queries += Query.map(::Username).required("username")
        } bindContract Method.GET to { Response(OK) }
    }

    println(a(Request(Method.GET, "/")))
}
