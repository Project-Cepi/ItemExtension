package world.cepi.itemextension.item.traits.list

import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import world.cepi.itemextension.item.traits.Trait

class MaterialTrait(
    /**
     * The display material for the item
     */
    val material: Material = Material.PAPER,

    /**
     * CustomModelData for the item (resource pack)
     */
    val customModelData: Int = 0
) : Trait {

    override fun task(item: ItemStack) {
        item.customModelData = customModelData
    }

}