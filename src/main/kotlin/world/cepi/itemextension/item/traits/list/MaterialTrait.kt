package world.cepi.itemextension.item.traits.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minestom.server.item.ItemMeta
import net.minestom.server.item.Material
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.ItemTrait
import world.cepi.kstom.command.arguments.generation.annotations.DefaultNumber
import world.cepi.kstom.serializer.MaterialSerializer

/** The material that the item will take on. Default is Paper. */
@Serializable
@SerialName("material")
data class MaterialTrait(
    /** The display material for the item */
    @Serializable(with = MaterialSerializer::class)
    val material: Material = Material.PAPER,

    /** CustomModelData for the item (resource pack) */
    @param:DefaultNumber(0.0)
    private val customModelData: Int = 0
) : ItemTrait() {

    override val taskIndex = -1f

    override fun task(item: ItemMeta.Builder, originalItem: Item): Unit = with(item) {
        if (customModelData != 0) customModelData(customModelData)
    }

}