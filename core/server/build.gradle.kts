plugins {
    id("quest.laxla.mockoge.internal.bundler")
}

bundler {
    kotlin.sourceSets {
        common {
            dependencies {
                api(project(":core:common"))
            }
        }
    }
}
