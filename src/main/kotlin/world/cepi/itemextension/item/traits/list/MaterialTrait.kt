package world.cepi.itemextension.item.traits.list

import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

/** The material that the item will take on. Default is Paper. */
class MaterialTrait(
    /** The display material for the item */
    val material: Material = Material.PAPER,

    /** CustomModelData for the item (resource pack) */
    private val customModelData: Int = 0
) : ItemTrait() {

    override val taskIndex = 1
    override val loreIndex = 1

    override fun task(item: ItemStack) {
        item.customModelData = customModelData
        item.material = material
    }

}