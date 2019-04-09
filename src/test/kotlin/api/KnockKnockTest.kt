package api

import org.http4k.contract.contract
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

    private val user = User(Id(1), Username("Bob"), EmailAddress("bob@bob.com"))

    @Test
    fun `finds existing user and lets them in`(approver: Approver) {
        val contract = contract {
            routes += KnockKnock({ user }, { true }, { UserEntry("Bob", true, 0) })
        }

        approver.assertApproved(contract(Request(POST, "/knock").query("username", "bob")), ACCEPTED)
    }

    @Test
    fun `user does not exist`(approver: Approver) {
        val contract = contract {
            routes += KnockKnock({ null }, { true }, { UserEntry("Bob", true, 0) })
        }

        approver.assertApproved(contract(Request(POST, "/knock").query("username", "bob")), NOT_FOUND)
    }

    @Test
    fun `user cannot enter`(approver: Approver) {
        val contract = contract {
            routes += KnockKnock({ user }, { false }, { UserEntry("Bob", true, 0) })
        }

        approver.assertApproved(contract(Request(POST, "/knock").query("username", "bob")), CONFLICT)
    }
}