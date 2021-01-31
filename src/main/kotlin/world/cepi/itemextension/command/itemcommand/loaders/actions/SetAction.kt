package world.cepi.itemextension.command.itemcommand.loaders.actions

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.command.builder.arguments.minecraft.ArgumentItemStack
import net.minestom.server.entity.Player
import net.minestom.server.item.Material
import world.cepi.itemextension.command.itemcommand.*
import world.cepi.itemextension.command.itemcommand.loaders.ItemCommandLoader
import world.cepi.itemextension.command.itemcommand.loaders.processTraitName
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.checkIsItem
import world.cepi.itemextension.item.traits.Trait
import world.cepi.itemextension.item.traits.TraitRefrenceList
import world.cepi.itemextension.item.traits.list.ItemTrait
import world.cepi.kstom.addSyntax
import world.cepi.kstom.arguments.argumentsFromConstructor
import world.cepi.kstom.arguments.asSubcommand
import world.cepi.kstom.setArgumentCallback
import kotlin.reflect.KClass
import kotlin.reflect.full.*

// TODO break down and organize
object SetAction : ItemCommandLoader {

    val set = "set".asSubcommand()

    val traits = listOf(*ItemTrait.classList
            .map { it }
            .toTypedArray())

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
    private fun defineSubTraits(traitContainer: KClass<out Trait>): List<KClass<out Trait>> {
        val companionClass = traitContainer.companionObjectInstance ?: return emptyList()
        if (companionClass !is TraitRefrenceList) return emptyList()

        return (traitContainer.companionObjectInstance as TraitRefrenceList).classList.toList()

    }

    private fun generateCommand(command: Command, vararg traits: KClass<out Trait>) {

        val trait = traits.last()

        // We will be using this constructor later to get its arguments
        val constructor = trait.primaryConstructor ?: return

        val constructorArguments = argumentsFromConstructor(constructor)

        if (constructorArguments.isEmpty()) return

        constructorArguments.forEach {
            command.setArgumentCallback(it)
            { commandSender -> commandSender.sendFormattedMessage(invalidTraitArgument) }

        }

        val traitArgs = traits.map { ArgumentType.Word(it.simpleName!!)
            .from(processTraitName(it.simpleName!!)) }

        // TODO subtrait functionality
        command.addSyntax(set, *traitArgs.toTypedArray(), *constructorArguments.toTypedArray()) { commandSender, arguments ->
            val values = constructorArguments.map { entry ->
                return@map when(entry) {
                    is ArgumentEnum ->
                        entry.enumArray.first { it.name.equals(arguments.getString(entry.id), ignoreCase = true) }
                    is ArgumentItemStack ->
                        arguments.getItemStack(entry.id).material
                    else -> arguments.getObject(entry.id)
                }
            }

            val player = commandSender as Player
            val itemStack = player.itemInMainHand

            if (itemStack.material == Material.AIR) {
                player.sendFormattedMessage(itemIsAir)
                return@addSyntax
            }

            if (checkIsItem(itemStack)) {
                val item = itemStack.data!!.get<Item>(Item.key)!!

                if (item.hasTrait(trait))
                    item.removeTrait(trait)

                item.addTrait(trait.primaryConstructor!!.call(*values.toTypedArray()))

                player.itemInMainHand = item.renderItem(player.itemInMainHand.amount)

                player.sendFormattedMessage(traitAdded, processTraitName(trait.simpleName!!))
            } else
                player.sendFormattedMessage(requireFormattedItem)

        }
    }

}