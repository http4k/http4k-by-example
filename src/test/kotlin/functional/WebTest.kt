package functional

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import env.TestEnvironment
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.junit.Test

class WebTest {
    private val env = TestEnvironment()

    @Test
    fun `homepage`() {
        val response = env.app(Request(GET, ""))
        response.status shouldMatch equalTo(OK)
        response.header("content-type") shouldMatch equalTo("text/html")
    }

    @Test
    fun `manage users`() {
        val response = env.app(Request(GET, "/users"))
        response.status shouldMatch equalTo(OK)
        response.header("content-type") shouldMatch equalTo("text/html")
    }
}