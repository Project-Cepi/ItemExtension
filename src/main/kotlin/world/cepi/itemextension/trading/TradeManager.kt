package world.cepi.itemextension.trading

import net.minestom.server.entity.Player

object TradeManager {

    /**
     * Opens a trade between two players
     *
     * @param requester Who requested the trade
     * @param requestee Who accepted the trade request
     */
    fun openTrade(requester: Player, requestee: Player) {
        val ui = TradeUI.mainScreen(requester, requestee)

        ui.render(requester)
        ui.render(requestee)
    }

}