plugins {
    id("quest.laxla.mockoge.bundler")
}

bundler {
    kotlin.sourceSets {
        common {
            dependencies {
                api(project(":core:server"))
            }
        }
    }
}
