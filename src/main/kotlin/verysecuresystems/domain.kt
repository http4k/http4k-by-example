package verysecuresystems

data class Event(val description: String)

data class Id(val value: Int)

data class Username(val value: String)

data class EmailAddress(val value: String)

data class User(val id: Id, val name: Username, val email: EmailAddress)

data class UserEntry(val username: String, val goingIn: Boolean, val timestamp: Long)

data class Message(val message: String)
