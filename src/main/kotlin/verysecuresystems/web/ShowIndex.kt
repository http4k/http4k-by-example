package verysecuresystems.web

import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.routing.bind
import org.http4k.template.TemplateRenderer
import org.http4k.template.ViewModel
import java.time.Clock
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Index(val time: String, val browser: String) : ViewModel

/**
 * The root index page of the server, displayed using a ViewModel.
 */
fun ShowIndex(clock: Clock, renderer: TemplateRenderer) =
    "/" bind GET to SetHtmlContentType.then {
        Response(OK).body(
            renderer(
                Index(LocalDateTime.now(clock).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), it.header("User-Agent")
                    ?: "unknown"))
        )
    }