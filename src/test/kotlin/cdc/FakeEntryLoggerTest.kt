package cdc

import env.FakeEntryLogger

class FakeEntryLoggerTest : EntryLoggerContract(FakeEntryLogger().app)
