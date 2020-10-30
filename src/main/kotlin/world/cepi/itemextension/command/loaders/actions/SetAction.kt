package world.cepi.itemextension.command.loaders.actions

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.Argument
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import net.minestom.server.item.Material
import world.cepi.itemextension.command.ArgumentEnum
import world.cepi.itemextension.command.checkIsItem
import world.cepi.itemextension.command.itemIsAir
import world.cepi.itemextension.command.loaders.ItemCommandLoader
import world.cepi.itemextension.command.requireFormattedItem
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.Traits
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmName

// TODO break down and organize
object SetAction : ItemCommandLoader {

    override fun register(command: Command) {

        val set = ArgumentType.Word("set").from("set")

        val traits = mapOf(*Traits.values()
            .map { it.clazz to ArgumentType.Word(it.name.toLowerCase()).from(it.name.toLowerCase()) }
            .toTypedArray())

        traits.forEach traitLoop@{ (trait, traitArg) ->
            // We will be using this constructor later to get its arguments
            val constructor = trait.primaryConstructor

            // Defined for the constructor parameter scanner
            val constructurArguments = linkedMapOf<KClassifier, Argument<*>>()

            constructor?.valueParameters?.forEach { kParam ->

                when (kParam.type.classifier) {

                    String::class -> constructurArguments[kParam.type.classifier!!] = ArgumentType.String(kParam.name!!)
                    Int::class -> constructurArguments[kParam.type.classifier!!] = ArgumentType.Integer(kParam.name!!)
                    else -> {
                        // special types
                        // TODO truly check if its an Enum.

                        val enumClz = (Class.forName(((kParam.type.classifier) as KClass<*>).jvmName).enumConstants as Array<Enum<*>>)

                        val argumentEnum = ArgumentEnum(kParam.name!!).from(*enumClz.map { it.name }.toTypedArray())
                        argumentEnum.enumArray = enumClz

                        constructurArguments[kParam.type.classifier!!] = argumentEnum
                    }

                }
            }

            constructurArguments.values.forEach {
                command.setArgumentCallback({ commandSender, _, _ -> commandSender.sendMessage("Invalid trait argument!") }, it)
            }

            command.addSyntax({ commandSender, arguments ->
                val values = constructurArguments.map { entry ->

                    if (entry.value is ArgumentEnum) {
                        val argumentEnum = entry.value as ArgumentEnum
                        return@map argumentEnum.enumArray.first { it.name == arguments.getString(entry.value.id) }
                    } else {
                        return@map arguments.getObject(entry.value.id)
                    }
                }

                val player = commandSender as Player
                val itemStack = player.itemInMainHand.clone()

                if (itemStack.material == Material.AIR) {
                    player.sendMessage(itemIsAir)
                    return@addSyntax
                }

                if (checkIsItem(itemStack)) {
                    val item = itemStack.data!!.get<Item>(Item.key)!!

                    if (item.hasTrait(trait))
                        item.removeTrait(trait)

                    item.addTrait(trait.primaryConstructor!!.call(*values.toTypedArray()))

                    player.itemInMainHand = item.renderItem(player.itemInMainHand.amount)

                    player.sendMessage("Trait added!")
                } else
                    player.sendMessage(requireFormattedItem)
            }, set, traitArg, *constructurArguments.values.toTypedArray())

        }
    }
}