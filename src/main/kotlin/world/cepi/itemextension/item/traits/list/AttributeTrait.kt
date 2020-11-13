package world.cepi.itemextension.item.traits.list

import net.minestom.server.chat.ChatColor
import world.cepi.itemextension.command.plus
import world.cepi.itemextension.item.traits.Trait
import world.cepi.itemextension.item.traits.TraitRefrenceList

class AttributeTrait : Trait {

    companion object: TraitRefrenceList(

    )

    val name: String = "Example"
    val value: Int = 0

    override fun renderLore(): List<String> {
        return listOf(ChatColor.DARK_GRAY + "▸" + "${ChatColor.BRIGHT_GREEN}$value ${ChatColor.GRAY}$name")
    }



}