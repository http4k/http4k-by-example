package verysecuresystems.web

import org.http4k.core.ContentType.Companion.TEXT_HTML
import org.http4k.core.Filter
import org.http4k.core.with
import org.http4k.lens.Header.CONTENT_TYPE
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.template.HandlebarsTemplates
import verysecuresystems.external.UserDirectory
import java.time.Clock

val SetHtmlContentType = Filter { next ->
    { next(it).with(CONTENT_TYPE of TEXT_HTML) }
}

/**
 * Defines the web content layer of the app.
 */
object Web {
    operator fun invoke(clock: Clock, userDirectory: UserDirectory): RoutingHttpHandler {
        val templates = HandlebarsTemplates().CachingClasspath()

        return routes(
            "/users" bind routes(
                DeleteUser(userDirectory),
                CreateUser(templates, userDirectory),
                ListUsers(templates, userDirectory)
            ),
            ShowIndex(clock, templates)
        )
    }
}
