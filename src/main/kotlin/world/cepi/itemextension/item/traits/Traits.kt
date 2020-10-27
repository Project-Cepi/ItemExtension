package world.cepi.itemextension.item.traits

import world.cepi.itemextension.item.traits.list.*
import kotlin.reflect.KClass

enum class Traits(val clazz: KClass<out Trait>) {

    DAMAGE(DamageTrait::class),
    LEVEL(LevelTrait::class),
    NAME(NameTrait::class),
    RARITY(RarityTrait::class),
    MATERIAL(MaterialTrait::class),

}