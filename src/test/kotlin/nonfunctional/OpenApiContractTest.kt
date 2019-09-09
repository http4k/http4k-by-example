package nonfunctional

import env.TestEnvironment
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.http4k.testing.Approver
import org.http4k.testing.JsonApprovalTest
import org.http4k.testing.assertApproved
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(JsonApprovalTest::class)
class OpenApiContractTest {
    private val env = TestEnvironment()

    @Test
    fun `provides API documentation in open api format`(approver: Approver) {
        approver.assertApproved(env.http(Request(Method.GET, "/api/api-docs")), OK)
    }
}