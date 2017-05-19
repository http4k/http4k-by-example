package cdc

import org.http4k.client.OkHttp
import org.junit.Ignore

/**
 * Contract implementation for the real user directory service. Extra steps might be required here to setup/teardown
 * test data.
 */
@Ignore // this would not be ignored in reality
class RealEntryLoggerTest : EntryLoggerContract(OkHttp())