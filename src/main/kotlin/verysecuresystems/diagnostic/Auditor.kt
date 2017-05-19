package verysecuresystems.diagnostic

import org.http4k.core.Filter
import verysecuresystems.Event
import verysecuresystems.Events
import java.time.Clock

object Auditor {
    operator fun invoke(clock: Clock, events: Events): Filter = Filter {
        next ->
        {
            val response = next(it)
            events(Event("${clock.instant()}: uri=${it.method}:${it.uri} status=${response.status.code}"))
            response
        }
    }
}