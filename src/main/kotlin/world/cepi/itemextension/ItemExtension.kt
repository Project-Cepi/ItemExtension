package world.cepi.itemextension

import net.minestom.server.MinecraftServer
import net.minestom.server.extensions.Extension
import world.cepi.itemextension.events.CombatHandler
import world.cepi.itemextension.events.DeathHandler
import world.cepi.itemextension.events.HealthHandler
import world.cepi.itemextension.events.NoVoidHandler
import world.cepi.itemextension.command.ClearCommand
import world.cepi.itemextension.command.GiveCommand
import world.cepi.itemextension.command.itemcommand.ItemCommand
import world.cepi.itemextension.events.DisableDropping

/** Extension wrapper for Minestom. */
object ItemExtension : Extension() {

    override fun initialize() {
        val connectionManager = MinecraftServer.getConnectionManager()
        connectionManager.addPlayerInitialization {
            HealthHandler.register(it)
            CombatHandler.register(it)
            DeathHandler.register(it)
            NoVoidHandler.register(it)
            DisableDropping.load(it)
        }

        MinecraftServer.getCommandManager().register(ItemCommand())
        MinecraftServer.getCommandManager().register(ClearCommand())
        MinecraftServer.getCommandManager().register(GiveCommand())

        logger.info("[ItemExtension] has been enabled!")
    }

    override fun terminate() {
        logger.info("[ItemExtension] has been disabled!")
    }

}