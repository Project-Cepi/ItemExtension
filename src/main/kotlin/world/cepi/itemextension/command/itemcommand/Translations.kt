package world.cepi.itemextension.command.itemcommand

import net.minestom.server.chat.ChatColor
import net.minestom.server.command.CommandSender
import net.minestom.server.entity.Player

/**
 * Sends a formatted message to the corresponding sender.
 *
 * @param message The origin message, usually grabbed from the list of translations
 * @param params The replacers, usually used to replace a placeholder in a translation message
 */
fun CommandSender.sendFormattedMessage(message: String, vararg params: String = arrayOf("")) {

    var parsedMessage = message
    params.forEachIndexed { index, item ->
        parsedMessage = parsedMessage.replaceFirst("%${index + 1}", item)
    }

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