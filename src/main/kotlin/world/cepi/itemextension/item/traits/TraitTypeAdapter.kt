package world.cepi.itemextension.item.traits

import com.beust.klaxon.TypeAdapter
import kotlin.reflect.KClass

class TraitTypeAdapter: TypeAdapter<Trait> {

    override fun classFor(type: Any): KClass<out Trait> = when(type as String) {
        else -> throw IllegalArgumentException("Unknown type: $type")
    }

}