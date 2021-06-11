package world.cepi.itemextension.item.traits

import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import net.minestom.server.item.ItemStackBuilder
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.list.*
import world.cepi.itemextension.item.traits.list.attacks.PrimaryAttackTrait
import world.cepi.itemextension.item.traits.list.attacks.SecondaryAttackTrait
import world.cepi.itemextension.item.traits.list.attacks.TertiaryAttackTrait
import world.cepi.itemextension.item.traits.list.stats.HealthStatTrait
import world.cepi.itemextension.item.traits.list.stats.SpeedStatTrait

// TODO just make ordered list for lore priority -- easier to read.
/** Trait objects that get appended to Items. Inspired by the decorator pattern */
@Serializable
abstract class ItemTrait {

    /** The position where this task is run in the item (for item rendering). */
    abstract val taskIndex: Float

    /**
     * This function runs at item render time.
     *
     * @param item The item that is being rendered.
     */
    open fun task(item: ItemStackBuilder, originalItem: Item) { }

    /**
     * This function is called based on the [loreIndex] of the [ItemTrait]
     *
     * @return A list of ColoredText used to display on an array
     */
    open fun renderLore(item: Item): List<Component> {
        return arrayListOf()
    }

    companion object: TraitRefrenceList(
        // Background (lore-unrendered) traits
        PriceTrait::class,
        MaterialTrait::class,

        // Non-lore traits
        NameTrait::class,

        // Rendered traits
        TypeTrait::class,
        LevelTrait::class,

        PrimaryAttackTrait::class,
        SecondaryAttackTrait::class,
        TertiaryAttackTrait::class,

        DamageTrait::class,
        ArmorTrait::class,
        AttackSpeedTrait::class,

        HealthStatTrait::class,
        SpeedStatTrait::class,

        RarityTrait::class,
        LoreTrait::class,
        CustomTextTrait::class
    )

}