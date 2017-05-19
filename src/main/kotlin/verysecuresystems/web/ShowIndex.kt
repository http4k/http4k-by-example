package verysecuresystems.web

import org.http4k.contract.Route
import org.http4k.contract.ServerRoute
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.template.TemplateRenderer
import org.http4k.template.ViewModel
import java.time.LocalDateTime

data class Index(val time: String, val browser: String) : ViewModel

object ShowIndex {
    fun route(renderer: TemplateRenderer): ServerRoute =
        Route().at(Method.GET) bind
            {
                Response(Status.OK).body(renderer(Index(LocalDateTime.now().toString(), it.header("User-Agent") ?: "unknown")))
            }
}