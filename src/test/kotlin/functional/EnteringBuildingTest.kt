package functional

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import env.TestEnvironment
import env.enterBuilding
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.CONFLICT
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.TEMPORARY_REDIRECT
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
        assertThat(env.enterBuilding("Rita", env.obtainAccessToken()), hasStatus(NOT_FOUND))
    }

    @Test
    fun `rejects missing username in entry endpoint`() {
        assertThat(env.enterBuilding(null, env.obtainAccessToken()), hasStatus(BAD_REQUEST))
    }

    @Test
    fun `entry endpoint is protected with oauth token`() {
        assertThat(env.enterBuilding("Bob", null), hasStatus(TEMPORARY_REDIRECT))
    }

    @Test
    fun `allows known user in and logs entry`() {
        env.userDirectory.contains(User(Id(1), Username("Bob"), EmailAddress("bob@bob.com")))
        assertThat(env.enterBuilding("Bob", env.obtainAccessToken()), hasStatus(ACCEPTED))
        assertThat(env.entryLogger.entries, equalTo(listOf(UserEntry("Bob", true, env.clock.millis()))))
    }

    @Test
    fun `does not allow double entry`() {
        env.userDirectory.contains(User(Id(1), Username("Bob"), EmailAddress("bob@bob.com")))
        val accessToken = env.obtainAccessToken()

        assertThat(env.enterBuilding("Bob", accessToken), hasStatus(ACCEPTED))
        assertThat(env.enterBuilding("Bob", accessToken), hasStatus(CONFLICT))
    }
}

