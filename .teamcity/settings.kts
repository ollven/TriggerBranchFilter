import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.PullRequests
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildFeatures.pullRequests
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.triggers.finishBuildTrigger
import jetbrains.buildServer.configs.kotlin.triggers.schedule
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2024.07"

project {

    vcsRoot(HttpsGithubComOllvenBranchFiltersRefsHeadsMain)

    buildType(ScheduleTrigger)
    buildType(FinishTrigger)
    buildType(VCSTriggerPlywithPRfeatureDettings)
    buildType(VCSTrigger)
}

object FinishTrigger : BuildType({
    name = "FinishTrigger"

    vcs {
        root(HttpsGithubComOllvenBranchFiltersRefsHeadsMain)
    }

    steps {
        maven {
            id = "Maven2"
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
        }
    }

    triggers {
        finishBuildTrigger {
            buildType = "${VCSTrigger.id}"
            branchFilter = """
                +:<default>
                +pr:target=to_main
                +pr:source=newsource
            """.trimIndent()
        }
    }

    features {
        perfmon {
        }
        pullRequests {
            vcsRootExtId = "${HttpsGithubComOllvenBranchFiltersRefsHeadsMain.id}"
            provider = github {
                authType = vcsRoot()
                filterAuthorRole = PullRequests.GitHubRoleFilter.MEMBER
            }
        }
    }
})

object ScheduleTrigger : BuildType({
    name = "ScheduleTrigger"

    vcs {
        root(HttpsGithubComOllvenBranchFiltersRefsHeadsMain)
    }

    steps {
        maven {
            id = "Maven2"
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
        }
    }

    triggers {
        vcs {
            enabled = false
            branchFilter = "+:pr*"
        }
        schedule {
            schedulingPolicy = cron {
                minutes = "0/2"
            }
            branchFilter = """
                +:<default>
                +pr:target=to_main
                +pr:source=newsource
            """.trimIndent()
            triggerBuild = always()
        }
    }

    features {
        perfmon {
        }
        pullRequests {
            vcsRootExtId = "${HttpsGithubComOllvenBranchFiltersRefsHeadsMain.id}"
            provider = github {
                authType = vcsRoot()
                filterAuthorRole = PullRequests.GitHubRoleFilter.MEMBER
            }
        }
    }
})

object VCSTrigger : BuildType({
    name = "VCSTrigger"

    vcs {
        root(HttpsGithubComOllvenBranchFiltersRefsHeadsMain)
    }

    steps {
        maven {
            id = "Maven2"
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
        }
    }

    triggers {
        vcs {
            branchFilter = """
                +:<default>
                +pr:target=to_main
                +pr:source=newsource
            """.trimIndent()
        }
    }

    features {
        perfmon {
        }
        pullRequests {
            vcsRootExtId = "${HttpsGithubComOllvenBranchFiltersRefsHeadsMain.id}"
            provider = github {
                authType = vcsRoot()
                filterAuthorRole = PullRequests.GitHubRoleFilter.MEMBER
            }
        }
    }
})

object VCSTriggerPlywithPRfeatureDettings : BuildType({
    name = "VCSTriggerPlywithPRfeatureDettings"

    vcs {
        root(HttpsGithubComOllvenBranchFiltersRefsHeadsMain)
    }

    steps {
        maven {
            id = "Maven2"
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
        }
    }

    triggers {
        vcs {
        }
    }

    features {
        perfmon {
        }
        pullRequests {
            vcsRootExtId = "${HttpsGithubComOllvenBranchFiltersRefsHeadsMain.id}"
            provider = github {
                authType = vcsRoot()
                filterTargetBranch = "+:newsource"
                filterAuthorRole = PullRequests.GitHubRoleFilter.MEMBER
            }
        }
    }
})

object HttpsGithubComOllvenBranchFiltersRefsHeadsMain : GitVcsRoot({
    name = "https://github.com/ollven/BranchFilters#refs/heads/main"
    url = "https://github.com/ollven/BranchFilters"
    branch = "refs/heads/main"
    branchSpec = "refs/heads/*"
    authMethod = token {
        userName = "oauth2"
        tokenId = "tc_token_id:CID_3c80ea31643e58c3cc3088385773629c:-1:cdefa0c8-2905-4db4-80ce-b9292f64e995"
    }
})
