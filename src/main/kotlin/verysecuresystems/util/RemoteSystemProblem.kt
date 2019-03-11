package verysecuresystems.util

import org.http4k.core.Status

class RemoteSystemProblem(name: String, val status: Status) : Exception("$name returned $status")
