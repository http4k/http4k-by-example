package functional

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import env.TestEnvironment
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.Test

class WebTest {
    private val env = TestEnvironment()

    @Test
    fun `homepage`() {
        val response = env.app(Request(Method.GET, ""))
        response.status shouldMatch equalTo(Status.OK)
        response.header("content-type") shouldMatch equalTo("text/html")
    }

    @Test
    fun `manage users`() {
        val response = env.app(Request(Method.GET, "/users"))
        response.status shouldMatch equalTo(Status.OK)
        response.header("content-type") shouldMatch equalTo("text/html")
    }
}