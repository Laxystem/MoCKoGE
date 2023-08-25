plugins {
    id("quest.laxla.mockoge.internal.bundler")
}

bundler {
    dependencies {
        common {
            api(project(":core:common"))
        }
    }
}
