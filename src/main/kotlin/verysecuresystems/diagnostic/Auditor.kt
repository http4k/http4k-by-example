package verysecuresystems.diagnostic

import org.http4k.core.Events
import org.http4k.core.Filter
import verysecuresystems.IncomingEvent
import verysecuresystems.OutgoingEvent
import java.time.Clock

/**
 * This auditor is responsible for logging the performance of inbound calls to the system.
 */
object Auditor {
    fun Incoming(clock: Clock, events: Events) = Filter { next ->
        {
            next(it).apply { events(IncomingEvent(clock.instant(), it.uri, status)) }
        }
    }

    fun Outgoing(clock: Clock, events: Events) = Filter { next ->
        {
            next(it).apply { events(OutgoingEvent(clock.instant(), it.uri, status)) }
        }
    }
}