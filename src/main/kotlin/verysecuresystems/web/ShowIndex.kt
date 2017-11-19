package verysecuresystems.web

import org.http4k.contract.ContractRoute
import org.http4k.contract.bindContract
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.then
import org.http4k.template.TemplateRenderer
import org.http4k.template.ViewModel
import java.time.LocalDateTime

data class Index(val time: String, val browser: String) : ViewModel

object ShowIndex {
    fun route(renderer: TemplateRenderer): ContractRoute =
        "/" bindContract GET to SetHtmlContentType.then {
            Response(Status.OK).body(renderer(Index(LocalDateTime.now().toString(), it.header("User-Agent") ?: "unknown")))
        }
}