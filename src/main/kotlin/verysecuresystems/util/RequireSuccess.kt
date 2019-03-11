package verysecuresystems.util

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Status

object RequireSuccess : Filter {
    override fun invoke(next: HttpHandler): HttpHandler = {
        next(it).apply {
            if (status.serverError && status != Status.NOT_FOUND) throw RemoteSystemProblem(it.uri.toString(), status)
        }
    }
}