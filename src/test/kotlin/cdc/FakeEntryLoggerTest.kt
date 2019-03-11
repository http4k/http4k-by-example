package cdc

import env.FakeEntryLogger

class FakeEntryLoggerTest : EntryLoggerContract {
    override val http = FakeEntryLogger()
}
