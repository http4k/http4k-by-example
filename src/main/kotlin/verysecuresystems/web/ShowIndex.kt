package verysecuresystems.web

import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.routing.bind
import org.http4k.template.TemplateRenderer
import org.http4k.template.ViewModel
import java.time.LocalDateTime

data class Index(val time: String, val browser: String) : ViewModel

object ShowIndex {
    operator fun invoke(renderer: TemplateRenderer) =
        "/" bind GET to SetHtmlContentType.then {
            Response(OK).body(
                renderer(
                    Index(LocalDateTime.now().toString(), it.header("User-Agent") ?: "unknown"))
            )
        }
}