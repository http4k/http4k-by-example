package unit

import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.testing.Approver
import org.http4k.testing.JsonApprovalTest
import org.http4k.testing.assertApproved
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import verysecuresystems.UserEntry
import verysecuresystems.api.ByeBye

@ExtendWith(JsonApprovalTest::class)
class ByeByeTest {

    private val entry = UserEntry("bob", false, 0)

    @Test
    fun `user is removed`(approver: Approver) {
        val app = ByeBye({ true }, { entry })
        approver.assertApproved(app(Request(POST, "/bye").query("username", "bob")), ACCEPTED)
    }

    @Test
    fun `user not found`(approver: Approver) {
        val app = ByeBye({ false }, { entry })
        approver.assertApproved(app(Request(POST, "/bye").query("username", "bob")), NOT_FOUND)
    }
}