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
private const val LoaderPackage = "$Package.loader"
private const val AnnotationName = "Bundleable"
private const val Annotation = "$LoaderPackage.$AnnotationName"
private const val SuperClassName = "BundleScript"
private const val SuperClass = "$LoaderPackage.$SuperClassName"
private const val GenerationPackage = "$Package.generated"
private const val GenerationFileName = "Bundles"
private const val ImmutableCollections = "kotlinx.collections.immutable"
private const val ImmutableList = "ImmutableList"
private const val ListBuilder = "persistentListOf"

/**
 * Creates a list of all objects annotated with [Annotation] and extending [SuperClass] inside [GenerationPackage].
 */
class BundlerSymbolProcessor(
    val codeGenerator: CodeGenerator,
    @Suppress("unused")
    val logger: KSPLogger,
    @Suppress("unused") // TODO: use or remove
    val config: Map<String, String>,
    val isCommon: Boolean
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(Annotation)
            .filterIsInstance<KSClassDeclaration>()
            .filterNot { it.isActual }
            .filterNot { it.isExpect }
            .filter { it.classKind == ClassKind.OBJECT }
            .filter { it.isPublic() }
            .toList()

        if (symbols.isEmpty()) return emptyList()

        if (!isCommon) file(GenerationPackage, GenerationFileName) {
            property(
                GenerationFileName,
                ImmutableCollections.type(ImmutableList).parameterizedBy(LoaderPackage.type(SuperClassName)),
                KModifier.PUBLIC, KModifier.ACTUAL
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
        }.writeTo(codeGenerator, aggregating = true, symbols.mapNotNull { it.containingFile })

        return symbols.filterNot { it.validate() }
    }
}
