package env

import org.http4k.contract.Root
import org.http4k.contract.RouteModule
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

    val app = RouteModule(Root)
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
        .toHttpHandler()
}
