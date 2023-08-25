plugins {
    id("quest.laxla.mockoge.internal.bundler")
}

val collections: String by project
val colormath: String by project
val coroutines: String by project
val datetime: String by project
val klogging: String by project
val okio: String by project
val semver: String by project
val serialization: String by project

bundler {
    dependencies {
        common {
            api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")
            api("org.jetbrains.kotlinx:kotlinx-serialization-json-okio:$serialization")
            api("org.jetbrains.kotlinx:kotlinx-collections-immutable:$collections")
            api("org.jetbrains.kotlinx:kotlinx-datetime:$datetime")
            api("com.github.ajalt.colormath:colormath:$colormath")
            api("io.github.oshai:kotlin-logging:$klogging")
            api("io.github.z4kn4fein:semver:$semver")
            api("com.squareup.okio:okio:$okio")
        }

        tests {
            api("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines")
        }
    }
}
