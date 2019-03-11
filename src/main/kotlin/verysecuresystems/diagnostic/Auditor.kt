package verysecuresystems.diagnostic

import org.http4k.core.Events
import org.http4k.core.Filter
import verysecuresystems.IncomingEvent
import java.time.Clock

object Auditor {
    operator fun invoke(clock: Clock, events: Events) = Filter { next ->
        {
            next(it).apply { events(IncomingEvent(clock.instant(), it.uri, status)) }
        }
    }
}