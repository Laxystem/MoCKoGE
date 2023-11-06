@file:Suppress(Deprecation, BetaMultiplatformClassesAndObjects)

package quest.laxla.mockoge.loader.internal

import quest.laxla.mockoge.util.BetaMultiplatformClassesAndObjects
import quest.laxla.mockoge.util.Deprecation
import quest.laxla.mockoge.util.NoActualForExpect
import kotlin.reflect.KClass

private const val DeprecationMessage = "Not a part of published API. Public for technical reasons."

// TODO: get rid of whatever this is

/**
 * Only accessible when compiled to JVM.
 *
 * Do not try to access any of the classes in this file outside the JVM.
 */
@Deprecated(DeprecationMessage)
@Suppress(NoActualForExpect)
public expect annotation class KotlinScript(
    val displayName: String = "",
    val fileExtension: String = "kts",
    val filePathPattern: String = "",
    val compilationConfiguration: KClass<out KtsCompileConfig> = KtsCompileConfigDefault::class,
    val evaluationConfiguration: KClass<out KtsEvalConfig> = KtsEvalConfigDefault::class,
    val hostConfiguration: KClass<out KtsHostConfig> = KtsHostConfig::class
)

@Deprecated(DeprecationMessage)
@Suppress(NoActualForExpect)
public expect open class KtsCompileConfig
@Deprecated(DeprecationMessage)
@Suppress(NoActualForExpect)
public expect object KtsCompileConfigDefault : KtsCompileConfig

@Deprecated(DeprecationMessage)
@Suppress(NoActualForExpect)
public expect open class KtsEvalConfig
@Deprecated(DeprecationMessage)
@Suppress(NoActualForExpect)
public expect object KtsEvalConfigDefault : KtsEvalConfig

@Deprecated(DeprecationMessage)
@Suppress(NoActualForExpect)
public expect open class KtsHostConfig
