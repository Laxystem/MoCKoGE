package quest.laxla.mockoge.gradle

import com.squareup.kotlinpoet.*

/**
 * @receiver Package Name
 */
fun String.type(vararg simpleNames: String) = ClassName(this, *simpleNames)

/**
 * @receiver Package Name
 */
fun String.member(name: String) = MemberName(this, name)

inline fun file(packageName: String, className: String, block: FileSpec.Builder.() -> Unit): FileSpec =
    FileSpec.builder(packageName, className).apply(block).build()

inline fun FileSpec.Builder.property(
    name: String,
    type: TypeName,
    vararg modifiers: KModifier,
    block: PropertySpec.Builder.() -> Unit
): PropertySpec = PropertySpec.builder(name, type, *modifiers).apply(block).build().also(this::addProperty)

inline fun PropertySpec.Builder.initializer(block: CodeBlock.Builder.() -> Unit) = initializer(CodeBlock.builder().apply(block).build())
