package world.cepi.itemextension

import net.minestom.server.MinecraftServer
import net.minestom.server.extensions.Extension
import world.cepi.itemextension.command.ItemCommand

/** Extension wrapper for Minestom. */
object ItemExtension : Extension() {

    override fun initialize() {
        MinecraftServer.getCommandManager().register(ItemCommand())

        logger.info("[ItemExtension] has been enabled!")
    }

    override fun terminate() {
        logger.info("[ItemExtension] has been disabled!")
    }

}