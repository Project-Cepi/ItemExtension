package world.cepi.itemextension.item.traits.list

import net.minestom.server.chat.ChatColor
import world.cepi.itemextension.command.plus
import world.cepi.itemextension.item.traits.Trait

/** The level amount required to even use the item, usually to define its overall quality. */
class LevelTrait(
    /** The minimum level required to use the item passed as a parameter. */
    private val level: Int
) : Trait {

    override val loreIndex = 0

    override fun renderLore(): List<String> {
        return listOf(ChatColor.GRAY +  "Min. Level " + ChatColor.WHITE + level)
    }

}