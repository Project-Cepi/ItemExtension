package world.cepi.itemextension.item.traits.list.attacks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minestom.server.item.ItemStackBuilder
import world.cepi.itemextension.item.Item
import world.cepi.kstom.item.withMeta

@Serializable
@SerialName("primary_attack")
data class PrimaryAttackTrait(override val attack: Attack): AttackTrait() {
    override val clickType = "Left"

    override fun task(item: ItemStackBuilder, originalItem: Item) {
        item.withMeta {
            set(Attack.generateTag<PrimaryAttackTrait>(), attack.name)
        }
    }
}