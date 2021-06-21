package world.cepi.itemextension.command.itemcommand.loaders.actions

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player
import world.cepi.itemextension.command.itemcommand.*
import world.cepi.itemextension.command.itemcommand.loaders.processTraitName
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.checkIsItem
import world.cepi.itemextension.item.itemSerializationModule
import world.cepi.itemextension.item.traits.ItemTrait
import world.cepi.kepi.messages.sendFormattedTranslatableMessage
import world.cepi.kstom.command.addSyntax
import world.cepi.kstom.command.arguments.argumentsFromClass
import world.cepi.kstom.command.arguments.literal
import world.cepi.kstom.command.setArgumentCallback
import world.cepi.kstom.item.get
import kotlin.reflect.KClass
import kotlin.reflect.full.*

// TODO break down and organize
object SetSubcommand : Command("set") {

    val traits = ItemTrait.classList

    init {

        traits.forEach { trait ->

            if (defineSubTraits(trait).isNotEmpty()) {

                defineSubTraits(trait).forEach {
                    generateCommand(this, trait, it)
                }

            } else
                generateCommand(this, trait)

        }
    }

    /**
     * Gets the subtrait arguments from a trait object
     * Must be a container that contains subtypes of ItemTraits.
     *
     * @param traitContainer The trait container to scan subtraits from.
     *
     * @return A list of klasses of subtraits from the trait container.
     */
    private fun defineSubTraits(traitContainer: KClass<out ItemTrait>): List<KClass<out ItemTrait>> {
        return traitContainer.sealedSubclasses

    }

    private fun generateCommand(command: Command, vararg traits: KClass<out ItemTrait>) {

        // EX: /item set attack primary, last is primary
        val lastTrait = traits.last()

        val traitConstructorArguments = argumentsFromClass(lastTrait)

        traitConstructorArguments.args.forEach {
            command.setArgumentCallback(it)
            { commandSender -> commandSender.sendFormattedTranslatableMessage("item", "trait.invalid") }

        }

        val traitArgs = traits.mapIndexed { index, loopTrait ->
            if (index != 0) { // If this trait isnt the first trait (root trait)
                // Drop the suffix, EX PrimaryAttackTrait becomes Primary
                loopTrait.simpleName!!.dropLast(traits[index - 1].simpleName!!.length).lowercase().literal()
            } else {
                processTraitName(loopTrait.simpleName!!).literal()
            }
        }

        command.addSyntax(*traitArgs.toTypedArray(), *traitConstructorArguments.args) { commandSender, arguments ->
            val player = commandSender as Player
            val itemStack = player.itemInMainHand

            if (itemStack.isAir) {
                player.sendFormattedTranslatableMessage("mob", "main.required")
                return@addSyntax
            }

            if (checkIsItem(itemStack)) {
                val item = itemStack.meta.get<Item>(Item.key, itemSerializationModule)!!

                item.put(traitConstructorArguments.createInstance(arguments, commandSender))

                player.itemInMainHand = item.renderItem(player.itemInMainHand.amount.coerceIn(1, Integer.MAX_VALUE))

                player.sendFormattedTranslatableMessage("item", "trait.add", Component.text(processTraitName(lastTrait.simpleName!!), NamedTextColor.BLUE))
            } else
                player.sendFormattedTranslatableMessage("mob", "formatted.required")

        }
    }

}