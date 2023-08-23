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
private const val Annotation = "$Package.BundleScript"
private const val SuperClass = "$Package.Bundle"
private const val GenerationPackage = "$Package.generated"
private const val CoreGenerationFileName = "Core"

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
            .filter { it.isPublic() } // todo: instanceof SuperClass
            .toList()


        if (symbols.isEmpty()) return emptyList()

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

    inner class Visitor(private val file: OutputStreamWriter) : KSVisitorVoid()
}
