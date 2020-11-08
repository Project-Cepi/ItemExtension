package world.cepi.itemextension.command.loaders

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import world.cepi.itemextension.command.sendFormattedMessage
import world.cepi.itemextension.item.traits.Traits

object TraitListLoader : ItemCommandLoader {

    override fun register(command: Command) {
        val traitList = ArgumentType.Word("trait")
            .from(*Traits.values().map { it.name.toLowerCase() }
                .toTypedArray())

        command.setArgumentCallback({ commandSender, _, _ -> commandSender.sendFormattedMessage("Invalid trait!") }, traitList)
    }

}