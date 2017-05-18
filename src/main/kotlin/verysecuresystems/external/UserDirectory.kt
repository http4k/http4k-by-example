package verysecuresystems.external

import org.http4k.core.HttpHandler
import verysecuresystems.EmailAddress
import verysecuresystems.Id
import verysecuresystems.User
import verysecuresystems.Username

class UserDirectory(client: HttpHandler) {
    fun create(name: Username, inEmail: EmailAddress): User = TODO()
    fun delete(id: Id): Unit = TODO()
    fun list(): List<User> = TODO()
    fun lookup(username: Username): User? = TODO()
}