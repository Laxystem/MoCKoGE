package quest.laxla.mockoge.gradle

import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo

private const val Package = "quest.laxla.mockoge"
private const val CorePackage = "$Package.core"
private const val AnnotationName = "Bundleable"
private const val Annotation = "$Package.$AnnotationName"
private const val SuperClassName = "BundleScript"
private const val SuperClass = "$Package.$SuperClassName"
private const val CoreGenerationPackage = "$CorePackage.generated"
private const val CoreGenerationFileName = "Core"
private const val ImmutableCollections = "kotlinx.collections.immutable"
private const val ImmutableList = "ImmutableList"
private const val ListBuilder = "persistentListOf"

/**
 * Creates a list of all objects annotated with [Annotation] and extending [SuperClass] inside [CoreGenerationPackage].
 */
class BundlerSymbolProcessor(
    val codeGenerator: CodeGenerator,
    val logger: KSPLogger,
    val config: Map<String, String>
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(Annotation)
            .filterIsInstance<KSClassDeclaration>()
            .filterNot { it.isActual } // we use the `expect` version.
            .filter { it.classKind == ClassKind.OBJECT }
            .filter { it.isPublic() }
            .toList()


        if (symbols.isEmpty()) return emptyList()

        file(CoreGenerationPackage, CoreGenerationFileName) {
            property(
                "Bundles",
                ImmutableCollections.type(ImmutableList).parameterizedBy(CorePackage.type(SuperClassName)),
                KModifier.PUBLIC
            ) {
                initializer {
                    add("%M(", ImmutableCollections.member(ListBuilder))

                    symbols.forEachIndexed { index, type ->
                        val code = if (index == symbols.lastIndex) "%T" else "%T, "

                        add(code, type.asStarProjectedType().toTypeName())
                    }

                    add(")")
                }
            }
        }.writeTo(codeGenerator, aggregating = false, symbols.mapNotNull { it.containingFile })

        return symbols.filterNot { it.validate() }
    }
}
