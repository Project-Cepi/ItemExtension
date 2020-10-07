package world.cepi.itemextension

import net.minestom.server.MinecraftServer
import net.minestom.server.extensions.Extension;
import world.cepi.itemextension.command.ItemCommand

class ItemExtension : Extension() {

    override fun initialize() {
        logger.info("[ItemExtension] has been enabled!")

        MinecraftServer.getCommandManager().register(ItemCommand())
    }

    override fun terminate() {
        logger.info("[ItemExtension] has been disabled!")
    }

}