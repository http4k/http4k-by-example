package cdc

import env.FakeUserDirectory
import verysecuresystems.EmailAddress
import verysecuresystems.Username

class FakeUserDirectoryTest : UserDirectoryContract(FakeUserDirectory().app) {
    override val username = Username("ElonMusk")
    override val email = EmailAddress("elon@tesla.com")
}
