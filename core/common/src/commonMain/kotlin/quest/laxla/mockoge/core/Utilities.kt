package quest.laxla.mockoge.core

import kotlin.jvm.JvmName

@JvmName("createIdentifier")
public infix fun String.at(provider: NamespaceProvider): Identifier = Identifier(provider.namespace, this)