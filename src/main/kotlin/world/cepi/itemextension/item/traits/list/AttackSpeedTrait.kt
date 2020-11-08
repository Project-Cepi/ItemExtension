package world.cepi.itemextension.item.traits.list

import net.minestom.server.chat.ChatColor
import world.cepi.itemextension.command.plus
import world.cepi.itemextension.item.traits.Trait

class AttackSpeedTrait(
        val attackSpeed: Int
) : Trait {

    override val loreIndex = 0

    override fun renderLore(): List<String> {
        return listOf(ChatColor.GRAY +  "Attack speed " + ChatColor.WHITE + attackSpeed)
    }

}