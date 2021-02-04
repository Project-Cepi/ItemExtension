package world.cepi.itemextension.item

import net.minestom.server.entity.Player
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

class KItem(material: Material, amount: Byte = 1, damage: Int = 0): ItemStack(material, amount, damage) {

    val leftCallbacks: MutableList<(Player, Player.Hand) -> Boolean> = mutableListOf()
    val rightCallbacks: MutableList<(Player, Player.Hand) -> Boolean> = mutableListOf()

    override fun onLeftClick(player: Player, hand: Player.Hand) {
        leftCallbacks.forEach {
            it(player, hand)
        }
    }

    override fun onRightClick(player: Player, hand: Player.Hand) {
        rightCallbacks.forEach {
            it(player, hand)
        }
    }

}

fun item(material: Material = Material.PAPER, amount: Byte = 1, init: KItem.() -> Unit): KItem {
    val item = KItem(material, amount)
    item.init()
    return item
}