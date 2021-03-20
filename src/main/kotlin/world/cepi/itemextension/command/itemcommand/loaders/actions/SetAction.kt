package world.cepi.itemextension.command.itemcommand.loaders.actions

import net.kyori.adventure.text.Component
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.minecraft.ArgumentItemStack
import net.minestom.server.entity.Player
import world.cepi.itemextension.command.itemcommand.*
import world.cepi.itemextension.command.itemcommand.loaders.ItemCommandLoader
import world.cepi.itemextension.command.itemcommand.loaders.processTraitName
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.checkIsItem
import world.cepi.itemextension.item.traits.ItemTrait
import world.cepi.itemextension.item.traits.TraitRefrenceList
import world.cepi.kepi.messages.sendFormattedMessage
import world.cepi.kstom.command.addSyntax
import world.cepi.kstom.command.arguments.argumentsFromConstructor
import world.cepi.kstom.command.arguments.asSubcommand
import world.cepi.kstom.command.setArgumentCallback
import kotlin.reflect.KClass
import kotlin.reflect.full.*

// TODO break down and organize
object SetAction : ItemCommandLoader {

    val set = "set".asSubcommand()

    val traits = ItemTrait.classList

    override fun register(command: Command) {

        traits.forEach { trait ->

            if (defineSubTraits(trait).isNotEmpty()) {

                defineSubTraits(trait).forEach {
                    generateCommand(command, trait, it)
                }

            } else
                generateCommand(command, trait)

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
        val companionClass = traitContainer.companionObjectInstance ?: return emptyList()
        if (companionClass !is TraitRefrenceList) return emptyList()

        return (traitContainer.companionObjectInstance as TraitRefrenceList).classList.toList()

    }

    private fun generateCommand(command: Command, vararg traits: KClass<out ItemTrait>) {

        val lastTrait = traits.last()

        // We will be using this constructor later to get its arguments
        val traitConstructor = lastTrait.primaryConstructor ?: return

        val traitConstructorArguments = argumentsFromConstructor(traitConstructor)

        if (traitConstructorArguments.isEmpty()) return

        traitConstructorArguments.forEach {
            command.setArgumentCallback(it)
            { commandSender -> commandSender.sendFormattedMessage(Component.text(invalidTraitArgument)) }

        }

        val traitArgs = traits.mapIndexed { index, loopTrait ->
            if (index != 0) { // If this trait isnt the first trait (root trait)
                // Drop the suffix, EX PrimaryAttackTrait becomes Primary
                loopTrait.simpleName!!.dropLast(traits[index - 1].simpleName!!.length).toLowerCase().asSubcommand()
            } else {
                processTraitName(loopTrait.simpleName!!).asSubcommand()
            }
        }

        command.addSyntax(set, *traitArgs.toTypedArray(), *traitConstructorArguments.toTypedArray()) { commandSender, arguments ->
            val values: List<Any> = traitConstructorArguments.map { entry ->
                return@map when(entry) {
                    is ArgumentItemStack ->
                        arguments.get(entry).material
                    else -> arguments.get(entry)
                }
            }

            val player = commandSender as Player
            val itemStack = player.itemInMainHand

            if (itemStack.isAir) {
                player.sendFormattedMessage(Component.text(itemIsAir))
                return@addSyntax
            }

            if (checkIsItem(itemStack)) {
                val item = itemStack.data!!.get<Item>(Item.key)!!

                if (item.hasTrait(lastTrait))
                    item.removeTrait(lastTrait)

                item.addTrait(lastTrait.primaryConstructor!!.call(*values.toTypedArray()))

                player.itemInMainHand = item.renderItem(player.itemInMainHand.amount.coerceIn(1, Byte.MAX_VALUE))

                player.sendFormattedMessage(traitAdded, processTraitName(lastTrait.simpleName!!))
            } else
                player.sendFormattedMessage(Component.text(requireFormattedItem))

        }
    }

}