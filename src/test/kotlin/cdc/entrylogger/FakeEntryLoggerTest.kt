package cdc.entrylogger

import env.entrylogger.FakeEntryLogger

class FakeEntryLoggerTest : EntryLoggerContract {
    override val http = FakeEntryLogger()
}
