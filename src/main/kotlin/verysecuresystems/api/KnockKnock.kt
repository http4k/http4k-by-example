package verysecuresystems.api

import org.http4k.contract.ContractRoute
import org.http4k.contract.meta
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.CONFLICT
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import org.http4k.lens.Query
import verysecuresystems.Message
import verysecuresystems.User
import verysecuresystems.UserEntry
import verysecuresystems.Username

/**
 * Allows users to enter the building, but only if they exist.
 */
fun KnockKnock(lookup: (Username) -> User?,
               add: (Username) -> Boolean,
               entryLogger: (Username) -> UserEntry): ContractRoute {
    val username = Query.map(::Username).required("username")
    val message = Body.auto<Message>().toLens()

    val userEntry: HttpHandler = {
        lookup(username(it))?.name
            ?.let {
                if (add(it)) {
                    entryLogger(it)
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
        returning(ACCEPTED, message to Message("Access granted"))
        returning(NOT_FOUND, message to Message("Unknown user"))
        returning(CONFLICT, message to Message("User is already inside building"))
        returning(UNAUTHORIZED to "Incorrect key")
    } bindContract POST to userEntry
}