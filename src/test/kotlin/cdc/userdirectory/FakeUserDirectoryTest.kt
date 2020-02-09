package cdc.userdirectory

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.throws
import env.userdirectory.FakeUserDirectory
import org.http4k.cloudnative.RemoteRequestFailed
import org.junit.jupiter.api.Test
import verysecuresystems.EmailAddress
import verysecuresystems.Username

class FakeUserDirectoryTest : UserDirectoryContract {
    override val http = FakeUserDirectory()
    override val username = Username("ElonMusk")
    override val email = EmailAddress("elon@tesla.com")

    // test a behaviour here which is not supported by the "real" server
    @Test
    fun `list users blows up and return`() {
        http.blowsUp()
        assertThat({ userDirectory().list() }, throws<RemoteRequestFailed>())
    }

}
