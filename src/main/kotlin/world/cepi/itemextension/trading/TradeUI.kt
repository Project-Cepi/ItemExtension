package world.cepi.itemextension.trading

import com.mattworzala.canvas.extra.mask
import com.mattworzala.canvas.fragment
import net.minestom.server.entity.Player
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

internal object TradeUI {

    fun mainScreen(source: Player, target: Player) = fragment(6, 9) {

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