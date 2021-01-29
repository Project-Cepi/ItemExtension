package world.cepi.itemextension.item.traits.list.attributes

import net.minestom.server.attribute.AttributeOperation
import net.minestom.server.attribute.Attributes
import net.minestom.server.item.ItemStack
import net.minestom.server.item.attribute.AttributeSlot
import net.minestom.server.item.attribute.ItemAttribute
import world.cepi.itemextension.item.traits.list.AttributeTrait
import java.util.*

class SpeedAttributeTrait(override val value: Double): AttributeTrait() {

    override val name = "Speed"

    override fun task(item: ItemStack) {
        AttributeSlot.values().forEach {
            item.addAttribute(ItemAttribute(UUID.randomUUID(), "e", Attributes.MOVEMENT_SPEED, AttributeOperation.ADDITION, value, it))
        }
    }

}