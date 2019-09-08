package verysecuresystems.web

import org.http4k.core.ContentType.Companion.TEXT_HTML
import org.http4k.core.Filter
import org.http4k.core.then
import org.http4k.core.with
import org.http4k.lens.Header.CONTENT_TYPE
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.security.OAuthProvider
import org.http4k.template.HandlebarsTemplates
import verysecuresystems.external.UserDirectory
import java.time.Clock

val SetHtmlContentType = Filter { next ->
    { next(it).with(CONTENT_TYPE of TEXT_HTML) }
}

/**
 * Defines the web content layer of the app, including the OAuth-protected
 * user management UI.
 */
fun Web(clock: Clock, oAuthProvider: OAuthProvider, userDirectory: UserDirectory): RoutingHttpHandler {
    val templates = HandlebarsTemplates().CachingClasspath()

    return ShowError(templates)
        .then(
            routes(
                oAuthProvider.authFilter.then(
                    "/users" bind routes(
                        DeleteUser(userDirectory),
                        CreateUser(templates, userDirectory),
                        ListUsers(templates, userDirectory)
                    )
                ),
                ShowIndex(clock, templates)
            )
        )
}