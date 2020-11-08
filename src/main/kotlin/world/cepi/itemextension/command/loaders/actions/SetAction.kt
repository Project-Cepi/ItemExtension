package world.cepi.itemextension.command.loaders.actions

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.Argument
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import net.minestom.server.item.Material
import world.cepi.itemextension.command.*
import world.cepi.itemextension.command.loaders.ItemCommandLoader
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.checkIsItem
import world.cepi.itemextension.item.traits.Traits
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters

// TODO break down and organize
object SetAction : ItemCommandLoader {

    override fun register(command: Command) {

        val set = ArgumentType.Word("set").from("set")

        val traits = mapOf(*Traits.values()
            .map { it.clazz to ArgumentType.Word(it.name.toLowerCase()).from(it.name.toLowerCase()) }
            .toTypedArray())

        traits.forEach traitLoop@{ (trait, traitArg) ->
            // We will be using this constructor later to get its arguments
            val constructor = trait.primaryConstructor!!

            val constructorArguments = defineArguments(constructor) ?: return@traitLoop

            if (constructorArguments.size == 0) return@traitLoop

            constructorArguments.values.forEach {
                command.setArgumentCallback(
                    { commandSender, _, _ -> commandSender.sendFormattedMessage(invalidTraitArgument) },
                    it
                )
            }

            command.addSyntax({ commandSender, arguments ->
                val values = constructorArguments.map { entry ->

                    if (entry.value is ArgumentEnum) {
                        val argumentEnum = entry.value as ArgumentEnum
                        return@map argumentEnum.enumArray.first { it.name == arguments.getString(entry.value.id) }
                    } else
                        return@map arguments.getObject(entry.value.id)
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

                    player.sendFormattedMessage(traitAdded)
                } else
                    player.sendFormattedMessage(requireFormattedItem)

            }, set, traitArg, *constructorArguments.values.toTypedArray())

        }
    }

    private fun defineArguments(constructor: KFunction<*>): LinkedHashMap<KClassifier, Argument<*>>? {

        val linkedMap = linkedMapOf<KClassifier, Argument<*>>()

        constructor.valueParameters.forEach { kParam ->

            when (kParam.type.classifier) {

                String::class -> linkedMap[kParam.type.classifier!!] =
                        ArgumentType.String(kParam.name!!)
                Int::class -> linkedMap[kParam.type.classifier!!] =
                        ArgumentType.Integer(kParam.name!!)
                else -> {
                    return null
                    // special types
                    // TODO truly check if its an Enum.
                    try {
                        val enumClz =
                                ((kParam.type.classifier) as KClass<*>).java.enumConstants as Array<Enum<*>>

                        val argumentEnum = ArgumentEnum(kParam.name!!).from(*enumClz.map { it.name }.toTypedArray())
                        argumentEnum.enumArray = enumClz

                        linkedMap[kParam.type.classifier!!] = argumentEnum
                    } catch (e: Exception) {
                        return null
                    }
                }

            }
        }
        return linkedMap
    }

}