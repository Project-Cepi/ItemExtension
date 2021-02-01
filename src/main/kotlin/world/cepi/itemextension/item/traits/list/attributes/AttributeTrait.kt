package world.cepi.itemextension.item.traits.list.attributes

import net.minestom.server.chat.ChatColor
import world.cepi.itemextension.command.itemcommand.plus
import world.cepi.itemextension.item.traits.TraitRefrenceList
import world.cepi.itemextension.item.traits.list.ItemTrait

abstract class AttributeTrait : ItemTrait() {

    companion object: TraitRefrenceList(
            HealthAttributeTrait::class,
            SpeedAttributeTrait::class
    )

    override val loreIndex = 5
    override val taskIndex = 1

    open val name: String = "Example"
    open val value: Double = 0.0

    override fun renderLore(): List<String> {
        return if (value >= 0)
            listOf(ChatColor.DARK_GRAY + "► " + "${ChatColor.BRIGHT_GREEN}+$value ${ChatColor.GRAY}$name")
        else
            listOf(ChatColor.DARK_GRAY + "► " + "${ChatColor.RED}-$value ${ChatColor.GRAY}$name")
    }

}