import io.typeflows.github.workflow.GitHub
import io.typeflows.github.workflow.Job
import io.typeflows.github.workflow.RunsOn.Companion.UBUNTU_LATEST
import io.typeflows.github.workflow.Workflow
import io.typeflows.github.workflow.step.RunCommand
import io.typeflows.github.workflow.step.UseAction
import io.typeflows.github.workflow.step.marketplace.Checkout
import io.typeflows.github.workflow.step.marketplace.JavaDistribution.Temurin
import io.typeflows.github.workflow.step.marketplace.JavaVersion.V21
import io.typeflows.github.workflow.step.marketplace.SetupJava
import io.typeflows.github.workflow.trigger.Branches
import io.typeflows.github.workflow.trigger.PullRequest
import io.typeflows.github.workflow.trigger.Push
import io.typeflows.util.Builder

class BuildWorkflow : Builder<Workflow> {
    override fun build() = Workflow("build") {
        displayName = "Build"

        on += Push {
            branches = Branches.Only("master")
        }
        on += PullRequest {
            branches = Branches.Only("*")
        }

        jobs += Job("build", UBUNTU_LATEST) {
            steps += Checkout()
            steps += SetupJava(Temurin, V21)
            steps += RunCommand("./gradlew check") {
                name = "Build"
            }
            steps += UseAction("TimonVS/pr-labeler-action@v5.0.0") {
                condition = GitHub.event_name.isEqualTo("pull_request")
                name = "Tag automerge branch"
                with += mapOf("configuration-path" to ".github/pr-labeler.yml")
                env += "GITHUB_TOKEN" to $$"${{ secrets.ORG_PUBLIC_REPO_RELEASE_TRIGGERING }}"
            }
        }
    }
}
