package world.cepi.itemextension.item

import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import world.cepi.itemextension.item.traits.Trait
import world.cepi.itemextension.item.traits.TraitContainer
import world.cepi.itemextension.item.traits.getTrait
import world.cepi.itemextension.item.traits.list.MaterialTrait
import world.cepi.itemextension.item.traits.list.RarityTrait

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
    fun renderItem(amount: Byte): ItemStack {

        assert(hasTrait(MaterialTrait::class))

        val item = ItemStack(getTrait<Trait, MaterialTrait>().material, amount, 0)

        val lore: MutableList<String> = mutableListOf()

        traits.sortedByDescending { it.loreIndex }.forEach {
            it.task(item)
            lore.addAll(it.renderLore())
        }

        return item
    }


}