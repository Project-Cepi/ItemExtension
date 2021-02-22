package world.cepi.itemextension.item.traits.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minestom.server.chat.ChatColor
import world.cepi.itemextension.command.itemcommand.plus
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.ItemTrait

// TODO make armor trait functional
@SerialName("armor")
@Serializable
class ArmorTrait(
    val armor: Int
) : ItemTrait() {

    override val loreIndex = 4f
    override val taskIndex = 1f

    override fun renderLore(item: Item): List<String> {
        return listOf(ChatColor.CYAN + "+$armor Armor")
    }

}