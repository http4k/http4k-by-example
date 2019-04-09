package functional

import env.TestEnvironment
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.testing.ApprovalTest
import org.http4k.testing.Approver
import org.http4k.webdriver.Http4kWebDriver
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApprovalTest::class)
class WebTest {
    private val env = TestEnvironment()

    private val browser = Http4kWebDriver(env.app)

    @Test
    fun homepage(approver: Approver) {
        approver.assertApproved(env.app(Request(GET, "")))
    }

    @Test
    fun `manage users`(approver: Approver) {
        approver.assertApproved(env.app(Request(GET, "/users")))
    }

    @Test
    fun `serves static content`(approver: Approver) {
        approver.assertApproved(env.app(Request(GET, "/style.css")))
    }
}