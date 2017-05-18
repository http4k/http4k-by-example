package verysecuresystems.api

import org.http4k.contract.Route
import org.http4k.contract.ServerRoute
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import org.http4k.lens.Query
import verysecuresystems.Inhabitants
import verysecuresystems.Message
import verysecuresystems.Username
import verysecuresystems.external.EntryLogger

object ByeBye {
    private val username = Query.map(::Username).required("username")
    private val message = Body.auto<Message>().required()

    fun route(inhabitants: Inhabitants, entryLogger: EntryLogger): ServerRoute {

        val userExit: HttpHandler = {
            val exiting = username(it)
            if (inhabitants.remove(exiting)) {
                entryLogger.exit(exiting)
                Response(Status.ACCEPTED).with(message to Message("processing"))
            } else Response(Status.BAD_REQUEST).with(message to Message("User is not inside building"))
        }

        return Route("User exits the building")
            .query(username)
            .returning("Exit granted" to Status.OK)
            .returning("User is not inside building" to Status.BAD_REQUEST)
            .returning("Incorrect key" to Status.UNAUTHORIZED)
            .at(POST) / "bye" bind userExit
    }
}