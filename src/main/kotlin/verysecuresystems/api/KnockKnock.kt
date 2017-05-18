package verysecuresystems.api

import org.http4k.contract.Route
import org.http4k.contract.ServerRoute
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import org.http4k.lens.Query
import verysecuresystems.Inhabitants
import verysecuresystems.Message
import verysecuresystems.Username
import verysecuresystems.external.EntryLogger
import verysecuresystems.external.UserDirectory

object KnockKnock {
    private val username = Query.map(::Username).required("username")
    private val message = Body.auto<Message>().required()

    fun route(inhabitants: Inhabitants, userDirectory: UserDirectory, entryLogger: EntryLogger): ServerRoute {
        val userEntry: HttpHandler = {
            val user = userDirectory.lookup(username(it))
            user?.let {
                if (inhabitants.add(user.name)) {
                    entryLogger.enter(user.name)
                    Response(ACCEPTED).with(message to Message("Access granted"))
                } else {
                    Response(BAD_REQUEST).with(message to Message("User is already inside building"))
                }
            } ?: Response(NOT_FOUND).with(message to Message("Unknown user"))
        }

        return Route("User enters the building")
            .query(username)
            .returning("Access granted" to ACCEPTED)
            .returning("Unknown user" to NOT_FOUND)
            .returning("User is already inside building" to BAD_REQUEST)
            .returning("Incorrect key" to UNAUTHORIZED)
            .at(POST) / "knock" bind userEntry
    }
}

