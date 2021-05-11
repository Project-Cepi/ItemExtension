package world.cepi.itemextension.command

import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player
import world.cepi.itemextension.command.itemcommand.loaders.ConditionLoader

object ClearCommand : Command("clear") {

    init {
        ConditionLoader.register(this)

        setDefaultExecutor { commandSender, _ ->
            val player = commandSender as Player
            player.inventory.clear()
        }

        addSyntax({ _, _ ->

        }, one, two, three)
    }

}