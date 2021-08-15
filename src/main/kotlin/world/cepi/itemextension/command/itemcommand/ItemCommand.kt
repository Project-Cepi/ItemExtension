package world.cepi.itemextension.command.itemcommand

import net.minestom.server.command.builder.Command
import world.cepi.itemextension.command.itemcommand.loaders.actions.*
import world.cepi.itemextension.command.itemcommand.loaders.loaders
import world.cepi.kstom.command.addSubcommands

object ItemCommand : Command("item") {

    init {

        addSubcommands(
            CreateSubcommand,
            RemoveSubcommand,
            ResetSubcommand,
            SetSubcommand,
            UpdateSubcommand,
            DataSubcommand,
            RegistrySubcommand
        )

        // Load loaders
        loaders.forEach { it.register(this) }
    }

}