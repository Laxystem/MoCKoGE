plugins {
    quest.laxla.mockoge.bundler
}

bundler {
    isInternal = true

    kotlin.sourceSets {
        common {
            dependencies {
                api(project(":core:common"))
            }
        }
    }
}
