package unit

import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.CONFLICT
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.testing.Approver
import org.http4k.testing.JsonApprovalTest
import org.http4k.testing.assertApproved
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import verysecuresystems.EmailAddress
import verysecuresystems.Id
import verysecuresystems.User
import verysecuresystems.UserEntry
import verysecuresystems.Username
import verysecuresystems.api.KnockKnock

@ExtendWith(JsonApprovalTest::class)
class KnockKnockTest {

    private val user = User(Id(1), Username("bob"), EmailAddress("a@b"))
    private val entry = UserEntry("bob", true, 0)

    @Test
    fun `user is accepted`(approver: Approver) {
        val app = KnockKnock({ user }, { true }, { entry })
        approver.assertApproved(app(Request(POST, "/knock").query("username", "bob")), ACCEPTED)
    }

    @Test
    fun `user not found`(approver: Approver) {
        val app = KnockKnock({ null }, { true }, { entry })
        approver.assertApproved(app(Request(POST, "/knock").query("username", "bob")), NOT_FOUND)
    }

    @Test
    fun `user is already in building`(approver: Approver) {
        val app = KnockKnock({ user }, { false }, { entry })
        approver.assertApproved(app(Request(POST, "/knock").query("username", "bob")), CONFLICT)
    }
}