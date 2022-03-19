package functional

import env.TestEnvironment
import env.logIn
import org.http4k.core.Response
import org.http4k.core.Status.Companion.I_M_A_TEAPOT
import org.http4k.core.Status.Companion.OK
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

    @Test
    fun `manage users requires login via oauth`(approver: Approver) {
        approver.assertApproved(env.browser {
            logIn()
        })
    }

    @Test
    fun `add user`(approver: Approver) {
        approver.assertApproved(env.browser {
            logIn()
            addUser()
        })
    }

    @Test
    fun `add user then delete them`(approver: Approver) {
        approver.assertApproved(env.browser {
            logIn()
            addUser()
            deleteUser()
        })
    }

    private fun Http4kWebDriver.addUser() {
        findElement(By.id("username"))?.sendKeys("bob")
        findElement(By.id("email"))?.sendKeys("email@email")
        findElement(By.id("createUserForm"))?.apply { submit() }
    }

    private fun Http4kWebDriver.deleteUser() {
        findElement(By.id("deleteUserForm"))?.apply { submit() }
    }
}

private operator fun Http4kWebDriver.invoke(fn: Http4kWebDriver.() -> Unit): Http4kWebDriver = apply(fn)

private fun Approver.assertApproved(browser: Http4kWebDriver) {
    assertApproved(Response(browser.status ?: I_M_A_TEAPOT).body(browser.pageSource ?: ""), OK)
}
