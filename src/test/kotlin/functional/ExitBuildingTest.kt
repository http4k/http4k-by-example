package functional

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import env.TestEnvironment
import env.enterBuilding
import env.exitBuilding
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.TEMPORARY_REDIRECT
import org.http4k.hamkrest.hasStatus
import org.junit.jupiter.api.Test
import verysecuresystems.EmailAddress
import verysecuresystems.Id
import verysecuresystems.User
import verysecuresystems.UserEntry
import verysecuresystems.Username

class ExitingBuildingTest {

    private val env = TestEnvironment()

    @Test
    fun `rejects missing username in exit endpoint`() {
        assertThat(env.exitBuilding(null, env.obtainAccessToken()), hasStatus(BAD_REQUEST))
    }

    @Test
    fun `exit endpoint is protected with oauth token`() {
        assertThat(env.exitBuilding("Bob", null), hasStatus(TEMPORARY_REDIRECT))
    }

    @Test
    fun `allows known user to exit and logs entry and exit`() {
        env.userDirectory.contains(User(Id(1), Username("Bob"), EmailAddress("bob@bob.com")))

        val accessToken = env.obtainAccessToken()

        assertThat(env.enterBuilding("Bob", accessToken), hasStatus(ACCEPTED))
        assertThat(env.exitBuilding("Bob", accessToken), hasStatus(ACCEPTED))
        assertThat(env.entryLogger.entries, equalTo(listOf(
            UserEntry("Bob", true, env.clock.millis()),
            UserEntry("Bob", false, env.clock.millis())
        )))
    }

    @Test
    fun `does not allow exit when not in building`() {
        env.userDirectory.contains(User(Id(1), Username("Bob"), EmailAddress("bob@bob.com")))
        assertThat(env.exitBuilding("Bob", env.obtainAccessToken()), hasStatus(NOT_FOUND))
    }
}
