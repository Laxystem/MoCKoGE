plugins {
    id("quest.laxla.mockoge.bundler")
}

val collections: String by project
val colormath: String by project
val coroutines: String by project
val datetime: String by project
val klogging: String by project
val logback: String by project
val okio: String by project
val semver: String by project
val serialization: String by project
val slf4j: String by project

bundler {
    MoCKoGE()

    target {
        alpha()
        linuxX64()
    }

    dependencies {
        common {
            api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")
            api("org.jetbrains.kotlinx:kotlinx-serialization-json-okio:$serialization")
            api("org.jetbrains.kotlinx:kotlinx-collections-immutable:$collections")
            api("org.jetbrains.kotlinx:kotlinx-datetime:$datetime")
            api("com.github.ajalt.colormath:colormath:$colormath")
            api("io.github.oshai:kotlin-logging:$klogging")
            api("io.github.z4kn4fein:semver:$semver")
            api("com.squareup.okio:okio:$okio") // TODO: Use KotlinX IO
            implementation("org.jetbrains.kotlin:kotlin-scripting-common")
        }

        test {
            api("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines")
        }

        jvm {
            implementation("org.jetbrains.kotlin:kotlin-scripting-jvm")
            implementation("org.jetbrains.kotlin:kotlin-scripting-jvm-host")
            runtimeOnly("org.slf4j:slf4j-api:$slf4j")
            runtimeOnly("ch.qos.logback:logback-classic:$logback")
        }
    }
}
