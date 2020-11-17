package world.cepi.itemextension.command.itemcommand

import net.minestom.server.chat.ChatColor

/**
 * Adds a string to a color
 * @param string String to add to the corresponding color
 */
operator fun ChatColor.plus(string: String): String = this.toString() + string

/**
 * Adds a string to another color
 * @param color Color to add to the corresponding color
 */
operator fun ChatColor.plus(color: ChatColor): String = this.toString() + color