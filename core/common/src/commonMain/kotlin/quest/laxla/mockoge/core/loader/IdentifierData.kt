package quest.laxla.mockoge.core.loader

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import quest.laxla.mockoge.core.Identifier
import kotlin.jvm.JvmInline

@JvmInline
@Serializable(with = IdentifierData.Serializer::class)
public value class IdentifierData(
    internal val identifier: Identifier
) {
    public companion object Serializer : KSerializer<IdentifierData> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(this::class.qualifiedName!!, PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: IdentifierData) {
            encoder.encodeString(value.identifier.toString())
        }

        override fun deserialize(decoder: Decoder): IdentifierData {
            return IdentifierData(Identifier(decoder.decodeString()))
        }
    }
}
