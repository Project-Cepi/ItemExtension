package world.cepi.itemextension

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.extensions.Extension
import world.cepi.itemextension.combat.events.*
import world.cepi.itemextension.command.ClearCommand
import world.cepi.itemextension.command.GiveCommand
import world.cepi.itemextension.command.itemcommand.ItemCommand
import world.cepi.itemextension.stats.StatsHandler

/** Extension wrapper for Minestom. */
object ItemExtension : Extension() {

    private val playerInitialization: (Player) -> Unit = { player ->
        HealthHandler.register(player)
        CombatHandler.register(player)
        DeathHandler.register(player)
        NoVoidHandler.register(player)
        DisableDropping.register(player)
        StatsHandler.register(player)

    }

    override fun initialize() {
        val connectionManager = MinecraftServer.getConnectionManager()
        connectionManager.addPlayerInitialization(playerInitialization)

        MinecraftServer.getCommandManager().register(ItemCommand)
        MinecraftServer.getCommandManager().register(ClearCommand)
        MinecraftServer.getCommandManager().register(GiveCommand)

        logger.info("[ItemExtension] has been enabled!")
    }

    override fun terminate() {

        val connectionManager = MinecraftServer.getConnectionManager()
        connectionManager.removePlayerInitialization(playerInitialization)

        MinecraftServer.getCommandManager().unregister(ItemCommand)
        MinecraftServer.getCommandManager().unregister(ClearCommand)
        MinecraftServer.getCommandManager().unregister(GiveCommand)

        logger.info("[ItemExtension] has been disabled!")
    }

}