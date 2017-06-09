package verysecuresystems.api

import org.http4k.contract.ContractRoute
import org.http4k.contract.RouteMeta
import org.http4k.contract.bind
import org.http4k.contract.meta
import org.http4k.contract.query
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

    fun route(inhabitants: Inhabitants, userDirectory: UserDirectory, entryLogger: EntryLogger): ContractRoute {
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

        return (
            "/knock"
                query username
                to POST
                bind userEntry
                meta RouteMeta("User enters the building")
                .returning("Access granted" to ACCEPTED)
                .returning("Unknown user" to NOT_FOUND)
                .returning("User is already inside building" to CONFLICT)
                .returning("Incorrect key" to UNAUTHORIZED)
            )
    }
}

