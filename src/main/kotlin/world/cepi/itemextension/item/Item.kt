package world.cepi.itemextension.item

import net.minestom.server.chat.ColoredText
import net.minestom.server.data.DataImpl
import net.minestom.server.item.ItemFlag
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import world.cepi.itemextension.item.traits.Trait
import world.cepi.itemextension.item.traits.TraitContainer

/** Item object wrapper for Cepi's items. Built on top of the decorator pattern, calling them traits. */
class Item: TraitContainer<Trait> {

    override val traits: MutableList<Trait> = mutableListOf()

    /**
     * Renders an item to an ItemStack.
     *
     * @param amount The amount of the item to return
     *
     * @return The ItemStack after applying traits to the Item
     */
    fun renderItem(amount: Byte = 1): ItemStack {

        val item = ItemStack(Material.PAPER, amount, 0)

        val lore: MutableList<ColoredText> = mutableListOf()

        traits.sortedByDescending { it.loreIndex }.forEach {
            it.task(item)
            lore.addAll(it.renderLore())
        }

        if (item.lore == null) item.lore = arrayListOf<ColoredText>()
        item.lore!!.removeAll { true }
        item.lore!!.addAll(lore)

        item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS,
            ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE)

        val data = DataImpl()

        data.set(key, this, Item::class.java)

        item.data = data

        return item
    }

    companion object {
        /** Key for klaxon JSON storage. */
        const val key = "cepi-item"
    }


}