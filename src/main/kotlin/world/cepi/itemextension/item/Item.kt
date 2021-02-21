package world.cepi.itemextension.item

import kotlinx.serialization.Serializable
import net.minestom.server.chat.ColoredText
import net.minestom.server.data.DataImpl
import net.minestom.server.item.ItemFlag
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import world.cepi.itemextension.item.traits.Trait
import world.cepi.itemextension.item.traits.TraitContainer

/** Item object wrapper for Cepi's items. Built on top of the decorator pattern, calling them traits. */
@Serializable
class Item: TraitContainer<Trait>() {

    override val traits: MutableList<Trait> = mutableListOf()

    /**
     * Renders an item to an ItemStack.
     *
     * @param amount The amount of the item to return
     *
     * @return The ItemStack after applying traits to the Item
     */
    fun renderItem(amount: Byte = 1): KItem {

        val item = KItem(Material.PAPER, amount)

        val data = DataImpl()

        data.set(key, this, Item::class.java)

        item.data = data

        item.lore = traits.sortedBy(::sortLore)
            .map { trait -> trait.renderLore(this).map { ColoredText.of(it) } }
            .flatten()

        traits.sortedBy(::sortTask).forEach { it.task(item) }

        item.addItemFlags(*ItemFlag.values())

        return item
    }

    companion object {
        /** Key for klaxon JSON storage. */
        const val key = "cepi-item"

        fun sortLore(trait: Trait): Float {
            return trait.loreIndex
        }

        fun sortTask(trait: Trait): Float {
            return trait.loreIndex
        }
    }


}

/**
 * Check if an [ItemStack] contains [Item] data.
 * It checks it via the itemstack's [DataImpl]
 *
 * @param itemStack The itemStack to check by
 *
 * @return If the [ItemStack] "is" an [Item]
 */
fun checkIsItem(itemStack: ItemStack): Boolean {
    // data must be initialized for an itemStack
    if (itemStack.data == null) itemStack.data = DataImpl()

    return itemStack.data!!.hasKey(Item.key)
}