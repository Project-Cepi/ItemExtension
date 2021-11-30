package world.cepi.itemextension.command.itemcommand.loaders.actions

import net.minestom.server.entity.Player
import world.cepi.itemextension.data.RegisteredItem
import world.cepi.itemextension.data.RegisteredItemModel
import world.cepi.itemextension.item.cepiItem
import world.cepi.kepi.command.subcommand.KepiRegistrySubcommand
import world.cepi.kepi.data.ContentDataHandler
import world.cepi.kepi.item.AddCreationalItem

object RegistrySubcommand : KepiRegistrySubcommand<RegisteredItem>(
    ContentDataHandler.main,
    RegisteredItemModel,
    item@ { AddCreationalItem.put(player, it.item.renderItem(1)) },
    add@ { RegisteredItem(it, (sender as? Player)?.itemInMainHand?.cepiItem ?: return@add null) }
)