package quest.laxla.mockoge.util

import quest.laxla.mockoge.Identifier
import quest.laxla.mockoge.NamespaceProvider
import kotlin.jvm.JvmName

public const val NamespaceGroup: String = "namespace"
public const val PathGroup: String = "path"

public const val IdentifierSeparator: Char = ':'
public const val PathSeparator: Char = '/'
public const val NamespaceSeparator: Char = '/'

public val PathPattern: Regex = "[a-z_]+($PathSeparator[a-z_]+)*".toRegex()
public val NamespacePattern: Regex = "[a-z_]+($NamespaceSeparator[a-z_]+)*".toRegex()
public val IdentifierPattern: Regex =
    "(?<$NamespaceGroup>${NamespacePattern.pattern})$IdentifierSeparator(?<$PathGroup>${PathPattern.pattern})".toRegex()

@JvmName("create")
public infix fun String.at(provider: NamespaceProvider): Identifier = Identifier(provider.namespace, this)
