package verysecuresystems.oauth

import org.http4k.client.OkHttp
import org.http4k.core.*
import org.http4k.format.Jackson.auto
import org.http4k.lens.*
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer

data class PullRequest(val number: Int, val state: String, val head: Head)

data class Head(val ref: String)

fun main() {
    val client = OkHttp()

    val repoOwner = "http4k"  // Replace with your repo owner
    val repoName = "http4k-by-example"    // Replace with your repo name
    val githubToken = "6da42cd1bca9bc0ac96dd0166f14516e4d6f0955" // Replace with your GitHub token

    // Fetch all open pull requests
    val openPullRequests = fetchOpenPullRequests(client, repoOwner, repoName, githubToken)

    // Close each open pull request
    openPullRequests.forEach { pr ->
        closePullRequest(client, repoOwner, repoName, pr.number, githubToken)
        deleteBranch(client, repoOwner, repoName, pr.head.ref, githubToken)
    }
}

fun deleteBranch(client: HttpHandler, owner: String, repo: String, branchName: String, token: String) {
    val request = Request(Method.DELETE, "https://api.github.com/repos/$owner/$repo/git/refs/heads/$branchName")
        .header("Authorization", "token $token")

    val response = client(request)

    if (response.status.successful) {
        println("Deleted branch '$branchName'")
    } else {
        println("Failed to delete branch '$branchName': ${response.status} - ${response.bodyString()}")
    }
}
fun fetchOpenPullRequests(client: HttpHandler, owner: String, repo: String, token: String): List<PullRequest> {
    val request = Request(Method.GET, "https://api.github.com/repos/$owner/$repo/pulls")
        .header("Authorization", "token $token")

    val response = client(request)
    val bodyLens = Body.auto<List<PullRequest>>().toLens()

    return bodyLens(response)
        .filter { it.state == "open" }
}

fun closePullRequest(client: HttpHandler, owner: String, repo: String, prNumber: Int, token: String) {
    val request = Request(Method.PATCH, "https://api.github.com/repos/$owner/$repo/pulls/$prNumber")
        .header("Authorization", "token $token")
        .body("""{"state": "closed"}""")

    val response = client(request)

    if (response.status.successful) {
        println("Closed PR #$prNumber")
    } else {
        println("Failed to close PR #$prNumber: ${response.status} - ${response.bodyString()}")
    }
}