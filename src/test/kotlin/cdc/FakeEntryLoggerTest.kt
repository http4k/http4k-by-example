package cdc

import org.http4k.client.OkHttp

class FakeEntryLoggerTest : EntryLoggerContract(OkHttp())
