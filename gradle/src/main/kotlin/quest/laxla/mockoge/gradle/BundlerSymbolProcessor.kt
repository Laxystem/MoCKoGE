package quest.laxla.mockoge.gradle

import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import java.io.OutputStreamWriter

private const val Package = "quest.laxla.mockoge"
private const val AnnotationName = "Bundleable"
private const val Annotation = "$Package.$AnnotationName"
private const val SuperClassName = "BundleScript"
private const val SuperClass = "$Package.$SuperClassName"
private const val GenerationPackage = "$Package.generated"
private const val CoreGenerationFileName = "Core"
private const val ImmutableCollections = "kotlinx.collections.immutable"
private const val ImmutableList = "ImmutableList"

/**
 * Creates a list of all objects annotated with [Annotation] and extending [SuperClass] inside [GenerationPackage].
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

        /*
        FileSpec.builder(GenerationPackage, CoreGenerationFileName).run {
            //PropertySpec.builder("Bundles", ClassName(ImmutableCollections, ImmutableList).parameterizedBy(ClassName))
        }
         */

        codeGenerator.createNewFile(
            dependencies = Dependencies(aggregating = true, *symbols.map { it.containingFile!! }.toTypedArray()),
            packageName = GenerationPackage,
            fileName = CoreGenerationFileName
        ).use { file ->
            OutputStreamWriter(file).use { writer ->
                writer.append("package $GenerationPackage")

                symbols.forEach { it.accept(Visitor(writer), Unit) }
            }
        }

        return symbols.filterNot { it.validate() }
    }

    private inner class Visitor(private val writer: OutputStreamWriter) : KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            // todo: complete this
        }
    }
}
