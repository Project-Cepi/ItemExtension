package world.cepi.itemextension.item.traits.list

import net.minestom.server.chat.ChatColor
import world.cepi.itemextension.command.itemcommand.plus
import world.cepi.itemextension.item.traits.TraitRefrenceList
import world.cepi.itemextension.item.traits.list.attributes.HealthAttributeTrait
import world.cepi.itemextension.item.traits.list.attributes.SpeedAttributeTrait

abstract class AttributeTrait : ItemTrait() {

    companion object: TraitRefrenceList(
            HealthAttributeTrait::class,
            SpeedAttributeTrait::class
    )

    open val name: String = "Example"
    open val value: Int = 0

    override fun renderLore(): List<String> {
        return listOf(ChatColor.DARK_GRAY + "▸" + "${ChatColor.BRIGHT_GREEN}$value ${ChatColor.GRAY}$name")
    }

}