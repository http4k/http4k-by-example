package env

import org.http4k.contract.bind
import org.http4k.contract.contract
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.with
import verysecuresystems.UserEntry
import verysecuresystems.external.EntryLogger
import verysecuresystems.external.EntryLogger.Companion.Entry
import verysecuresystems.external.EntryLogger.Companion.Entry.body
import verysecuresystems.external.EntryLogger.Companion.LogList

class FakeEntryLogger {

    val entries = mutableListOf<UserEntry>()

    val app = contract(
        Entry.route bind {
            req: Request ->
            val userEntry = body(req)
            entries.add(userEntry)
            Response(CREATED).with(Entry.response of userEntry)
        }
        ,
        EntryLogger.Companion.Exit.route bind
            {
                req: Request ->
                val userEntry = Entry.body(req)
                entries.add(userEntry)
                Response(ACCEPTED).with(Entry.response of userEntry)
            },

        LogList.route bind
            {
                Response(Status.OK).with(LogList.response of entries)
            })
}
