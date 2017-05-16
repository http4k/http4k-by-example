package org.http4k.todo

import org.http4k.contract.Root
import org.http4k.contract.RouteModule
import org.http4k.core.then
import org.http4k.filter.CorsPolicy.Companion.UnsafeGlobalPermissive
import org.http4k.filter.DebuggingFilters
import org.http4k.filter.ServerFilters
import org.http4k.server.Jetty
import org.http4k.server.asServer

fun main(args: Array<String>) {
    val port = if (args.isNotEmpty()) args[0] else "5000"

    val globalFilters = DebuggingFilters.PrintRequestAndResponse().then(ServerFilters.Cors(UnsafeGlobalPermissive))

    globalFilters.then(
        RouteModule(Root)
             .toHttpHandler())
        .asServer(Jetty(port.toInt())).start().block()
}

