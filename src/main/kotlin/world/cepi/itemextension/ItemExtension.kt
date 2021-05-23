package world.cepi.itemextension

import net.minestom.server.entity.Player
import net.minestom.server.extensions.Extension
import world.cepi.itemextension.combat.TargetHandler
import world.cepi.itemextension.combat.events.*
import world.cepi.itemextension.command.ClearCommand
import world.cepi.itemextension.command.GiveCommand
import world.cepi.itemextension.command.itemcommand.ItemCommand
import world.cepi.itemextension.stats.StatsHandler
import world.cepi.kstom.Manager
import world.cepi.kstom.command.register
import world.cepi.kstom.command.unregister

/** Extension wrapper for Minestom. */
object ItemExtension : Extension() {

    private val playerInitialization: (Player) -> Unit = { player ->
        HealthHandler.register(player)
        DeathHandler.register(player)
        NoVoidHandler.register(player)
        DisableDropping.register(player)
        StatsHandler.register(player)
    }

    override fun initialize() {

        CombatHandler.register()

        Manager.connection.addPlayerInitialization(playerInitialization)

        TargetHandler.register()

        ItemCommand.register()
        ClearCommand.register()
        GiveCommand.register()

        logger.info("[ItemExtension] has been enabled!")
    }

    override fun terminate() {

        // TODO unregister combat handler

        Manager.connection.removePlayerInitialization(playerInitialization)

        ItemCommand.unregister()
        ClearCommand.unregister()
        GiveCommand.unregister()

        logger.info("[ItemExtension] has been disabled!")
    }

}