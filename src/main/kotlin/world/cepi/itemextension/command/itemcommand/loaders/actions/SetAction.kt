package world.cepi.itemextension.command.itemcommand.loaders.actions

import net.minestom.server.chat.ChatColor
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.Argument
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.command.builder.arguments.minecraft.ArgumentItemStack
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.Player
import net.minestom.server.item.Material
import world.cepi.itemextension.command.itemcommand.*
import world.cepi.itemextension.command.itemcommand.loaders.ItemCommandLoader
import world.cepi.itemextension.command.itemcommand.loaders.processTraitName
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.checkIsItem
import world.cepi.itemextension.item.traits.Trait
import world.cepi.itemextension.item.traits.list.ItemTrait
import world.cepi.kstom.addSyntax
import world.cepi.kstom.arguments.asSubcommand
import world.cepi.kstom.setArgumentCallback
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.superclasses
import kotlin.reflect.full.valueParameters

// TODO break down and organize
object SetAction : ItemCommandLoader {

    override fun register(command: Command) {

        val set = "set".asSubcommand()

        val traits = mapOf(*ItemTrait.classList
                .map { it to ArgumentType.Word(it.simpleName!!)
                        .from(processTraitName(it.simpleName!!)) }
                .toTypedArray())

        traits.forEach traitLoop@{ (trait, traitArg) ->

            // We will be using this constructor later to get its arguments
            val constructor = trait.primaryConstructor ?: return@traitLoop

            val constructorArguments = defineArguments(constructor) ?: return@traitLoop

            if (constructorArguments.isEmpty()) return@traitLoop

            constructorArguments.forEach {
                command.setArgumentCallback(it)
                        { commandSender -> commandSender.sendFormattedMessage(invalidTraitArgument) }

            }

            command.addSyntax(set, traitArg, *constructorArguments.toTypedArray()) { commandSender, arguments ->
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

    /**
     * Gets the subtrait arguments from a trait container.
     * Must be a container that contains subtypes of ItemTraits.
     *
     * @param traitContainer The trait container to scan subtraits from.
     *
     * @return A list of subtraits from the trait container.
     */
    private fun defineSubTraits(traitContainer: KClass<out Trait>): List<ItemTrait> {
        traitContainer.companionObject?.superclasses?.get(0)

        return emptyList()

    }

    /**
     * Takes a trait that isn't a TraitContainer,
     * and generates primitive arguments for it based on its constructor
     *
     * @param constructor The constructor to use to generate args from.
     *
     * @return A organized hashmap of arguments and its classifier
     */
    private fun defineArguments(constructor: KFunction<*>): List<Argument<*>> =
        constructor.valueParameters.map { argumentFromClass(it.type.classifier!! as KClass<*>)!! }

    /**
     * Generates a Minestom argument based on the class
     *
     * @param clazz The class to base the argument off of
     *
     * @return An argument that matches with the class.
     *
     */
    private fun argumentFromClass(clazz: KClass<*>): Argument<*>? {

        if (clazz.simpleName == null) return null

        when (clazz) {
            String::class -> return ArgumentType.String(clazz.simpleName!!)
            Int::class -> return ArgumentType.Integer(clazz.simpleName!!)
            Double::class -> return ArgumentType.Double(clazz.simpleName!!)
            Long::class -> return ArgumentType.Long(clazz.simpleName!!)
            ChatColor::class -> return ArgumentType.Color(clazz.simpleName!!)
            EntityType::class -> return ArgumentType.EntityType(clazz.simpleName!!)
            Material::class -> return ArgumentType.ItemStack(clazz.simpleName!!)
            else -> {
                if (clazz.java.enumConstants == null) return null

                @Suppress("UNCHECKED_CAST") // We already check if the class is an enum or not.
                val enumClz =
                        clazz.java.enumConstants as Array<Enum<*>>

                return ArgumentEnum(clazz.simpleName!!, enumClz)
                        .from(*enumClz.map { it.name.toLowerCase() }.toTypedArray())
            }
        }
    }

}