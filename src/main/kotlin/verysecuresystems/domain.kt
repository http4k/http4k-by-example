package verysecuresystems

import org.http4k.core.Status
import org.http4k.core.Uri
import org.http4k.events.Event
import org.http4k.events.EventCategory

data class IncomingEvent(val uri: Uri, val status: Status) : Event {
    val category = EventCategory("incoming")
}

data class OutgoingEvent(val uri: Uri, val status: Status) : Event {
    val category = EventCategory("outgoing")
}

data class Id(val value: Int)

data class Username(val value: String)

data class EmailAddress(val value: String)

data class User(val id: Id, val name: Username, val email: EmailAddress)

data class UserEntry(val username: String, val goingIn: Boolean, val timestamp: Long)

data class Message(val message: String)
