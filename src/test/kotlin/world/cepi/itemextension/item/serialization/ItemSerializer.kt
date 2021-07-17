package world.cepi.itemextension.item.serialization

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import world.cepi.itemextension.command.itemcommand.loaders.actions.DataSubcommand
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.list.AttackSpeedTrait
import world.cepi.itemextension.item.traits.list.LevelTrait
import world.cepi.itemextension.item.traits.list.actions.Action
import world.cepi.itemextension.item.traits.list.actions.PrimaryActionTrait

class ItemSerializer {

    @Test
    fun `normal item serialization should turn back into an item safely`() {
        val item = Item().apply {
            put(LevelTrait(5))
            put(AttackSpeedTrait(10.1))
            put(PrimaryActionTrait(Action.Dash()))
        }

        val json = DataSubcommand.format.encodeToString(item)

        val decodedJSON = DataSubcommand.format.decodeFromString<Item>(json)

        assertEquals(item, decodedJSON)

        assertTrue(decodedJSON.hasTrait<LevelTrait>())
        assertEquals(LevelTrait(5), decodedJSON.get<LevelTrait>())

        assertTrue(decodedJSON.hasTrait<PrimaryActionTrait>())
        assertEquals(PrimaryActionTrait(Action.Dash()), decodedJSON.get<PrimaryActionTrait>())
    }

}