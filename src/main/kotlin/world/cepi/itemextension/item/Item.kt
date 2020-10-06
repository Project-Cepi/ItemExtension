package world.cepi.itemextension.item

import net.minestom.server.item.Material

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
) {
}