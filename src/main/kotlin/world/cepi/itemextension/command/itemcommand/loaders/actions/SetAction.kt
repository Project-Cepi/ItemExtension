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
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmName

// TODO break down and organize
object SetAction : ItemCommandLoader {

    override fun register(command: Command) {

        val set = ArgumentType.Word("set").from("set")

        val traits = mapOf(*ItemTrait.classList
                .map { it to ArgumentType.Word(it.simpleName!!)
                        .from(processTraitName(it.simpleName!!)) }
                .toTypedArray())

        traits.forEach traitLoop@{ (trait, traitArg) ->

            // We will be using this constructor later to get its arguments
            val constructor = trait.primaryConstructor ?: return@traitLoop

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
                        return@map argumentEnum.enumArray.first { it.name.equals(arguments.getString(entry.value.id), ignoreCase = true) }
                    } else if (entry.value is ArgumentItemStack) {
                        return@map arguments.getItemStack(entry.value.id).material
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

                    player.sendFormattedMessage(traitAdded, trait.jvmName)
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
                Double::class -> linkedMap[kParam.type.classifier!!] =
                        ArgumentType.Double(kParam.name!!)
                Material::class -> linkedMap[kParam.type.classifier!!] =
                        ArgumentType.ItemStack(kParam.name!!)
                else -> {
                    if (((kParam.type.classifier) as KClass<*>).java.enumConstants == null) return null

                    @Suppress("UNCHECKED_CAST") // We already check if the class is an enum or not.
                    val enumClz =
                            ((kParam.type.classifier) as KClass<*>).java.enumConstants as Array<Enum<*>>

                    val argumentEnum = ArgumentEnum(kParam.name!!, enumClz).from(*enumClz.map { it.name.toLowerCase() }.toTypedArray())

                    linkedMap[kParam.type.classifier!!] = argumentEnum
                }

            }
        }
        return linkedMap
    }

}