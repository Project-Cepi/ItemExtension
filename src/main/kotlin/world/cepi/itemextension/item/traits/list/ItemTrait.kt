package world.cepi.itemextension.item.traits.list

import kotlinx.serialization.Serializable
import world.cepi.itemextension.item.traits.Trait
import world.cepi.itemextension.item.traits.TraitRefrenceList
import world.cepi.itemextension.item.traits.list.attacks.AttackTrait
import world.cepi.itemextension.item.traits.list.attributes.AttributeTrait

@Serializable
abstract class ItemTrait : Trait {
    companion object: TraitRefrenceList(
            DamageTrait::class,
            LevelTrait::class,
            NameTrait::class,
            RarityTrait::class,
            AttackSpeedTrait::class,
            MaterialTrait::class,
            ArmorTrait::class,
            LoreTrait::class,
            TypeTrait::class,
            AttributeTrait::class,
            AttackTrait::class
    )
}