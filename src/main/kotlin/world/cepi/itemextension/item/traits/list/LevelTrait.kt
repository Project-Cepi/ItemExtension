package world.cepi.itemextension.item.traits.list

import net.minestom.server.chat.ChatColor
import world.cepi.itemextension.item.Item
import world.cepi.kstom.plus

/** The level amount required to even use the item, usually to define its overall quality. */
class LevelTrait(
    /** The minimum level required to use the item passed as a parameter. */
    val level: Int
) : ItemTrait() {

    override val loreIndex = 1f
    override val taskIndex = 1f

    override fun renderLore(item: Item): List<String> {
        return listOf(ChatColor.GRAY + "Level Min. " + ChatColor.WHITE + level, "")
    }
}