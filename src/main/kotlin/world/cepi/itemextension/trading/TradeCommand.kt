package world.cepi.itemextension.trading

import world.cepi.kstom.command.arguments.ArgumentPlayer
import world.cepi.kstom.command.kommand.Kommand

object TradeCommand : Kommand({
    val playerArgument = ArgumentPlayer("player")

    syntax(playerArgument) {
        TradeManager.openTrade(player, !playerArgument)
    }
}, "trade")