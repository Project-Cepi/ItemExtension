package world.cepi.itemextension.item.traits.list

import net.minestom.server.chat.ChatColor
import world.cepi.itemextension.command.plus

interface AttributeTrait : ItemTrait {

    val name: String
    val value: Int

    override fun renderLore(): List<String> {
        return listOf(ChatColor.DARK_GRAY + "▸" + "${ChatColor.BRIGHT_GREEN}$value ${ChatColor.GRAY}$name")
    }



}