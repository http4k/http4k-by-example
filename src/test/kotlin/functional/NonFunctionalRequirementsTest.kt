package functional

import com.natpryce.hamkrest.containsSubstring
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import env.TestEnvironment
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.http4k.format.Jackson
import org.junit.Test

class NonFunctionalRequirementsTest {

    private val env = TestEnvironment()

    @Test
    fun `responds to ping`() {
        val response = env.app(Request(GET, "/internal/ping"))
        response.status shouldMatch equalTo(OK)
        response.bodyString() shouldMatch equalTo("pong")
    }

    @Test
    fun `responds to uptime`() {
        val response = env.app(Request(GET, "/internal/uptime"))
        response.status shouldMatch equalTo(OK)
        response.bodyString() shouldMatch equalTo("uptime is: 0s")
    }

    @Test
    fun `serves static content`() {
        val response = env.app(Request(GET, "/style.css"))
        response.status shouldMatch equalTo(OK)
        response.bodyString() shouldMatch containsSubstring("font-family: \"Droid Sans\"")
    }

    @Test
    fun `provides API documentation in swagger 2dot0 format`() {
        val response = env.app(Request(GET, "/api/api-docs"))
        response.status shouldMatch equalTo(OK)
        Jackson.parse(response.bodyString())["swagger"].textValue() shouldMatch equalTo("2.0")
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
