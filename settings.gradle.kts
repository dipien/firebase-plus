plugins {
    id("com.gradle.enterprise").version("3.7")
}

if (System.getenv("CI") == "true") {
    buildCache {
        local {
            directory = File(System.getProperty("user.home"), "/gradle-build-cache")
        }
    }
}

include(":remote-config")
include(":android-firebase-remote-config-plus")
