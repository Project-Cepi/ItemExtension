package world.cepi.itemextension.item.traits.list

import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

/** The material that the item will take on. Default is Paper. */
class MaterialTrait(
    /** The display material for the item */
    private val material: Material = Material.PAPER,

    /** CustomModelData for the item (resource pack) */
    private val customModelData: Int = 0
) : ItemTrait {

    override fun task(item: ItemStack) {
        item.customModelData = customModelData
        item.material = material
    }

}