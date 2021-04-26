package world.cepi.itemextension.command.itemcommand

import net.minestom.server.command.builder.Command
import world.cepi.itemextension.command.itemcommand.loaders.actions.*
import world.cepi.itemextension.command.itemcommand.loaders.loaders

object ItemCommand : Command("item") {

    init {

        // Add the actions
        arrayOf(
            CreateSubcommand,
            RemoveSubcommand,
            ResetSubcommand,
            SetSubcommand,
            UpdateSubcommand,
            DataSubcommand
        ).forEach {
            this.addSubcommand(it)
        }

        // Load loaders
        loaders.forEach { it.register(this) }
    }

}