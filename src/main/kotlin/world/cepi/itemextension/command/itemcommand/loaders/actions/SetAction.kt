package world.cepi.itemextension.command.itemcommand.loaders.actions

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.Argument
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.command.builder.arguments.minecraft.ArgumentItemStack
import net.minestom.server.entity.Player
import net.minestom.server.item.Material
import world.cepi.itemextension.command.itemcommand.*
import world.cepi.itemextension.command.itemcommand.loaders.ItemCommandLoader
import world.cepi.itemextension.command.itemcommand.loaders.processTraitName
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.checkIsItem
import world.cepi.itemextension.item.traits.list.ItemTrait
import world.cepi.kstom.addSyntax
import world.cepi.kstom.arguments.asSubcommand
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor
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
                command.setArgumentCallback(
                        { commandSender, _, _ -> commandSender.sendFormattedMessage(invalidTraitArgument) },
                        it
                )
            }

            command.addSyntax(set, traitArg, *constructorArguments.toTypedArray()) { commandSender, arguments ->
                val values = constructorArguments.map { entry ->

                    if (entry is ArgumentEnum) {
                        return@map entry.enumArray.first { it.name.equals(arguments.getString(entry.id), ignoreCase = true) }
                    } else if (entry is ArgumentItemStack) {
                        return@map arguments.getItemStack(entry.id).material
                    } else
                        return@map arguments.getObject(entry.id)
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
     * Takes a trait that isn't a TraitContainer, and generates primitive arguments for it based on its constructor
     *
     * @param constructor The constructor to use to generate args from.
     *
     * @return A organized hashmap of arguments and its classifier
     */
    private fun defineArguments(constructor: KFunction<*>): List<Argument<*>>? {

        return constructor.valueParameters.map { kParam ->

            when (kParam.type.classifier) {

                String::class -> return@map ArgumentType.String(kParam.name!!)
                Int::class -> return@map ArgumentType.Integer(kParam.name!!)
                Double::class -> return@map ArgumentType.Double(kParam.name!!)
                Material::class -> return@map ArgumentType.ItemStack(kParam.name!!)
                else -> {
                    if (((kParam.type.classifier) as KClass<*>).java.enumConstants == null) return null

                    @Suppress("UNCHECKED_CAST") // We already check if the class is an enum or not.
                    val enumClz =
                            ((kParam.type.classifier) as KClass<*>).java.enumConstants as Array<Enum<*>>

                    val argumentEnum = ArgumentEnum(kParam.name!!, enumClz).from(*enumClz.map { it.name.toLowerCase() }.toTypedArray())

                    argumentEnum
                }

            }
        }
    }

}