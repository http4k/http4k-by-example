package functional

import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.containsSubstring
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.has
import com.natpryce.hamkrest.should.shouldMatch
import env.TestEnvironment
import org.http4k.core.Body
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.http4k.format.Jackson
import org.http4k.hamkrest.hasBody
import org.http4k.hamkrest.hasStatus
import org.junit.jupiter.api.Test

class NonFunctionalRequirementsTest {

    private val env = TestEnvironment()

    @Test
    fun `responds to ping`() {
        val response = env.app(Request(GET, "/internal/ping"))
        response shouldMatch hasStatus(OK).and(hasBody("pong"))
    }

    @Test
    fun `responds to uptime`() {
        val response = env.app(Request(GET, "/internal/uptime"))
        response shouldMatch hasStatus(OK).and(hasBody("uptime is: 0s"))
    }

    @Test
    fun `serves static content`() {
        val response = env.app(Request(GET, "/style.css"))
        response shouldMatch hasStatus(OK).and(hasBody(has("content", Body::toString, containsSubstring("font-family: \"Droid Sans\""))))
    }

    @Test
    fun `provides API documentation in swagger 2dot0 format`() {
        val response = env.app(Request(GET, "/api/api-docs"))
        response shouldMatch hasStatus(OK)
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
