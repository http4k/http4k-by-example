package functional

import env.TestEnvironment
import org.http4k.core.Response
import org.http4k.core.Status.Companion.I_M_A_TEAPOT
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Uri
import org.http4k.testing.ApprovalTest
import org.http4k.testing.Approver
import org.http4k.testing.assertApproved
import org.http4k.webdriver.Http4kWebDriver
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApprovalTest::class)
class WebTest {
    private val env = TestEnvironment()

    @Test
    fun homepage(approver: Approver) {
        approver.assertApproved(env.browser.apply { get(Uri.of("/")) })
    }

    @Test
    fun `serves static content`(approver: Approver) {
        approver.assertApproved(env.browser.apply { get(Uri.of("/style.css")) })
    }
}

private fun Approver.assertApproved(browser: Http4kWebDriver) {
    assertApproved(Response(browser.status ?: I_M_A_TEAPOT).body(browser.pageSource ?: ""), OK)
}
