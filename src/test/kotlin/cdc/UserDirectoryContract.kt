package cdc

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import org.http4k.core.HttpHandler
import org.junit.Test
import verysecuresystems.EmailAddress
import verysecuresystems.User
import verysecuresystems.Username
import verysecuresystems.external.UserDirectory

/**
 * This represents the contract that both the real and fake EntryLogger servers will adhere to.
 */
abstract class UserDirectoryContract(handler: HttpHandler) {

    val userDirectory = UserDirectory(handler)

    abstract val username: Username
    abstract val email: EmailAddress

    var user: User? = null

    @Test
    fun `is empty initially`() {
        userDirectory.lookup(username) shouldMatch absent()
        userDirectory.list() shouldMatch equalTo(listOf())
    }

    @Test
    fun `can create a user`() {
        val created = userDirectory.create(username, email)
        created.name shouldMatch equalTo(username)
        created.email shouldMatch equalTo(email)
    }

    @Test
    fun `can lookup a user by username`() {
        val created = userDirectory.create(username, email)
        userDirectory.lookup(username) shouldMatch equalTo(created)
    }

    @Test
    fun `can list users`() {
        val created = userDirectory.create(username, email)
        val list = userDirectory.list()
        list shouldMatch equalTo(listOf(created))
    }

    @Test
    fun `can delete user`() {
        val created = userDirectory.create(username, email)
        userDirectory.delete(created.id)
        userDirectory.lookup(username) shouldMatch absent()
        userDirectory.list() shouldMatch equalTo(listOf())

    }
}
