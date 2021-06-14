package world.cepi.itemextension.item.traits

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.reflect.KClass

internal object ItemTraitMapSerializer : KSerializer<MutableMap<KClass<out ItemTrait>, ItemTrait>> {

    val dataSerializer = ListSerializer(ItemTrait.serializer())

    override val descriptor: SerialDescriptor = dataSerializer.descriptor
    override fun serialize(encoder: Encoder, value: MutableMap<KClass<out ItemTrait>, ItemTrait>)
        = dataSerializer.serialize(encoder, value.values.toList())
    override fun deserialize(decoder: Decoder) = dataSerializer.deserialize(decoder).map {
        it::class to it
    }.toMap().toMutableMap()
}