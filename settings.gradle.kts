rootProject.name = "mockoge"
include("core", "graphics")

// For dependencies
includeBuild("gradle")

pluginManagement {
    // For plugins
    includeBuild("gradle")
}
