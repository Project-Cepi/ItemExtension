package world.cepi.itemextension.command

import net.minestom.server.chat.ChatColor
import net.minestom.server.command.CommandSender
import net.minestom.server.entity.Player

fun CommandSender.sendFormattedMessage(message: String) {
    if (this is Player)
        this.sendMessage(ChatColor.DARK_GRAY + ChatColor.BOLD + "| " + ChatColor.RESET + ChatColor.GRAY + message)
    else
        this.sendMessage(message)
}

const val requireFormattedItem = "You are not holding a formatted Item in your hand! Use /item create first."
const val itemIsAir = "You don't have an item in your hand! Please get one first!"
const val requireNonFormattedItem = "This item is already a Cepi item!"
const val usage = "Usage"
const val onlyPlayers = "This command is only available for players!"
const val traitAdded = "Trait added!"
const val traitRemoved = "Trait removed!"
const val invalidTraitArgument = "Invalid trait argument!"
const val itemRendered = "Item Rendered!"
const val traitNotFound = "This trait is not in this item!"
const val itemCreated = "Item created!"
const val itemReset = "Item reset!"