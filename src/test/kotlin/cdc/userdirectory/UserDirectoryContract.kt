package cdc.userdirectory

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.core.HttpHandler
import org.junit.jupiter.api.Test
import verysecuresystems.EmailAddress
import verysecuresystems.Username
import verysecuresystems.external.UserDirectory

/**
 * This represents the contract that both the real and fake EntryLogger servers will adhere to.
 */
interface UserDirectoryContract {
    val http: HttpHandler
    val username: Username
    val email: EmailAddress

    fun userDirectory() = UserDirectory(http)

    @Test
    fun `is empty initially`() {
        assertThat(userDirectory().lookup(username), absent())
        assertThat(userDirectory().list(), equalTo(listOf()))
    }

    @Test
    fun `can create a user`() {
        val created = userDirectory().create(username, email)
        assertThat(created.name, equalTo(username))
        assertThat(created.email, equalTo(email))
    }

    @Test
    fun `can lookup a user by username`() {
        val created = userDirectory().create(username, email)
        assertThat(userDirectory().lookup(username), equalTo(created))
    }

    @Test
    fun `can list users`() {
        val created = userDirectory().create(username, email)
        assertThat(userDirectory().list(), equalTo(listOf(created)))
    }

    @Test
    fun `can delete user`() {
        val created = userDirectory().create(username, email)
        userDirectory().delete(created.id)
        assertThat(userDirectory().lookup(username), absent())
        assertThat(userDirectory().list(), equalTo(listOf()))
    }
}
