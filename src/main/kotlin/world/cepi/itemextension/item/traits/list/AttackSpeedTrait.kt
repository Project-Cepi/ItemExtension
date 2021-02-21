package world.cepi.itemextension.item.traits.list

import net.minestom.server.chat.ChatColor
import world.cepi.itemextension.command.itemcommand.plus
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.list.attributes.AttributeTrait

class AttackSpeedTrait(
        /** The attack speed of the trait. */
        val attackSpeed: Double
) : ItemTrait() {

    override val loreIndex = 5f
    override val taskIndex = 1f

    override fun renderLore(item: Item): List<String> {
        return if (item.softHasTrait<AttributeTrait>()) {
            listOf(ChatColor.GRAY + attackSpeed.toString() + "s Attack Speed", "")
        } else {
            listOf(ChatColor.GRAY + attackSpeed.toString() + "s Attack Speed")
        }
    }

}