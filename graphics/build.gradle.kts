plugins {
    id("quest.laxla.mockoge.bundler")
}

bundler {
    presets.MoCKoGE()

    target {
        jvm()
    }

    dependencies {
        common {
            api(project(":core"))
        }
    }
}