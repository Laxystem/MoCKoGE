plugins {
    quest.laxla.mockoge.bundler
}

val okio: String by project

bundler {
    kotlin.sourceSets {
        common {
            dependencies {
                api("com.squareup.okio:okio:$okio")
            }
        }
    }
}