package functional

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import env.TestEnvironment
import env.enterBuilding
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.CONFLICT
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.hamkrest.hasStatus
import org.junit.jupiter.api.Test
import verysecuresystems.EmailAddress
import verysecuresystems.Id
import verysecuresystems.User
import verysecuresystems.UserEntry
import verysecuresystems.Username

class EnteringBuildingTest {

    private val env = TestEnvironment()

    @Test
    fun `unknown user is not allowed into building`() {
        env.enterBuilding("Rita", "realSecret") shouldMatch hasStatus(NOT_FOUND)
    }

    @Test
    fun `rejects missing username in entry endpoint`() {
        env.enterBuilding(null, "realSecret") shouldMatch hasStatus(BAD_REQUEST)
    }

    @Test
    fun `entry endpoint is protected with a secret key`() {
        env.enterBuilding("Bob", "fakeSecret") shouldMatch hasStatus(UNAUTHORIZED)
    }

    @Test
    fun `allows known user in and logs entry`() {
        env.userDirectory.contains(User(Id(1), Username("Bob"), EmailAddress("bob@bob.com")))
        env.enterBuilding("Bob", "realSecret") shouldMatch hasStatus(ACCEPTED)
        env.entryLogger.entries shouldMatch equalTo(listOf(UserEntry("Bob", true, env.clock.millis())))
    }

    @Test
    fun `does not allow double entry`() {
        env.userDirectory.contains(User(Id(1), Username("Bob"), EmailAddress("bob@bob.com")))
        env.enterBuilding("Bob", "realSecret") shouldMatch hasStatus(ACCEPTED)
        env.enterBuilding("Bob", "realSecret") shouldMatch hasStatus(CONFLICT)
    }
}

