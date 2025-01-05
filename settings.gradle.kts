pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()

        maven ("https://s01.oss.sonatype.org/content/groups/public" )
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven ("https://jitpack.io" )
        maven ("https://s01.oss.sonatype.org/content/groups/public" )
    }
}

rootProject.name = "Campus_News"
include(":app")
 