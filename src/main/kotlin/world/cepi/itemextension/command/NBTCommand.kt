package world.cepi.itemextension.command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import world.cepi.kstom.command.addSyntax
import world.cepi.kstom.command.arguments.literal

internal object NBTCommand : Command("nbt") {

    init {
        val get = "get".literal()
        val set = "set".literal()

        val nbt = ArgumentType.NbtCompound("nbt")
        val material = ArgumentType.ItemStack("material").setDefaultValue(
            ItemStack.of(Material.PAPER)
        )

        addSyntax(get) {
            val player = sender as Player

            val item = player.itemInMainHand

            player.sendMessage(
                Component.text(item.meta.toSNBT(), NamedTextColor.GRAY)
                    .hoverEvent(HoverEvent.showText(Component.text("Click to copy", NamedTextColor.YELLOW)))
                    .clickEvent(ClickEvent.copyToClipboard(item.meta.toSNBT()))
            )
        }

        addSyntax(set, nbt, material) {
            val player = sender as Player

            player.itemInMainHand = ItemStack.fromNBT(
                context[material].material,
                context[nbt]
            )
        }
    }

}