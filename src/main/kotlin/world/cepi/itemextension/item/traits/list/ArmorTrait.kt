package world.cepi.itemextension.item.traits.list

import net.minestom.server.chat.ChatColor
import world.cepi.itemextension.command.itemcommand.plus

// TODO make armor trait
class ArmorTrait(
    val armor: Int
) : ItemTrait() {

    override val loreIndex = 3

    override fun renderLore(): List<String> {
        return listOf(ChatColor.CYAN + "+$armor Armor")
    }

}