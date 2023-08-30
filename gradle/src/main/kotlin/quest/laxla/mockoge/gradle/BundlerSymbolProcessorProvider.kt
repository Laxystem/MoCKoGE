package quest.laxla.mockoge.gradle

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class BundlerSymbolProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        BundlerSymbolProcessor(environment.codeGenerator, environment.logger, environment.options, environment.platforms.size > 1)
}