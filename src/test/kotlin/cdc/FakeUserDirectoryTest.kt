package cdc

import org.http4k.client.OkHttp
import verysecuresystems.EmailAddress
import verysecuresystems.Username

class FakeUserDirectoryTest : UserDirectoryContract(OkHttp()) {
    override val username = Username("Elon Musk")
    override val email = EmailAddress("elon@tesla.com")
}
