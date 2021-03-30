package world.cepi.itemextension.command.itemcommand.loaders.actions

import kotlinx.serialization.Contextual
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.kyori.adventure.text.Component
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import world.cepi.itemextension.command.itemcommand.itemIsAir
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.checkIsItem
import world.cepi.itemextension.item.module
import world.cepi.kepi.messages.sendFormattedMessage
import world.cepi.kstom.command.addSyntax
import world.cepi.kstom.command.arguments.asSubcommand

object DataAction : Command("data") {

    @Contextual
    val format = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
        serializersModule = module
    }

    init {
        val get = "get".asSubcommand()

        val from = "from".asSubcommand()
        val json = ArgumentType.NbtCompound("json")

        addSyntax(get) { sender ->
            val player = sender as Player
            val itemStack = player.itemInMainHand

            if (itemStack.isAir) {
                player.sendFormattedMessage(Component.text(itemIsAir))
                return@addSyntax
            }

            if (checkIsItem(itemStack)) {
                println(format.encodeToString(itemStack.data!!.get<Item>(Item.key)))
            }
        }

        addSyntax(from, json) { sender, args ->
            val player = sender as Player

            val nbtData = args.get(json) // TODO process to json and convert
        }
    }
}