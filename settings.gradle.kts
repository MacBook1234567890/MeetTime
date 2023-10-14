pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven{ url =uri("https://maven.google.com") }

        maven {
            url = uri("https://tokbox.bintray.com/opentok")
        }
//        maven {
//            url =uri("https://jfrog.io/artifactory/<repository-name>")
//
//        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
//        maven {
//            url=uri ("https://github.com/jitsi/jitsi-maven-repository/raw/master/releases")
//        }


    }
}

rootProject.name = "MeetTime"
include(":app")
 