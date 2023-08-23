package quest.laxla.mockoge.core.util

public inline infix fun <T> T?.otherwise(block: () -> Unit): T? = this ?: null.also { block() }