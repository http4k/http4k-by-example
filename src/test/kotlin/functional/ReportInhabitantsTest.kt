package functional

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import env.TestEnvironment
import env.checkInhabitants
import env.enterBuilding
import env.exitBuilding
import org.http4k.core.Body
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.format.Jackson.auto
import org.http4k.hamkrest.hasStatus
import org.junit.jupiter.api.Test
import verysecuresystems.EmailAddress
import verysecuresystems.Id
import verysecuresystems.User
import verysecuresystems.Username

class ReportInhabitantsTest {

    private val env = TestEnvironment()
    private val inhabitants = Body.auto<Array<User>>().map(Array<User>::toList).toLens()

    @Test
    fun `who is there endpoint is protected with a secret key`() {
        assertThat(env.checkInhabitants("fakeSecret").status, equalTo(UNAUTHORIZED))
    }

    @Test
    fun `initially there is no-one inside`() {
        val checkInhabitants = env.checkInhabitants("realSecret")
        assertThat(checkInhabitants, hasStatus(OK))
        assertThat(inhabitants(checkInhabitants), equalTo(listOf()))
    }

    @Test
    fun `when a user enters the building`() {
        val user = User(Id(1), Username("Bob"), EmailAddress("bob@bob.com"))

        env.userDirectory.contains(user)

        env.enterBuilding("Bob", "realSecret")
        assertThat(inhabitants(env.checkInhabitants("realSecret")), equalTo(listOf(user)))

        env.exitBuilding("Bob", "realSecret")
        assertThat(inhabitants(env.checkInhabitants("realSecret")), equalTo(listOf()))
    }
}
