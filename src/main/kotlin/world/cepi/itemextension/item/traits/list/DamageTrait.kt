package world.cepi.itemextension.item.traits.list

import net.minestom.server.chat.ChatColor
import world.cepi.itemextension.command.itemcommand.plus
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.KItem

/** The damage this brings upon attackers */
class DamageTrait(
    val damage: Double
) : ItemTrait() {

    override val loreIndex = 3f
    override val taskIndex = 1f

    override fun renderLore(item: Item): List<String> {
        return listOf("", ChatColor.GOLD + "+$damage Damage")
    }

    override fun task(item: KItem) {
        item.data!!.set("damage", damage)
    }

}