package verysecuresystems.api

import org.http4k.contract.Route
import org.http4k.contract.ServerRoute
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
import verysecuresystems.Inhabitants
import verysecuresystems.Message
import verysecuresystems.Username
import verysecuresystems.external.EntryLogger
import verysecuresystems.external.UserDirectory

object KnockKnock {
    private val username = Query.map(::Username).required("username")
    private val message = Body.auto<Message>().toLens()

    fun route(inhabitants: Inhabitants, userDirectory: UserDirectory, entryLogger: EntryLogger): ServerRoute {
        val userEntry: HttpHandler = {
            userDirectory.lookup(username(it))
                ?.let {
                    if (inhabitants.add(it.name)) {
                        entryLogger.enter(it.name)
                        Response(ACCEPTED).with(message of Message("Access granted"))
                    } else {
                        Response(CONFLICT).with(message of Message("User is already inside building"))
                    }
                }
                ?: Response(NOT_FOUND).with(message of Message("Unknown user"))
        }

        return Route("User enters the building")
            .query(username)
            .returning("Access granted" to ACCEPTED)
            .returning("Unknown user" to NOT_FOUND)
            .returning("User is already inside building" to CONFLICT)
            .returning("Incorrect key" to UNAUTHORIZED)
            .at(POST) / "knock" bind userEntry
    }
}

