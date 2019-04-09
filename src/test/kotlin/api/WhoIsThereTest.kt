package api

import org.http4k.contract.contract
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.http4k.testing.Approver
import org.http4k.testing.JsonApprovalTest
import org.http4k.testing.assertApproved
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import verysecuresystems.EmailAddress
import verysecuresystems.Id
import verysecuresystems.User
import verysecuresystems.Username
import verysecuresystems.api.WhoIsThere

@ExtendWith(JsonApprovalTest::class)
class WhoIsThereTest {

    private val user = User(Id(1), Username("Bob"), EmailAddress("bob@bob.com"))

    @Test
    fun `can list inhabitants`(approver: Approver) {
        val contract = contract {
            routes += WhoIsThere(listOf(user.name)) { user }
        }

        approver.assertApproved(contract(Request(GET, "/whoIsThere")), OK)
    }
}