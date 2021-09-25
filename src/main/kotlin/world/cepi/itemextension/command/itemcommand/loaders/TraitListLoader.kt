package world.cepi.itemextension.command.itemcommand.loaders

import net.kyori.adventure.text.Component
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import world.cepi.itemextension.item.traits.ItemTrait
import world.cepi.kepi.messages.sendFormattedMessage
import world.cepi.kstom.command.kommand.Kommand

object TraitListLoader : ItemCommandLoader {

    override fun register(command: Kommand) {
        val traitList = ArgumentType.Word("trait")
            .from(*ItemTrait.classList.map { processTraitName(it.simpleName!!) }
                .toTypedArray())

        command.argumentCallback(traitList) { sender.sendFormattedMessage(Component.text("Invalid trait!")) }
    }

}

/**
 * Turns a trait name, EX NameTrait, into name.
 * NOTE: This is an unsafe method.
 *
 * @param name String that ends with "Trait"
 *
 * @return A substring of that trait, EX NameTrait turns into "name"
 */
fun processTraitName(name: String) = name.lowercase().dropLast("Trait".length)