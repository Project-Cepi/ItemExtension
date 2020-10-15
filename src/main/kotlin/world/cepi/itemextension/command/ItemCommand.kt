package world.cepi.itemextension.command

import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Arguments
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.Argument
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.data.DataImpl
import net.minestom.server.entity.Player
import net.minestom.server.item.Material
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.Traits
import world.cepi.itemextension.item.traits.list.MaterialTrait
import kotlin.reflect.KClassifier
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters


class ItemCommand : Command("item") {

    init {

        val actions = ArgumentType.Word("action").from("create", "reset", "set", "remove")
        val create = ArgumentType.Word("action").from("create")
        val reset = ArgumentType.Word("action").from("reset")
        val set = ArgumentType.Word("action").from("set")
        val remove = ArgumentType.Word("action").from("remove")

        setDefaultExecutor { commandSender, _ ->
            commandSender.sendMessage("Usage: /item (create, reset, set, remove) <params>")
        }

        val traitList = ArgumentType.Word("trait").from(*Traits.values().map { it.name.toLowerCase() }.toTypedArray())

        val traits = mapOf(*Traits.values().map { it to ArgumentType.Word("trait").from(it.name.toLowerCase()) }.toTypedArray())

        setCondition { sender ->
            if (!sender.isPlayer) {
                sender.sendMessage("The command is only available for players!")
                false
            } else true
        }

        setArgumentCallback({ commandSender, _, _ ->
            commandSender.sendMessage("Invalid action! Actions include <reset, create, set, and remove>.")
        }, actions)

        setArgumentCallback({ commandSender, _, _ ->
            commandSender.sendMessage("Invalid trait!")
        }, traitList)

        addSyntax({ commandSender, args -> singleAction(commandSender, args) }, create)
        addSyntax({ commandSender, args -> singleAction(commandSender, args) }, reset)

        addSyntax({ commandSender, args -> actionWithTrait(commandSender, args) }, remove, traitList)

        traits.forEach { (trait, traitArg) ->
            if (trait.clazz.primaryConstructor == null || trait.clazz.primaryConstructor!!.valueParameters.isEmpty()) {
                addSyntax({ commandSender, args -> actionWithTrait(commandSender, args) }, set, traitArg)
            }
        }

        traits.forEach { (trait, traitArgument) ->
            try {
                // We will be using this constructor later to get its arguments
                val constructor = trait.clazz.primaryConstructor ?: return@forEach

                // Defined for the constructor paramater scanner
                val map = hashMapOf<KClassifier, Argument<*>>()

                var validArguments = true

                constructor.valueParameters.forEach { kParam ->
                    when (kParam.type.classifier) {

                        String::class -> {
                            map[kParam.type.classifier!!] = ArgumentType.String(kParam.name)
                        }

                        else -> validArguments = false

                    }
                }

                if (validArguments) {

                    map.values.forEach {
                        setArgumentCallback({ commandSender, _, _ ->
                            commandSender.sendMessage("Invalid trait argument!")
                        }, it)
                    }

                    addSyntax({ commandSender, arguments ->
                        val values = map.map { arguments.getObject(it.value.id) }

                        val player = commandSender as Player
                        val itemStack = player.itemInMainHand

                        if (itemStack.material == Material.AIR) {
                            player.sendMessage("You don't have an item in your hand! Please get one first!")
                            return@addSyntax
                        }

                        // data must be initialized for an itemStack
                        if (itemStack.data == null) {
                            itemStack.data = DataImpl()
                        }

                        val isCepiItem = itemStack.data.get<Item>(Item.key) != null

                        if (isCepiItem) {
                            val item = itemStack.data.get<Item>(Item.key)

                            if (item.hasTrait(trait.clazz)) {
                                item.removeTrait(trait.clazz)
                            }

                            item.addTrait(trait.clazz.primaryConstructor!!.call(*values.toTypedArray()))

                            player.itemInMainHand = item.renderItem(player.itemInMainHand.amount)
                        } else {
                            player.sendMessage("You are not holding a formatted Item in your hand! Use /item create first.")
                        }

                    }, set, traitArgument, *map.values.toTypedArray())

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun singleAction(commandSender: CommandSender, args: Arguments) {
        val player = commandSender as Player
        val itemStack = player.itemInMainHand

        if (itemStack.material == Material.AIR) {
            player.sendMessage("You don't have an item in your hand! Please get one first!")
            return
        }

        // data must be initialized for an itemStack
        if (itemStack.data == null) {
            itemStack.data = DataImpl()
        }

        val isCepiItem = itemStack.data.get<Item>(Item.key) != null
        when (args.getWord("action")) {
            "create" -> {
                if (!isCepiItem) {
                    val item = Item()
                    item.addTrait(MaterialTrait(itemStack.material, itemStack.customModelData))
                    player.itemInMainHand = item.renderItem(itemStack.amount)
                    player.sendMessage("Item created!")
                } else {
                    player.sendMessage("This item is already a Cepi item! Try /item reset to get a blank slate.")
                }
            }
            "reset" -> {
                if (isCepiItem) {
                    val item = itemStack.data.get<Item>(Item.key)
                    item.traits.removeIf { it !is MaterialTrait }
                    player.itemInMainHand = item.renderItem()
                    player.sendMessage("Item Reset!")
                } else {
                    player.sendMessage("You are not holding a formatted Item in your hand! Use /item create first.")
                }
            }
        }
    }

    fun actionWithTrait(commandSender: CommandSender, args: Arguments) {
        val player = commandSender as Player
        val itemStack = player.itemInMainHand

        if (itemStack.material == Material.AIR) {
            player.sendMessage("You don't have an item in your hand! Please get one first!")
            return
        }

        // data must be initialized for an itemStack
        if (itemStack.data == null) {
            itemStack.data = DataImpl()
        }

        val isCepiItem = itemStack.data.get<Item>(Item.key) != null

        when (args.getWord("action")) {
            "remove" -> {
                if (isCepiItem) {
                    val trait = Traits.values().first { it.name.toLowerCase() == args.getWord("traits") }

                    val item = itemStack.data.get<Item>(Item.key)

                    if (item.hasTrait(trait.clazz)) {
                        item.removeTrait(trait.clazz)
                        player.sendMessage("Trait removed!")
                    } else {
                        player.sendMessage("This trait isn't in this item!")
                    }
                } else {
                    player.sendMessage("You are not holding a formatted Item in your hand! Use /item create first.")
                }
            }
            "set" -> {
                if (isCepiItem) {
                    val trait = Traits.values().first { it.name.toLowerCase() == args.getWord("traits") }

                    if (trait.clazz.primaryConstructor?.valueParameters == null) {

                        val item = itemStack.data.get<Item>(Item.key)

                        if (item.hasTrait(trait.clazz)) {
                            item.removeTrait(trait.clazz)
                        }
                    } else {
                        player.sendMessage("This trait requires more than one argument!")
                    }

                }
            }
        }
    }

}