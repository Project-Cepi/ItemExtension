package world.cepi.itemextension.trading

import com.mattworzala.canvas.extra.mask
import com.mattworzala.canvas.fragment
import net.minestom.server.entity.Player
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.item.metadata.PlayerHeadMeta

internal object TradeUI {

    fun mainScreen(source: Player, target: Player) = fragment(9, 6) {

        item(2,
            ItemStack.of(Material.PLAYER_HEAD)
                .withMeta(PlayerHeadMeta::class.java) {
                    it.playerSkin(source.skin)
                }
        )

        item(6,
            ItemStack.of(Material.PLAYER_HEAD)
                .withMeta(PlayerHeadMeta::class.java) {
                    it.playerSkin(target.skin)
                }
        )

        mask {
            pattern = """
               110111011
               100010001
               100010001
               100010001
               100010001
               110111011
            """.trimIndent()

            fill(
                ItemStack.of(Material.GRAY_STAINED_GLASS_PANE, 1)
                    .withMeta {
                        it.customModelData(1)
                    }
            )
        }
    }

}