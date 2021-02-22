package world.cepi.itemextension.item.traits

import kotlinx.serialization.Serializable
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.KItem
import world.cepi.itemextension.item.traits.list.*
import world.cepi.itemextension.item.traits.list.attacks.AttackTrait
import world.cepi.itemextension.item.traits.list.attributes.AttributeTrait

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
    open fun task(item: KItem) { }

    /**
     * This function is called based on the [loreIndex] of the [ItemTrait]
     *
     * @return A list of ColoredText used to display on an array
     */
    open fun renderLore(item: Item): List<String> {
        return arrayListOf()
    }

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