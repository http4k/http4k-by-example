package env

import env.entrylogger.FakeEntryLogger
import env.oauthserver.OAuthClientData
import env.oauthserver.SimpleOAuthServer
import env.userdirectory.FakeUserDirectory
import org.http4k.core.Credentials
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Uri
import org.http4k.core.with
import org.http4k.lens.Header
import org.http4k.lens.Query
import org.http4k.routing.reverseProxy
import org.http4k.security.AccessToken
import org.http4k.testing.RecordingEvents
import org.http4k.webdriver.Http4kWebDriver
import org.openqa.selenium.By
import verysecuresystems.SecuritySystem
import java.time.Clock.fixed
import java.time.Instant.ofEpochSecond
import java.time.LocalDate
import java.time.LocalTime.MIDNIGHT
import java.time.ZoneId
import java.time.ZoneOffset.UTC
import java.util.Random

class TestEnvironment {
    val clock = fixed(ofEpochSecond(LocalDate.of(3000, 1, 1).toEpochSecond(MIDNIGHT, UTC)), ZoneId.of("UTC"))!!

    val userDirectory = FakeUserDirectory { Random(1).nextInt() }
    val entryLogger = FakeEntryLogger()

    private val events = RecordingEvents()

    private val oAuthClientData = OAuthClientData(
        Credentials("securityServer", "securityServerSecret"),
        Uri.of("http://security/api/oauth/callback")
    )
    private val credentials = Credentials("user", "password")

    private val oauthServer = SimpleOAuthServer(credentials, oAuthClientData)

    private val securityServer =
        SecuritySystem(
            clock,
            events,
            reverseProxy(
                "oauth" to oauthServer,
                "userDirectory" to userDirectory,
                "entryLogger" to entryLogger
            ),
            Uri.of("http://security"),
            Uri.of("http://oauth"),
            Uri.of("http://userDirectory"),
            Uri.of("http://entryLogger")
        )

    // this HttpHandler handles switching of hosts from the security server and the oauth server
    // (due to redirects happening during the OAuth flow)
    val http: HttpHandler = {
        if (it.uri.authority == "oauth") oauthServer(it) else securityServer(it)
    }

    val browser = Http4kWebDriver(http)
}

private val username = Query.optional("username")
private val authorization = Header.map({ AccessToken(it.removePrefix("Bearer ")) }, { "Bearer ${it.value}" }
).optional("Authorization")

fun TestEnvironment.enterBuilding(user: String?, token: AccessToken?): Response =
    http(Request(POST, "/api/knock").with(username of user, authorization of token))

fun TestEnvironment.exitBuilding(user: String?, token: AccessToken?): Response =
    http(Request(POST, "/api/bye").with(username of user, authorization of token))

fun TestEnvironment.checkInhabitants(token: AccessToken?): Response =
    http(Request(GET, "/api/whoIsThere").with(authorization of token))

fun Http4kWebDriver.logIn() = apply {
    get(Uri.of("/users"))
    findElement(By.id("loginForm"))?.apply { submit() }
}
