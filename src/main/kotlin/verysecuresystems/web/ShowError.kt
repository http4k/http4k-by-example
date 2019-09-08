package verysecuresystems.web

import org.http4k.core.Body
import org.http4k.core.ContentType.Companion.TEXT_HTML
import org.http4k.core.Filter
import org.http4k.core.Response
import org.http4k.core.Status.Companion.SERVICE_UNAVAILABLE
import org.http4k.core.with
import org.http4k.template.TemplateRenderer
import org.http4k.template.ViewModel
import org.http4k.template.viewModel

/**
 * Catch all exceptions and shows a nice HTML page instead of a stacktrace
 */
fun ShowError(templates: TemplateRenderer) = Filter { next ->
    {
        try {
            next(it)
        } catch (e: Exception) {
            Response(SERVICE_UNAVAILABLE).with(Body.viewModel(templates, TEXT_HTML).toLens() of (Error(e)))
        }
    }
}

class Error(e: Exception) : ViewModel {
    val message = e.localizedMessage
}
