package world.cepi.itemextension.item

import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import world.cepi.itemextension.item.traits.Trait
import world.cepi.itemextension.item.traits.TraitContainer
import kotlin.reflect.KClass

/**
 * Item object wrapper for Cepi's items. Built on top of the decorator pattern, calling them traits.
 */
class Item(
    /**
     * The name of the item. Color is defiend by rarity, not the name
     */
    var name: String,

    /**
     * The material of the item. For display reasons. customModelData can be defined as a trait.
     */
    var material: Material,

    /**
     * The rarity of the Item.
     */
    var rarity: Rarity
): TraitContainer<Trait> {

    override val traits: MutableList<Trait> = mutableListOf()

    /**
     * Renders an item to an ItemStack.
     *
     * @param amount The amount of the item to return
     */
    fun renderItem(amount: Byte): ItemStack {
        return ItemStack(material, amount, 0)
    }


}