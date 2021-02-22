package world.cepi.itemextension.item

import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import world.cepi.itemextension.item.traits.ItemTrait
import world.cepi.itemextension.item.traits.list.*
import world.cepi.itemextension.item.traits.list.attacks.AttackTrait
import world.cepi.itemextension.item.traits.list.attacks.PrimaryAttackTrait
import world.cepi.itemextension.item.traits.list.attacks.SecondaryAttackTrait
import world.cepi.itemextension.item.traits.list.attacks.TertiaryAttackTrait
import world.cepi.itemextension.item.traits.list.attributes.AttributeTrait
import world.cepi.itemextension.item.traits.list.attributes.HealthAttributeTrait
import world.cepi.itemextension.item.traits.list.attributes.SpeedAttributeTrait

val module = SerializersModule {

    polymorphic(ItemTrait::class) {
        subclass(MaterialTrait::class)
        subclass(LevelTrait::class)
        subclass(ArmorTrait::class)
        subclass(DamageTrait::class)
        subclass(LoreTrait::class)
        subclass(NameTrait::class)
        subclass(RarityTrait::class)
        subclass(TypeTrait::class)
        subclass(AttackSpeedTrait::class)
    }

    polymorphic(AttackTrait::class) {
        subclass(PrimaryAttackTrait::class)
        subclass(SecondaryAttackTrait::class)
        subclass(TertiaryAttackTrait::class)
    }

    polymorphic(AttributeTrait::class) {
        subclass(HealthAttributeTrait::class)
        subclass(SpeedAttributeTrait::class)
    }
}