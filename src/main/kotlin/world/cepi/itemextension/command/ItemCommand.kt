package world.cepi.itemextension.command

import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Arguments
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.Argument
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.data.DataImpl
import net.minestom.server.entity.Player
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.Traits
import world.cepi.itemextension.item.traits.list.MaterialTrait
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmName

class ItemCommand : Command("item") {

    private val requireFormattedItem = "You are not holding a formatted Item in your hand! Use /item create first."
    private val itemIsAir = "You don't have an item in your hand! Please get one first!"
    private val requireNonFormattedItem = "This item is already a Cepi item!"
    private val usage = "Usage"
    private val onlyPlayers = "This command is only available for players!"
    private val invalidActions = "Invalid action! Actions include"

    init {

        val create = ArgumentType.Word("create").from("create")
        val reset = ArgumentType.Word("reset").from("reset")
        val set = ArgumentType.Word("set").from("set")
        val remove = ArgumentType.Word("remove").from("remove")
        val update = ArgumentType.Word("update").from("update")

        setDefaultExecutor { commandSender, _ ->
            commandSender.sendMessage("$usage: /item <create, reset, set, remove> <params>")
        }

        val traitList = ArgumentType.Word("trait")
            .from(*Traits.values().map { it.name.toLowerCase() }
                .toTypedArray())

        val traits = mapOf(*Traits.values()
            .map { it.clazz to ArgumentType.Word(it.name.toLowerCase()).from(it.name.toLowerCase()) }
            .toTypedArray())

        setCondition { sender ->
            if (!sender.isPlayer) {
                sender.sendMessage(onlyPlayers)
                false
            } else true
        }

        setArgumentCallback({ commandSender, _, _ -> commandSender.sendMessage("Invalid trait!") }, traitList)

        addSyntax({ commandSender, _ -> createAction(commandSender) }, create)
        addSyntax({ commandSender, _ -> resetAction(commandSender) }, reset)
        addSyntax({ commandSender, _ -> updateAction(commandSender) }, update)

        addSyntax({ commandSender, args -> actionWithTrait(commandSender, args) }, remove, traitList)

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

                        val enumClz = (Class.forName(((kParam.type.classifier) as KClass<*>).jvmName).enumConstants as Array<Enum<*>>)

                        val argumentEnum = ArgumentEnum(kParam.name!!).from(*enumClz.map { it.name }.toTypedArray())
                        argumentEnum.enumArray = enumClz

                        constructurArguments[kParam.type.classifier!!] = argumentEnum
                    }

                }
            }

            constructurArguments.values.forEach {
                setArgumentCallback({ commandSender, _, _ -> commandSender.sendMessage("Invalid trait argument!") }, it)
            }

            addSyntax({ commandSender, arguments ->
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

                if (checkIsCepiItem(itemStack)) {
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

    private fun createAction(commandSender: CommandSender) {
        val player = commandSender as Player
        val itemStack = player.itemInMainHand.clone()

        if (itemStack.material == Material.AIR) {
            player.sendMessage(itemIsAir)
            return
        }

        val isCepiItem = checkIsCepiItem(itemStack)
        if (!isCepiItem) {
            val item = Item()
            item.addTrait(MaterialTrait(itemStack.material, itemStack.customModelData))
            player.itemInMainHand = item.renderItem(itemStack.amount)
            player.sendMessage("Item Created!")
        } else
            player.sendMessage(requireNonFormattedItem)

    }

    private fun resetAction(commandSender: CommandSender) {
        val player = commandSender as Player
        val itemStack = player.itemInMainHand.clone()

        if (itemStack.material == Material.AIR) {
            player.sendMessage(itemIsAir)
            return
        }

        val isCepiItem = checkIsCepiItem(itemStack)

        if (isCepiItem) {
            val item = itemStack.data!!.get<Item>(Item.key)!!
            item.removeAllTraits()
            player.itemInMainHand = item.renderItem(itemStack.amount)
            player.sendMessage("Item Reset!")
        } else
            player.sendMessage(requireFormattedItem)
    }

    fun updateAction(commandSender: CommandSender) {
        val player = commandSender as Player
        val itemStack = player.itemInMainHand.clone()

        if (itemStack.material == Material.AIR) {
            player.sendMessage(itemIsAir)
            return
        }

        val isCepiItem = checkIsCepiItem(itemStack)

        if (isCepiItem) {
            val item = itemStack.data!!.get<Item>(Item.key)!!
            player.itemInMainHand = item.renderItem(itemStack.amount)
            player.sendMessage("Item Rendered!!")
        } else
            player.sendMessage(requireFormattedItem)
    }

    private fun actionWithTrait(commandSender: CommandSender, args: Arguments) {
        val player = commandSender as Player
        val itemStack = player.itemInMainHand.clone()

        if (itemStack.material == Material.AIR) {
            player.sendMessage(itemIsAir)
            return
        }

        val isCepiItem = checkIsCepiItem(itemStack)

        if (isCepiItem) {
            val trait = Traits.values().first { it.name.equals(args.getWord("trait"), ignoreCase = true) }

            val item = itemStack.data!!.get<Item>(Item.key)!!

            if (item.hasTrait(trait.clazz)) {
                item.removeTrait(trait.clazz)
                player.sendMessage("Trait removed!")
                player.itemInMainHand = item.renderItem(player.itemInMainHand.amount)
            } else
                player.sendMessage("This trait isn't in this item!")
        } else
            player.sendMessage(requireFormattedItem)
    }

    private fun checkIsCepiItem(itemStack: ItemStack): Boolean {
        // data must be initialized for an itemStack
        if (itemStack.data == null) itemStack.data = DataImpl()

        return itemStack.data!!.hasKey(Item.key)
    }

}