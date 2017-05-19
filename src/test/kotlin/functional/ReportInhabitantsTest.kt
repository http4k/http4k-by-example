package functional

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import env.TestEnvironment
import env.checkInhabitants
import env.enterBuilding
import env.exitBuilding
import org.http4k.core.Body
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.format.Jackson.auto
import org.junit.Test
import verysecuresystems.EmailAddress
import verysecuresystems.Id
import verysecuresystems.User
import verysecuresystems.Username

class ReportInhabitantsTest {

    private val env = TestEnvironment()
    private val inhabitants = Body.auto<List<User>>().toLens()

    @Test
    fun `who is there endpoint is protected with a secret key`() {
        env.checkInhabitants("fakeSecret").status shouldMatch equalTo(UNAUTHORIZED)
    }

    @Test
    fun `initially there is no-one inside`() {
        val checkInhabitants = env.checkInhabitants("realSecret")
        checkInhabitants.status shouldMatch equalTo(OK)
        inhabitants(checkInhabitants) shouldMatch equalTo(listOf())
    }

    @Test
    fun `when a user enters the building`() {
        val user = User(Id(1), Username("Bob"), EmailAddress("bob@bob.com"))

        env.enterBuilding("Bob", "realSecret")
        inhabitants(env.checkInhabitants("realSecret")) shouldMatch equalTo(listOf(user))

        env.exitBuilding("Bob", "realSecret")
        inhabitants(env.checkInhabitants("realSecret")) shouldMatch equalTo(listOf())
    }
}
