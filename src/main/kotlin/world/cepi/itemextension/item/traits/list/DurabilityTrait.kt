package world.cepi.itemextension.item.traits.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.item.ItemStackBuilder
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.ItemTrait
import world.cepi.kstom.item.withMeta

@Serializable
@SerialName("durability")
class DurabilityTrait(val maxDurability: Int, val currentDurability: Int) : ItemTrait() {

    override val taskIndex = 1f

    override fun task(item: ItemStackBuilder, originalItem: Item) {
        item.withMeta {
            this.damage(
                (maxDurability - currentDurability) *
                    (originalItem.get<MaterialTrait>()!!.material.registry().maxDamage() / maxDurability)
            )
        }
    }

    override fun renderLore(item: Item): List<Component> {
        return listOf(
            Component.text("Uses: $currentDurability/$maxDurability", NamedTextColor.WHITE)
                .decoration(TextDecoration.ITALIC, false)
        )
    }
}