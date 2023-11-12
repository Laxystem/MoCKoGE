plugins {
    id("quest.laxla.mockoge.bundler")
}

bundler {
    MoCKoGE()

    target {
        jvm()
    }

    dependencies {
        common {
            api(project(":core"))
        }
    }
}