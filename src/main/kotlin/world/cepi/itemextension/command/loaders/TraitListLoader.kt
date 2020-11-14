package world.cepi.itemextension.command.loaders

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import world.cepi.itemextension.command.sendFormattedMessage
import world.cepi.itemextension.item.traits.list.ItemTrait

object TraitListLoader : ItemCommandLoader {

    override fun register(command: Command) {
        val traitList = ArgumentType.Word("trait")
            .from(*ItemTrait.classList.map { processTraitName(it.simpleName!!) }
                .toTypedArray())

        command.setArgumentCallback({ commandSender, _, _ -> commandSender.sendFormattedMessage("Invalid trait!") }, traitList)
    }

}

fun processTraitName(name: String) = name.toLowerCase().substring(0..name.length - 5)