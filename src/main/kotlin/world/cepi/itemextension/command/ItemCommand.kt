package world.cepi.itemextension.command

import net.minestom.server.command.builder.Command
import world.cepi.itemextension.command.loaders.loaders

class ItemCommand : Command("item") {

    init {
        loaders.forEach { it.register(this) }
    }

}