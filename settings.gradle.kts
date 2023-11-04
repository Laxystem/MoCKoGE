rootProject.name = "mockoge"
include("core", "graphics", "graphics:renderer", "graphics:windower")

// For dependencies
includeBuild("gradle")

pluginManagement {
    // For plugins
    includeBuild("gradle")
}
