package world.cepi.itemextension.item.traits.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minestom.server.item.ItemStackBuilder
import net.minestom.server.tag.Tag
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.ItemTrait
import world.cepi.kstom.item.withMeta

@Serializable
@SerialName("knockback")
data class KnockbackTrait(val amount: Float): ItemTrait() {

    override val taskIndex = 0f

    override fun task(item: ItemStackBuilder, originalItem: Item) {
        item.withMeta {
            set(Tag.Float("knockback"), amount)
        }
    }

}