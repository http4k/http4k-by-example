package verysecuresystems.web

import org.http4k.contract.contract
import org.http4k.core.ContentType.Companion.TEXT_HTML
import org.http4k.core.Filter
import org.http4k.core.with
import org.http4k.lens.Header.CONTENT_TYPE
import org.http4k.routing.RoutingHttpHandler
import org.http4k.template.HandlebarsTemplates
import verysecuresystems.external.UserDirectory

val SetHtmlContentType = Filter { next ->
    { next(it).with(CONTENT_TYPE of TEXT_HTML) }
}

object Web {
    fun router(userDirectory: UserDirectory): RoutingHttpHandler {
        val templates = HandlebarsTemplates().CachingClasspath()
        return contract {
            routes += ManageUsers.routes(templates, userDirectory).plus(ShowIndex.route(templates))
        }
    }
}
