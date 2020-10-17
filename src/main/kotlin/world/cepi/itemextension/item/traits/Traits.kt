package world.cepi.itemextension.item.traits

import world.cepi.itemextension.item.traits.list.*
import kotlin.reflect.KClass

enum class Traits(val clazz: KClass<out Trait>) {

    RARITY(RarityTrait::class),
    NAME(NameTrait::class),
    MATERIAL(MaterialTrait::class),
    LEVEL(LevelTrait::class),
    DAMAGE(DamageTrait::class)

}