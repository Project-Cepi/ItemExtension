package world.cepi.itemextension.item.traits.list

import net.minestom.server.item.Material
import world.cepi.itemextension.item.KItem

/** The material that the item will take on. Default is Paper. */
class MaterialTrait(
    /** The display material for the item */
    val material: Material = Material.PAPER,

    /** CustomModelData for the item (resource pack) */
    private val customModelData: Int = 0
) : ItemTrait() {

    override val taskIndex = 1f
    override val loreIndex = 1f

    override fun task(item: KItem) {
        item.customModelData = customModelData
        item.material = material
    }

}