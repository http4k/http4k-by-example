package env

import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.with
import verysecuresystems.UserEntry
import verysecuresystems.external.EntryLogger.Companion.Contract.Entry
import verysecuresystems.external.EntryLogger.Companion.Contract.Entry.body
import verysecuresystems.external.EntryLogger.Companion.Contract.Exit
import verysecuresystems.external.EntryLogger.Companion.Contract.LogList

class FakeEntryLogger {

    val entries = mutableListOf<UserEntry>()

    fun reset() = entries.clear()

    val routes = listOf(
        Entry.route bind {
            val userEntry = body(it)
            entries.plus(userEntry)
            Response(CREATED).with(Entry.response of userEntry)
        },
        Exit.route bind {
            val userEntry = Entry.body(it)
            entries.remove(userEntry)
            Response(ACCEPTED).with(Entry.response of userEntry)
        },
        LogList.route bind {
            Response(Status.OK).with(LogList.response of entries)
        }
    )
}
