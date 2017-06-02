package env

import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.with
import org.http4k.routing.contract
import verysecuresystems.UserEntry
import verysecuresystems.external.EntryLogger.Companion.Entry
import verysecuresystems.external.EntryLogger.Companion.Entry.body
import verysecuresystems.external.EntryLogger.Companion.Exit
import verysecuresystems.external.EntryLogger.Companion.LogList

class FakeEntryLogger {

    val entries = mutableListOf<UserEntry>()

    val app = contract()
        .withRoute(
            Entry.route bind {
                val userEntry = body(it)
                entries.add(userEntry)
                Response(CREATED).with(Entry.response of userEntry)
            }
        )
        .withRoute(Exit.route bind {
            val userEntry = Entry.body(it)
            entries.add(userEntry)
            Response(ACCEPTED).with(Entry.response of userEntry)
        })
        .withRoute(
            LogList.route bind {
                Response(Status.OK).with(LogList.response of entries)
            })
}
