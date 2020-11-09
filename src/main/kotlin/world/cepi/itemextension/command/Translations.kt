package world.cepi.itemextension.command

import net.minestom.server.chat.ChatColor
import net.minestom.server.command.CommandSender
import net.minestom.server.entity.Player

fun CommandSender.sendFormattedMessage(message: String, vararg params: String = arrayOf("")) {

    var parsedMessage = message
    params.forEachIndexed { item, index -> parsedMessage = parsedMessage.replace("%${index + 1}".toRegex(), item.toString()) }

    if (this is Player)
        this.sendMessage(ChatColor.DARK_GRAY + ChatColor.BOLD + "| " + ChatColor.RESET + ChatColor.GRAY + parsedMessage)
    else
        this.sendMessage(parsedMessage)
}

const val requireFormattedItem = "You are not holding a formatted Item in your hand! Use /item create first."
const val itemIsAir = "You don't have an item in your hand! Please get one first!"
const val requireNonFormattedItem = "This item is already an item!"
const val usage = "Usage"
const val onlyPlayers = "This command is only available for players!"
val traitAdded = "Trait ${ChatColor.BLUE}%1${ChatColor.GRAY} added!"
val traitRemoved = "Trait ${ChatColor.BLUE}%1${ChatColor.GRAY} removed!"
const val invalidTraitArgument = "Invalid trait argument!"
const val itemRendered = "Item rendered!"
const val traitNotFound = "This trait is not in this item!"
const val itemCreated = "Item created!"
const val itemReset = "Item reset!"