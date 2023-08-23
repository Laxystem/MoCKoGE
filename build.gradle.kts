allprojects {
    group = "quest.laxla.mockoge"

    repositories {
        mavenCentral()
        maven("Sonatype Snapshots") { // TODO: remove this
            url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        }
    }
}
