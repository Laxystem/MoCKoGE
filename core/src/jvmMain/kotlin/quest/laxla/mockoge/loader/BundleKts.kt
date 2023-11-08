@file:Suppress(BetaMultiplatformClassesAndObjects)

package quest.laxla.mockoge.loader

import quest.laxla.mockoge.util.BetaMultiplatformClassesAndObjects
import quest.laxla.mockoge.util.BundleFileExtension
import kotlin.script.experimental.annotations.KotlinScript

@KotlinScript(
    displayName = "MoCKoGE Bundle",
    fileExtension = BundleFileExtension,
    compilationConfiguration = CompilationConfiguration::class
)
public abstract class BundleKts : BundleScript()


