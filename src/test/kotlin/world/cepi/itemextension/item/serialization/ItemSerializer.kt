package world.cepi.itemextension.item.serialization

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.cepiItem
import world.cepi.itemextension.item.traits.list.AttackSpeedTrait
import world.cepi.itemextension.item.traits.list.LevelTrait
import world.cepi.itemextension.item.traits.list.actions.Action
import world.cepi.itemextension.item.traits.list.actions.PrimaryActionTrait

class ItemSerializer {

    @Test
    fun `normal item serialization should turn back into an item safely`() {
        val item = cepiItem {
            +LevelTrait(5)
            +AttackSpeedTrait(10.1)
            +PrimaryActionTrait(Action.Dash())
        }


        val json = item.toJSON()

        val decodedJSON = Item.fromJSON(json)

        assertEquals(item, decodedJSON)

        assertTrue(decodedJSON.hasTrait<LevelTrait>())
        assertEquals(LevelTrait(5), decodedJSON.get<LevelTrait>())

        assertTrue(decodedJSON.hasTrait<PrimaryActionTrait>())
        assertEquals(PrimaryActionTrait(Action.Dash()), decodedJSON.get<PrimaryActionTrait>())
    }

}