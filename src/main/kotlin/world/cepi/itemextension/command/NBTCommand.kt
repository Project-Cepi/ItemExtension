package world.cepi.itemextension.command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import world.cepi.kstom.command.arguments.literal
import world.cepi.kstom.command.kommand.Kommand

internal object NBTCommand : Kommand({

    val get by literal
    val set by literal

    val nbt = ArgumentType.NbtCompound("nbt")
    val material = ArgumentType.ItemStack("material").setDefaultValue(
        ItemStack.of(Material.PAPER)
    )

    syntax(get) {
        val item = player.itemInMainHand

        player.sendMessage(
            Component.text(item.meta.toSNBT(), NamedTextColor.GRAY)
                .hoverEvent(HoverEvent.showText(Component.text("Click to copy", NamedTextColor.YELLOW)))
                .clickEvent(ClickEvent.copyToClipboard(item.meta.toSNBT()))
        )
    }

    syntax(set, nbt, material) {
        player.itemInMainHand = ItemStack.fromNBT(
            context[material].material,
            context[nbt]
        )
    }

}, "nbt")