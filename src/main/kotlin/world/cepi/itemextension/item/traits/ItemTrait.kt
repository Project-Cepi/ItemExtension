package world.cepi.itemextension.item.traits

import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import net.minestom.server.item.ItemStackBuilder
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.list.*
import world.cepi.itemextension.item.traits.list.attacks.AttackTrait
import world.cepi.itemextension.item.traits.list.stats.StatTrait

/** Trait objects that get appended to Items. Inspired by the decorator pattern */
@Serializable
abstract class ItemTrait {

    /** The position where this trait is rendered in the item lore (for item rendering). */
    abstract val loreIndex: Float

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
        DamageTrait::class,
        LevelTrait::class,
        NameTrait::class,
        RarityTrait::class,
        AttackSpeedTrait::class,
        PriceTrait::class,
        MaterialTrait::class,
        ArmorTrait::class,
        LoreTrait::class,
        TypeTrait::class,
        StatTrait::class,
        AttackTrait::class
    )

}