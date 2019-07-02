package verysecuresystems.api

import org.http4k.contract.ContractRoute
import org.http4k.contract.meta
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import org.http4k.lens.Query
import verysecuresystems.Message
import verysecuresystems.UserEntry
import verysecuresystems.Username

/**
 * Allows users to exit the building, but only if they are already inside.
 */
object ByeBye {
   operator fun invoke(removeUser: (Username) -> Boolean,
                       entryLogger: (Username) -> UserEntry): ContractRoute {
        val username = Query.map(::Username).required("username")
        val message = Body.auto<Message>().toLens()

        val userExit: HttpHandler = {
            val exiting = username(it)
            if (removeUser(exiting)) {
                entryLogger(exiting)
                Response(ACCEPTED).with(message of Message("processing"))
            } else Response(NOT_FOUND).with(message of Message("User is not inside building"))
        }

        return "/bye" meta {
            summary = "User exits the building"
            queries += username
            returning(ACCEPTED to "Exit granted")
            returning(NOT_FOUND to "User is not inside building")
            returning(UNAUTHORIZED to "Incorrect key")
        } bindContract POST to userExit
    }
}