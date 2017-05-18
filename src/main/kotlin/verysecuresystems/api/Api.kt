package verysecuresystems.api

import org.http4k.contract.BasePath
import org.http4k.contract.Module
import org.http4k.contract.RouteModule
import verysecuresystems.external.EntryLogger
import verysecuresystems.external.UserDirectory

object Api {
    fun module(path: BasePath, userDirectory: UserDirectory, entryLogger: EntryLogger): Module = RouteModule(path)
}