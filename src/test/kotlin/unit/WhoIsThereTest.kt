package unit

import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.testing.Approver
import org.http4k.testing.JsonApprovalTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import verysecuresystems.EmailAddress
import verysecuresystems.Id
import verysecuresystems.User
import verysecuresystems.Username
import verysecuresystems.api.WhoIsThere

@ExtendWith(JsonApprovalTest::class)
class WhoIsThereTest {

    private val user = User(Id(1), Username("bob"), EmailAddress("a@b"))

    @Test
    fun `no users`(approver: Approver) {
        val app = WhoIsThere(emptyList()) { user }
        approver.assertApproved(app(Request(GET, "/whoIsThere")))
    }

    @Test
    fun `there are users`(approver: Approver) {
        val app = WhoIsThere(listOf(user.name)) { user }
        approver.assertApproved(app(Request(GET, "/whoIsThere")))
    }
}