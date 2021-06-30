package world.cepi.itemextension

import net.minestom.server.extensions.Extension
import world.cepi.itemextension.combat.TargetHandler
import world.cepi.itemextension.combat.events.CombatHandler
import world.cepi.itemextension.combat.events.DeathHandler
import world.cepi.itemextension.combat.events.DisableDropping
import world.cepi.itemextension.combat.events.NoVoidHandler
import world.cepi.itemextension.command.ClearCommand
import world.cepi.itemextension.command.GiveCommand
import world.cepi.itemextension.command.HealCommand
import world.cepi.itemextension.command.NBTCommand
import world.cepi.itemextension.command.itemcommand.ItemCommand
import world.cepi.itemextension.item.traits.list.attacks.AttackTrait.Companion.itemNode
import world.cepi.itemextension.stats.StatsHandler
import world.cepi.kstom.command.register
import world.cepi.kstom.command.unregister
import world.cepi.kstom.event.listenOnly
import world.cepi.kstom.extension.ExtensionCompanion

/** Extension wrapper for Minestom. */
class ItemExtension : Extension() {
    override fun initialize() {

        with(eventNode) {
            listenOnly(CombatHandler::register)
            listenOnly(DeathHandler::register)
            listenOnly(NoVoidHandler::register)
            listenOnly(DisableDropping::register)
            addChild(itemNode)
            StatsHandler.register(this)
        }

        TargetHandler.register()

        ItemCommand.register()
        ClearCommand.register()
        GiveCommand.register()
        HealCommand.register()
        NBTCommand.register()

        logger.info("[ItemExtension] has been enabled!")

    }

    override fun terminate() {

        // TODO unregister combat handler

        ItemCommand.unregister()
        ClearCommand.unregister()
        GiveCommand.unregister()
        HealCommand.unregister()
        NBTCommand.unregister()

        logger.info("[ItemExtension] has been disabled!")
    }

    companion object: ExtensionCompanion<ItemExtension>(Any())

}