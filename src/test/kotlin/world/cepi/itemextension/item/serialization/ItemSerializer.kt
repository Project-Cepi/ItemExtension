package world.cepi.itemextension.item.serialization

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import world.cepi.actions.ActionItem
import world.cepi.actions.list.FlingAction
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.cepiItem
import world.cepi.itemextension.item.traits.list.AttackSpeedTrait
import world.cepi.itemextension.item.traits.list.LevelTrait
import world.cepi.itemextension.item.traits.list.actions.PrimaryActionTrait
import java.time.Duration

class ItemSerializer {

    @Disabled("Class not found")
    fun `normal item serialization should turn back into an item safely`() {
        val item = cepiItem {
            +LevelTrait(5)
            +AttackSpeedTrait(Duration.ofMillis(10_000))
            +PrimaryActionTrait("Fling", true, ActionItem(FlingAction(1.0)))
        }


        val json = item.toJSON()

        val decodedJSON = Item.fromJSON(json)

        assertEquals(item, decodedJSON)

        assertTrue(decodedJSON.hasTrait<LevelTrait>())
        assertEquals(LevelTrait(5), decodedJSON.get<LevelTrait>())

        assertTrue(decodedJSON.hasTrait<PrimaryActionTrait>())
        assertEquals(PrimaryActionTrait("Fling", true, ActionItem(FlingAction(1.0))), decodedJSON.get<PrimaryActionTrait>())
    }

}