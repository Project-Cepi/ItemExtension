package world.cepi.itemextension

import net.minestom.server.MinecraftServer
import net.minestom.server.extensions.Extension
import world.cepi.itemextension.command.ClearCommand
import world.cepi.itemextension.command.GiveCommand
import world.cepi.itemextension.command.itemcommand.ItemCommand
import world.cepi.itemextension.events.DisableDropping

/** Extension wrapper for Minestom. */
object ItemExtension : Extension() {

    override fun initialize() {
        MinecraftServer.getCommandManager().register(ItemCommand())
        MinecraftServer.getCommandManager().register(ClearCommand())
        MinecraftServer.getCommandManager().register(GiveCommand())

        MinecraftServer.getConnectionManager().addPlayerInitialization {
            DisableDropping.load(it)
        }

        logger.info("[ItemExtension] has been enabled!")
    }

    override fun terminate() {
        logger.info("[ItemExtension] has been disabled!")
    }

}