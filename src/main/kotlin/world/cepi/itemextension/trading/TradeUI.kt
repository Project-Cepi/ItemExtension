package world.cepi.itemextension.trading

import com.mattworzala.canvas.extra.mask
import com.mattworzala.canvas.fragment
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.entity.Player
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.item.metadata.PlayerHeadMeta
import world.cepi.kstom.item.item

internal object TradeUI {

    fun mainScreen(source: Player, target: Player) = fragment(9, 6) {

        this.inventory.title = Component.text("Trade")
            .append(source.name.color(NamedTextColor.BLUE))
            .append(Component.text(" -> ", NamedTextColor.GRAY))
            .append(target.name.color(NamedTextColor.BLUE))

        item(2,
            ItemStack.of(Material.PLAYER_HEAD)
                .withDisplayName(source.name.color(NamedTextColor.BLUE).decoration(TextDecoration.ITALIC, false))
                .withMeta(PlayerHeadMeta::class.java) { head ->
                    source.skin?.let { head.playerSkin(it) }
                }

        )

        item(6,
            ItemStack.of(Material.PLAYER_HEAD)
                .withDisplayName(target.name.color(NamedTextColor.BLUE).decoration(TextDecoration.ITALIC, false))
                .withMeta(PlayerHeadMeta::class.java) { head ->
                    target.skin?.let { head.playerSkin(it) }
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
                item(Material.GRAY_STAINED_GLASS_PANE, 1) {
                    customModelData(1)
                }
            )
        }
    }

}