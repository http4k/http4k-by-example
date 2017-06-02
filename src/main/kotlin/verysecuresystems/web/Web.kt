package verysecuresystems.web

import org.http4k.core.ContentType
import org.http4k.core.Filter
import org.http4k.core.with
import org.http4k.lens.Header
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.contract
import org.http4k.template.HandlebarsTemplates
import verysecuresystems.external.UserDirectory

val SetHtmlContentType = Filter {
    next ->
    { next(it).with(Header.Common.CONTENT_TYPE of ContentType.TEXT_HTML) }
}

object Web {
    fun router(userDirectory: UserDirectory): RoutingHttpHandler {
        val templates = HandlebarsTemplates().CachingClasspath()

        return contract()
            .withRoutes(ManageUsers.routes(templates, userDirectory))
            .withRoute(ShowIndex.route(templates))
    }
}
