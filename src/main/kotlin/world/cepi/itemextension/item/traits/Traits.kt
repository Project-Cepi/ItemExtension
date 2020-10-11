package world.cepi.itemextension.item.traits

import world.cepi.itemextension.item.traits.list.MaterialTrait
import world.cepi.itemextension.item.traits.list.NameTrait
import world.cepi.itemextension.item.traits.list.RarityTrait
import kotlin.reflect.KClass

enum class Traits(val clazz: KClass<out Trait>) {

    RARITY(RarityTrait::class),
    NAME(NameTrait::class),
    MATERIAL(MaterialTrait::class),

}