pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        jcenter()
        maven (url = uri("https://jitpack.io") )

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven (url = uri("https://jitpack.io") )
        maven {
            url = uri("https://phonepe.mycloudrepo.io/public/repositories/phonepe-intentsdk-android")
        }
        maven {
            url = uri("https://artifactory.paytm.in/libs-release-local")
        }
    }
}

rootProject.name = "FreshVeggieMart"
include(":app")
 