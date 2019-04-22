package verysecuresystems

import org.http4k.cloudnative.env.Environment

/**
 * Main entry point. Note that this will not run without setting up the correct environmental variables
 * - see RunnableEnvironment for a demo-able version of the server.
 */
fun main() {
    SecuritySystemServer(Environment.ENV).start()
}