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
import world.cepi.itemextension.item.traits.list.actions.ActionTrait.Companion.itemNode
import world.cepi.itemextension.stats.StatsHandler
import world.cepi.itemextension.trading.TradeCommand
import world.cepi.kstom.Manager
import world.cepi.kstom.event.listenOnly
import world.cepi.kstom.util.log
import world.cepi.kstom.util.node
import world.cepi.mob.mob.Mob

/** Extension wrapper for Minestom. */
class ItemExtension : Extension() {
    override fun initialize(): LoadStatus {

        with(node) {
            listenOnly(CombatHandler::registerDamageByEntity)
            listenOnly(CombatHandler::registerGenericDamage)
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
        TradeCommand.register()

        log.info("[ItemExtension] has been enabled!")

        return LoadStatus.SUCCESS

    }

    override fun terminate() {

        // TODO unregister combat handler

        ItemCommand.unregister()
        ClearCommand.unregister()
        GiveCommand.unregister()
        HealCommand.unregister()
        NBTCommand.unregister()
        TradeCommand.unregister()

        log.info("[ItemExtension] has been disabled!")
    }

}