package world.cepi.itemextension.item.traits.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minestom.server.item.ItemStackBuilder
import net.minestom.server.item.Material
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.ItemTrait
import world.cepi.kstom.command.arguments.annotations.DefaultNumber

/** The material that the item will take on. Default is Paper. */
@Serializable
@SerialName("material")
data class MaterialTrait(
    /** The display material for the item */
    val material: Material = Material.PAPER,

    /** CustomModelData for the item (resource pack) */
    @param:DefaultNumber(0.0)
    private val customModelData: Int = 0
) : ItemTrait() {

    override val taskIndex = 1f

    override fun task(item: ItemStackBuilder, originalItem: Item) {
        item.meta { it.customModelData(customModelData) }
    }

}