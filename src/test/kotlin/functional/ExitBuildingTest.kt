package functional

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import env.TestEnvironment
import env.enterBuilding
import env.exitBuilding
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.hamkrest.hasStatus
import org.junit.Test
import verysecuresystems.EmailAddress
import verysecuresystems.Id
import verysecuresystems.User
import verysecuresystems.UserEntry
import verysecuresystems.Username

class ExitingBuildingTest {

    private val env = TestEnvironment()

    @Test
    fun `rejects missing username in exit endpoint`() {
        env.exitBuilding(null, "realSecret") shouldMatch hasStatus(BAD_REQUEST)
    }

    @Test
    fun `exit endpoint is protected with a secret key`() {
        env.exitBuilding("Bob", "fakeSecret") shouldMatch hasStatus(UNAUTHORIZED)
    }

    @Test
    fun `allows known user to exit and logs entry and exit`() {
        env.userDirectory.contains(User(Id(1), Username("Bob"), EmailAddress("bob@bob.com")))
        env.enterBuilding("Bob", "realSecret") shouldMatch hasStatus(ACCEPTED)
        env.exitBuilding("Bob", "realSecret") shouldMatch hasStatus(ACCEPTED)
        env.entryLogger.entries shouldMatch equalTo(listOf(
            UserEntry("Bob", true, env.clock.millis()),
            UserEntry("Bob", false, env.clock.millis())
        ))
    }

    @Test
    fun `does not allow exit when not in building`() {
        env.userDirectory.contains(User(Id(1), Username("Bob"), EmailAddress("bob@bob.com")))
        env.exitBuilding("Bob", "realSecret") shouldMatch hasStatus(NOT_FOUND)
    }
}
