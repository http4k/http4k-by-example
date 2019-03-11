package verysecuresystems.util

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.lens.LensFailure

object ConvertDownstreamErrors : Filter {
    override fun invoke(next: HttpHandler): HttpHandler = {
        try {
            next(it)
        } catch (e: Exception) {
            when {
                e is LensFailure && e.target is Response -> Response(Status.BAD_GATEWAY)
                e is RemoteSystemProblem -> Response(Status.SERVICE_UNAVAILABLE)
                else -> throw e
            }
        }
    }
}