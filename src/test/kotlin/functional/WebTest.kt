package functional

import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.assertion.assertThat
import env.TestEnvironment
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.http4k.hamkrest.hasHeader
import org.http4k.hamkrest.hasStatus
import org.junit.jupiter.api.Test

class WebTest {
    private val env = TestEnvironment()

    @Test
    fun homepage() {
        val response = env.app(Request(GET, ""))
        assertThat(response, hasStatus(OK).and(hasHeader("content-type", "text/html; charset=utf-8")))
    }

    @Test
    fun `manage users`() {
        val response = env.app(Request(GET, "/users"))
        assertThat(response, hasStatus(OK).and(hasHeader("content-type", "text/html; charset=utf-8")))
    }
}