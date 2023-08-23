rootProject.name = "mockoge"
include("core:common", "core:server", "core:client")

// For dependencies
includeBuild("gradle")

pluginManagement {
    // For plugins
    includeBuild("gradle")
}
