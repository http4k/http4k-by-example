package verysecuresystems

class Inhabitants : Iterable<Username> {
    private var currentUsers: List<Username> = emptyList()

    fun add(user: Username): Boolean =
        if (currentUsers.contains(user)) false
        else {
            currentUsers = currentUsers.plus(user)
            true
        }

    fun remove(user: Username): Boolean =
        if (currentUsers.contains(user)) {
            currentUsers = currentUsers.filterNot { it == user }
            true
        } else false

    override fun iterator(): Iterator<Username> = currentUsers.iterator()
}