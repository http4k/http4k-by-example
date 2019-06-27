package cdc.userdirectory

import env.userdirectory.FakeUserDirectory
import verysecuresystems.EmailAddress
import verysecuresystems.Username

class FakeUserDirectoryTest : UserDirectoryContract {
    override val http = FakeUserDirectory()
    override val username = Username("ElonMusk")
    override val email = EmailAddress("elon@tesla.com")
}
