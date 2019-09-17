package functional

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import env.TestEnvironment
import env.checkInhabitants
import env.enterBuilding
import env.exitBuilding
import env.oauthserver.ACCESS_TOKEN_PREFIX
import org.http4k.core.Body
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.TEMPORARY_REDIRECT
import org.http4k.format.Jackson.auto
import org.http4k.hamkrest.hasStatus
import org.http4k.security.AccessToken
import org.junit.jupiter.api.Test
import verysecuresystems.EmailAddress
import verysecuresystems.Id
import verysecuresystems.User
import verysecuresystems.Username
import java.util.UUID

class ReportInhabitantsTest {

    private val env = TestEnvironment()
    private val inhabitants = Body.auto<List<User>>().toLens()

    @Test
    fun `who is there endpoint is protected with oauth token`() {
        assertThat(env.checkInhabitants(null).status, equalTo(TEMPORARY_REDIRECT))
    }

    @Test
    fun `initially there is no-one inside`() {
        val checkInhabitants = env.checkInhabitants(AccessToken(ACCESS_TOKEN_PREFIX + UUID.randomUUID()))
        assertThat(checkInhabitants, hasStatus(OK))
        assertThat(inhabitants(checkInhabitants), equalTo(listOf()))
    }

    @Test
    fun `when a user enters the building`() {
        val user = User(Id(1), Username("Bob"), EmailAddress("bob@bob.com"))

        env.userDirectory.contains(user)

        val accessToken = AccessToken(ACCESS_TOKEN_PREFIX + UUID.randomUUID())

        env.enterBuilding("Bob", accessToken)
        assertThat(inhabitants(env.checkInhabitants(accessToken)), equalTo(listOf(user)))

        env.exitBuilding("Bob", accessToken)
        assertThat(inhabitants(env.checkInhabitants(accessToken)), equalTo(listOf()))
    }
}
