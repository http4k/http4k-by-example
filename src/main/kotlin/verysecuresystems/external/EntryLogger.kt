package verysecuresystems.external

import verysecuresystems.UserEntry
import verysecuresystems.Username

class EntryLogger(client: org.http4k.core.HttpHandler) {
    fun enter(username: Username): UserEntry = TODO()

    fun exit(username: Username): UserEntry = TODO()

    fun list(): List<UserEntry> = TODO()
}