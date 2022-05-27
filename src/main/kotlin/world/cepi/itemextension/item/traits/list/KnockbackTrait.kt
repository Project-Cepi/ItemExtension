package world.cepi.itemextension.item.traits.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minestom.server.item.ItemMeta
import net.minestom.server.tag.Tag
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.ItemTrait

@Serializable
@SerialName("knockback")
data class KnockbackTrait(val amount: Float): ItemTrait() {

    override val taskIndex = 0f

    override fun task(item: ItemMeta.Builder, originalItem: Item): Unit = with(item) {
        set(Tag.Float("knockback"), amount)
    }

}