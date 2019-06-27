package cdc.entrylogger

import org.http4k.client.OkHttp
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters
import org.junit.jupiter.api.Disabled

/**
 * Contract implementation for the real user directory service. Extra steps might be required here to setup/teardown
 * test data.
 */
@Disabled // this would not be ignored in reality
class RealEntryLoggerTest : EntryLoggerContract {
    override val http = ClientFilters.SetHostFrom(Uri.of("http://entrylogger.com")).then(OkHttp())
}