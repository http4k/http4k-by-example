package diagnostic

import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.assertion.assertThat
import env.TestEnvironment
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.http4k.hamkrest.hasBody
import org.http4k.hamkrest.hasStatus
import org.junit.jupiter.api.Test

class DiagnosticTest {

    private val env = TestEnvironment()

    @Test
    fun `responds to ping`() {
        val response = env.app(Request(GET, "/internal/ping"))
        assertThat(response, hasStatus(OK).and(hasBody("pong")))
    }

    @Test
    fun `responds to uptime`() {
        val response = env.app(Request(GET, "/internal/uptime"))
        assertThat(response, hasStatus(OK).and(hasBody("uptime is: 0s")))
    }

//
//    it("has a sitemap") {
//        val response = env.responseTo(Request("/sitemap.xml"))
//        response.status shouldBe Ok
//        response.contentType.startsWith(ContentTypes.APPLICATION_XML.value) shouldBe true
//        val siteMap = trim(XML.loadString(response.content))
//        ((siteMap \\ "urlset" \\ "url") (0) \\ "loc").text shouldBe "http://my.security.system/users"
//        ((siteMap \\ "urlset" \\ "url") (1) \\ "loc").text shouldBe "http://my.security.system"
//    }
//
}
