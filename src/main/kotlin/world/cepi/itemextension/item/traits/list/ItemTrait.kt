package world.cepi.itemextension.item.traits.list

import world.cepi.itemextension.item.traits.Trait
import world.cepi.itemextension.item.traits.TraitRefrenceList

open class ItemTrait : Trait {
    companion object: TraitRefrenceList(
            DamageTrait::class,
            LevelTrait::class,
            NameTrait::class,
            RarityTrait::class,
            AttackSpeedTrait::class,
            MaterialTrait::class,
            ArmorTrait::class,
            LoreTrait::class,
            TypeTrait::class
    )
}