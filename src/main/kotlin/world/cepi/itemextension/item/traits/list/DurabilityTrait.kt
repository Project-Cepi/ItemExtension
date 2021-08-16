package world.cepi.itemextension.item.traits.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import net.minestom.server.item.ItemStackBuilder
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.ItemTrait
import world.cepi.kstom.item.withMeta

@Serializable
@SerialName("durability")
class DurabilityTrait(val maxDurability: Int, val currentDurability: Int) : ItemTrait() {

    override val taskIndex = 1f

    override fun task(item: ItemStackBuilder, originalItem: Item) {
        item.withMeta { this.damage(maxDurability - currentDurability) } // TODO use registry
    }

    override fun renderLore(item: Item): List<Component> {
        return listOf(Component.text("Uses: $currentDurability/$maxDurability"))
    }
}