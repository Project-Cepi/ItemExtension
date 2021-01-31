package world.cepi.itemextension.item.traits.list

import net.minestom.server.chat.ChatColor
import net.minestom.server.item.ItemStack
import world.cepi.itemextension.command.itemcommand.plus

/** The damage this brings upon attackers */
class DamageTrait(
    val damage: Double
) : ItemTrait() {

    override val loreIndex = 2
    override val taskIndex = 1

    override fun renderLore(): List<String> {
        return listOf(ChatColor.GOLD + "+$damage Damage")
    }

    override fun task(item: ItemStack) {
        item.data!!.set("damage", damage)
    }

}