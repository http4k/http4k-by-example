package verysecuresystems.external

import org.http4k.contract.Route
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.format.Jackson.auto
import verysecuresystems.UserEntry
import verysecuresystems.Username
import java.time.Clock

class EntryLogger(client: HttpHandler, clock: Clock) {
    fun enter(username: Username): UserEntry = TODO()

    fun exit(username: Username): UserEntry = TODO()

    fun list(): List<UserEntry> = TODO()


    companion object {

        object Contract {
            object Entry {
                val body = Body.auto<UserEntry>().toLens()
                val route = Route().body(body).at(Method.POST) / "entry"
                val response = Body.auto<UserEntry>().toLens()
            }

            object Exit {
                val body = Body.auto<UserEntry>().toLens()
                val route = Route().body(body).at(Method.POST) / "exit"
                val response = Body.auto<UserEntry>().toLens()
            }

            object LogList {
                val body = Body.auto<List<UserEntry>>().toLens()
                val route = Route().at(Method.GET) / "list"
                val response = Body.auto<List<UserEntry>>().toLens()
            }
        }

    }

}