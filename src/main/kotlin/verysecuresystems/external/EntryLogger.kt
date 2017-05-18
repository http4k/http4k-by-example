package verysecuresystems.external

import org.http4k.core.HttpHandler
import verysecuresystems.UserEntry
import verysecuresystems.Username

class EntryLogger(client: HttpHandler) {
    fun enter(username: Username): UserEntry = TODO()

    fun exit(username: Username): UserEntry = TODO()

    fun list(): List<UserEntry> = TODO()
}