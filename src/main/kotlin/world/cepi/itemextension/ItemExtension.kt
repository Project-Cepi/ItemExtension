package world.cepi.itemextension

import net.minestom.server.extensions.Extension;

class ItemExtension : Extension() {

    override fun initialize() {
        logger.info("[ItemExtension] has been enabled!")
    }

    override fun terminate() {
        logger.info("[ItemExtension] has been disabled!")
    }

}