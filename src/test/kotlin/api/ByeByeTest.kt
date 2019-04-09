package api

import org.http4k.contract.contract
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

    @Test
    fun `user can exit`(approver: Approver) {
        val contract = contract {
            routes += ByeBye({ true }, { UserEntry("Bob", false, 0) })
        }

        approver.assertApproved(contract(Request(POST, "/bye").query("username", "bob")), ACCEPTED)
    }

    @Test
    fun `user not in building`(approver: Approver) {
        val contract = contract {
            routes += ByeBye({ false }, { UserEntry("Bob", false, 0) })
        }

        approver.assertApproved(contract(Request(POST, "/bye").query("username", "bob")), NOT_FOUND)
    }
}