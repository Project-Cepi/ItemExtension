package world.cepi.itemextension.item

import net.minestom.server.chat.ColoredText
import net.minestom.server.data.DataImpl
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import world.cepi.itemextension.item.traits.Trait
import world.cepi.itemextension.item.traits.TraitContainer

/**
 * Item object wrapper for Cepi's items. Built on top of the decorator pattern, calling them traits.
 */
class Item: TraitContainer<Trait> {

    override val traits: MutableList<Trait> = mutableListOf()

    /**
     * Renders an item to an ItemStack.
     *
     * @param amount The amount of the item to return
     */
    fun renderItem(amount: Byte = 1): ItemStack {

        val item = ItemStack(Material.PAPER, amount, 0)

        if (item.data == null) {
            item.data = DataImpl()
        }

        item.data.set(key, this, Item::class.java)

        val lore: MutableList<ColoredText> = mutableListOf()

        traits.sortedByDescending { it.loreIndex }.forEach {
            it.task(item)
            lore.addAll(it.renderLore())
        }

        item.lore.removeAll { true }
        item.lore.addAll(lore)

        return item
    }

    companion object {
        /**
         * Key for klaxon JSON storage.
         */
        const val key = "cepi-item"
    }


}