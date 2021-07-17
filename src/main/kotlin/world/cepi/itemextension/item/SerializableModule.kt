package world.cepi.itemextension.item

import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import world.cepi.itemextension.item.traits.ItemTrait
import world.cepi.itemextension.item.traits.list.*
import world.cepi.itemextension.item.traits.list.actions.PrimaryActionTrait
import world.cepi.itemextension.item.traits.list.actions.SecondaryActionTrait
import world.cepi.itemextension.item.traits.list.actions.TertiaryActionTrait
import world.cepi.itemextension.item.traits.list.stats.HealthStatTrait
import world.cepi.itemextension.item.traits.list.stats.SpeedStatTrait

val itemSerializationModule = SerializersModule {

    // TODO sealed / better serializaiton?
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
        subclass(PriceTrait::class)
        subclass(KnockbackTrait::class)

        subclass(PrimaryActionTrait::class)
        subclass(SecondaryActionTrait::class)
        subclass(TertiaryActionTrait::class)

        subclass(HealthStatTrait::class)
        subclass(SpeedStatTrait::class)

        subclass(CustomTextTrait::class)
    }
}