package verysecuresystems.web

import org.http4k.contract.BasePath
import org.http4k.contract.Module
import org.http4k.contract.Root
import org.http4k.contract.RouteModule
import org.http4k.contract.StaticModule
import org.http4k.core.ResourceLoader
import org.http4k.template.HandlebarsTemplates
import verysecuresystems.external.UserDirectory

object Web {
    fun module(path: BasePath, userDirectory: UserDirectory): Module {

        val templates = HandlebarsTemplates().CachingClasspath("templates")
        val webModule = RouteModule(path)
//            .withRoutes(ManageUsers.routes(userDirectory))
            .withRoutes(ManageUsers.routes(templates, userDirectory))
            .withRoute(ShowIndex.route(templates))

        return StaticModule(Root, ResourceLoader.Classpath("public")).then(webModule)
    }
}