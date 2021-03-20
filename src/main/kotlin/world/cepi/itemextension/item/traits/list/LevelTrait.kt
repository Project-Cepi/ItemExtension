package world.cepi.itemextension.item.traits.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.ItemTrait

/** The level amount required to even use the item, usually to define its overall quality. */
@SerialName("level")
@Serializable
class LevelTrait(
    /** The minimum level required to use the item passed as a parameter. */
    val level: Int
) : ItemTrait() {

    override val loreIndex = 1f
    override val taskIndex = 1f

    override fun renderLore(item: Item): List<Component> {
        return listOf(
            Component.text("Level Min. ", NamedTextColor.GRAY)
                .append(Component.text(level, NamedTextColor.WHITE))
            , Component.space())
    }
}