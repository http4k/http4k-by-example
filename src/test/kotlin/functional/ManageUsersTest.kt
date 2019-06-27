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
import org.openqa.selenium.By

@ExtendWith(ApprovalTest::class)
class ManageUsersTest {
    private val env = TestEnvironment()

    private val browser = Http4kWebDriver(env.http)

    @Test
    fun `manage users requires login via oauth`(approver: Approver) {
        approver.assertApproved(browser {
            logIn()
        })
    }

    @Test
    fun `add user`(approver: Approver) {
        approver.assertApproved(browser {
            logIn()
            findElement(By.id("username"))?.sendKeys("bob")
            findElement(By.id("email"))?.sendKeys("email@email")
            findElement(By.id("manageUserForm"))?.apply { submit() }
        })
    }

    private fun Http4kWebDriver.logIn() = apply {
        get(Uri.of("/users"))
        findElement(By.id("loginForm"))?.apply { submit() }
    }
}

private operator fun Http4kWebDriver.invoke(fn: Http4kWebDriver.() -> Unit): Http4kWebDriver = apply(fn)

private fun Approver.assertApproved(browser: Http4kWebDriver) {
    assertApproved(Response(browser.status ?: I_M_A_TEAPOT).body(browser.pageSource ?: ""), OK)
}
