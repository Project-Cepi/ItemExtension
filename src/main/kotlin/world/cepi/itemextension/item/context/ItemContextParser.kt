package world.cepi.itemextension.item.context

import net.minestom.server.command.CommandSender
import net.minestom.server.entity.Player
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.itemSerializationModule
import world.cepi.kstom.command.arguments.context.ContextParser
import world.cepi.kstom.item.get

/**
 * Context parser to get a player's item in main hand.
 */
object ItemContextParser : ContextParser<Item> {

    override fun parse(sender: CommandSender): Item? =
        (sender as? Player)?.itemInMainHand?.meta?.get(Item.key, itemSerializationModule)

}