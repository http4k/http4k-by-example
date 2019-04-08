package verysecuresystems

import org.http4k.core.Event
import org.http4k.core.EventCategory
import org.http4k.core.Status
import org.http4k.core.Uri
import java.time.Instant

data class IncomingEvent(val time: Instant, val uri: Uri, val status: Status) : Event {
    override val category = EventCategory("incoming")
}

data class OutgoingEvent(val time: Instant, val uri: Uri, val status: Status) : Event {
    override val category = EventCategory("outgoing")
}

data class Id(val value: Int)

data class Username(val value: String)

data class EmailAddress(val value: String)

data class User(val id: Id, val name: Username, val email: EmailAddress)

data class UserEntry(val username: String, val goingIn: Boolean, val timestamp: Long)

data class Message(val message: String)
