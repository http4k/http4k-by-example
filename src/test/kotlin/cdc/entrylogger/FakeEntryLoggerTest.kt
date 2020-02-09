package cdc.entrylogger

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.throws
import env.entrylogger.FakeEntryLogger
import org.http4k.cloudnative.RemoteRequestFailed
import org.junit.jupiter.api.Test
import verysecuresystems.Username

class FakeEntryLoggerTest : EntryLoggerContract {
    override val http = FakeEntryLogger()

    // test a behaviour here which is not supported by the "real" server
    @Test
    fun `username lookup blows up`() {
        http.blowsUp()
        assertThat({ entryLogger().enter(Username("bob")) }, throws<RemoteRequestFailed>())
    }
}
