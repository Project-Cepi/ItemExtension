package world.cepi.itemextension.command.itemcommand

import net.minestom.server.command.CommandSender
import net.minestom.server.permission.Permission
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TranslationsKtTest {

    class MockCommandSender : CommandSender {

        var lastReceivedMessage: String? = null

        override fun getAllPermissions(): MutableCollection<Permission<Any>> {
            return mutableListOf()
        }

        override fun sendMessage(message: String) {
            lastReceivedMessage = message
        }

    }

    @Test
    fun `formatting a message should replace params`() {
        val commandSender = MockCommandSender()

        commandSender.sendFormattedMessage("Hello %1", "World")

        assertEquals(commandSender.lastReceivedMessage, "Hello World")

    }

}