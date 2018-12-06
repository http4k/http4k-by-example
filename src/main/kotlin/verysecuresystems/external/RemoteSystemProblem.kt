package verysecuresystems.external

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.Status.Companion.BAD_GATEWAY
import org.http4k.lens.BodyLens
import org.http4k.lens.LensFailure

fun <T> HttpHandler.perform(request: Request, responseLens: BodyLens<T>): T = this(request)
    .let {
        if (it.status.code > 399) throw RemoteSystemProblem(request.uri.toString(), it.status)

        try {
            responseLens(it)
        } catch(e: LensFailure) {
            throw RemoteSystemProblem(request.uri.toString(), BAD_GATEWAY)
        }
    }

class RemoteSystemProblem(name: String, val status: Status) : Exception("$name returned $status")
