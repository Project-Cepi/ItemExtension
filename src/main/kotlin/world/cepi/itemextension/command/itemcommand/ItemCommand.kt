package world.cepi.itemextension.command.itemcommand

import world.cepi.itemextension.command.itemcommand.loaders.actions.*
import world.cepi.itemextension.command.itemcommand.loaders.loaders
import world.cepi.kstom.command.kommand.Kommand

object ItemCommand : Kommand({

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

}, "item")