package world.cepi.itemextension.item.traits

import world.cepi.itemextension.item.traits.list.*
import kotlin.reflect.KClass

/** List of traits represented as an enum to contain more data. */
enum class Traits(val clazz: KClass<out Trait>) {

    ATTACK_SPEED(AttackSpeedTrait::class),
    DAMAGE(DamageTrait::class),
    LEVEL(LevelTrait::class),
    NAME(NameTrait::class),
    RARITY(RarityTrait::class),
    MATERIAL(MaterialTrait::class),
    ARMOR(ArmorTrait::class),
    LORE(LoreTrait::class),
    TYPE(TypeTrait::class)

}