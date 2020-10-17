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
import kotlin.reflect.KClassifier
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters

class ItemCommand : Command("item") {

    private val requireFormattedItem = "You are not holding a formatted Item in your hand! Use /item create first."
    private val itemIsAir = "You don't have an item in your hand! Please get one first!"
    private val requireNonFormattedItem = "This item is already a Cepi item!"
    private val usage = "Usage"
    private val onlyPlayers = "This command is only available for players!"
    private val invalidActions = "Invalid action! Actions include"

    init {

        val actions = ArgumentType.Word("action").from("create", "reset", "set", "remove")
        val create = ArgumentType.Word("action").from("create")
        val reset = ArgumentType.Word("action").from("reset")
        val set = ArgumentType.Word("action").from("set")
        val remove = ArgumentType.Word("action").from("remove")

        setDefaultExecutor { commandSender, _ ->
            commandSender.sendMessage("$usage: /item (create, reset, set, remove) <params>")
        }

        val traitList = ArgumentType.Word("trait").from(*Traits.values().map { it.name.toLowerCase() }.toTypedArray())

        val traits = mapOf(*Traits.values()
            .map { it.clazz to ArgumentType.Word("trait").from(it.name.toLowerCase()) }
            .toTypedArray())

        setCondition { sender ->
            if (!sender.isPlayer) {
                sender.sendMessage(onlyPlayers)
                false
            } else true
        }

        setArgumentCallback({ commandSender, _, _ ->
            commandSender.sendMessage("$invalidActions <reset, create, set, and remove>.")
        }, actions)

        setArgumentCallback({ commandSender, _, _ -> commandSender.sendMessage("Invalid trait!") }, traitList)

        addSyntax({ commandSender, args -> singleAction(commandSender, args) }, create)
        addSyntax({ commandSender, args -> singleAction(commandSender, args) }, reset)

        addSyntax({ commandSender, args -> actionWithTrait(commandSender, args) }, remove, traitList)

        traits.forEach traitLoop@ { (trait, traitArg) ->
            // We will be using this constructor later to get its arguments
            val constructor = trait.primaryConstructor

            // Defined for the constructor parameter scanner
            val map = hashMapOf<KClassifier, Argument<*>>()

            constructor?.valueParameters?.forEach { kParam ->
                when (kParam.type.classifier) {

                    String::class -> map[kParam.type.classifier!!] = ArgumentType.String(kParam.name)
                    Int::class -> map[kParam.type.classifier!!] = ArgumentType.Integer(kParam.name)
                    else -> return@traitLoop

                }
            }

            map.values.forEach {
                setArgumentCallback({ commandSender, _, _ -> commandSender.sendMessage("Invalid trait argument!") }, it)
            }

            addSyntax({ commandSender, arguments ->
                val values = map.map { entry -> arguments.getObject(entry.value.id) }

                val player = commandSender as Player
                val itemStack = player.itemInMainHand

                if (itemStack.material == Material.AIR) {
                    player.sendMessage(itemIsAir)
                    return@addSyntax
                }

                if (checkIsCepiItem(itemStack)) {
                    val item = itemStack.data.get<Item>(Item.key)

                    if (item.hasTrait(trait))
                        item.removeTrait(trait)

                    item.addTrait(trait.primaryConstructor!!.call(*values.toTypedArray()))

                    player.itemInMainHand = item.renderItem(player.itemInMainHand.amount)
                } else
                    player.sendMessage(requireFormattedItem)
            }, set, traitArg, *map.values.toTypedArray())

        }
    }

    private fun singleAction(commandSender: CommandSender, args: Arguments) {
        val player = commandSender as Player
        val itemStack = player.itemInMainHand

        if (itemStack.material == Material.AIR) {
            player.sendMessage(itemIsAir)
            return
        }

        val isCepiItem = checkIsCepiItem(itemStack)
        when (args.getWord("action")) {
            "create" -> {
                if (!isCepiItem) {
                    val item = Item()
                    item.addTrait(MaterialTrait(itemStack.material, itemStack.customModelData))
                    player.itemInMainHand = item.renderItem(itemStack.amount)
                    player.sendMessage("Item Created!")
                } else
                    player.sendMessage(requireNonFormattedItem)
            }
            "reset" -> {
                if (isCepiItem) {
                    val item = itemStack.data.get<Item>(Item.key)
                    item.traits.removeAll { true }
                    player.itemInMainHand = item.renderItem(itemStack.amount)
                    player.sendMessage("Item Reset!")
                } else
                    player.sendMessage(requireFormattedItem)
            }
        }
    }

    private fun actionWithTrait(commandSender: CommandSender, args: Arguments) {
        val player = commandSender as Player
        val itemStack = player.itemInMainHand

        if (itemStack.material == Material.AIR) {
            player.sendMessage(itemIsAir)
            return
        }

        val isCepiItem = checkIsCepiItem(itemStack)

        if (isCepiItem) {
            val trait = Traits.values().first { it.name.equals(args.getWord("traits"), ignoreCase = true) }

            val item = itemStack.data.get<Item>(Item.key)

            if (item.hasTrait(trait.clazz)) {
                item.removeTrait(trait.clazz)
                player.sendMessage("Trait removed!")
            } else
                player.sendMessage("This trait isn't in this item!")
        } else
            player.sendMessage(requireFormattedItem)
    }

    private fun checkIsCepiItem(itemStack: ItemStack): Boolean {
        // data must be initialized for an itemStack
        if (itemStack.data == null) itemStack.data = DataImpl()

        return itemStack.data.hasKey(Item.key)
    }

}