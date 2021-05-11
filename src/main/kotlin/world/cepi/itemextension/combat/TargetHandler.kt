package world.cepi.itemextension.combat

import net.minestom.server.entity.Entity
import net.minestom.server.entity.LivingEntity
import net.minestom.server.entity.Metadata
import net.minestom.server.entity.Player
import net.minestom.server.item.ItemStack
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket
import net.minestom.server.utils.PacketUtils
import net.minestom.server.utils.time.TimeUnit
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.checkIsItem
import world.cepi.itemextension.item.module
import world.cepi.itemextension.item.traits.list.attacks.AttackTrait
import world.cepi.kstom.Manager
import world.cepi.kstom.item.get
import world.cepi.kstom.raycast.HitType
import world.cepi.kstom.raycast.RayCast.castRay
import java.util.*
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or

object TargetHandler {
    private val targets = mutableMapOf<Player, LivingEntity>()

    private const val checkTarget = 5L
    private val checkTargetTime = TimeUnit.TICK

    fun register() = Manager.scheduler.buildTask {
        Manager.connection.onlinePlayers.forEach {
            val itemInHand = it.itemInMainHand
            val itemInOffHand = it.itemInOffHand

            // Check if the item supports the targeting system
            if ((canTarget(itemInHand) || canTarget(itemInOffHand)) && it.instance != null) {

                // Cast a ray
                // TODO: Base max distance upon the item
                val result = castRay(
                    it.instance!!,
                    it,
                    it.position.clone().add(.0, it.eyeHeight, .0).toVector(),
                    it.position.direction,
                    100.0,
                    0.25
                )

                // Check if the ray has hit an entity
                if (result.hitType == HitType.ENTITY) {

                    val target = result.hitEntity!!

                    // Ignore if the entity hit is a player
                    if (target is Player) {
                        return@forEach
                    }

                    if (hasTarget(it)) { // Check if the player already has a target
                        val currentTarget = targets[it]!!
                        if (currentTarget != target) { // Switch target and update the entities' glow
                            targets[it] = target
                            PacketUtils.sendPacket(it, createMetadataPacket(currentTarget, false))
                            PacketUtils.sendPacket(it, createMetadataPacket(target, true))
                        }
                    } else { // Set the target
                        targets[it] = target
                        PacketUtils.sendPacket(it, createMetadataPacket(target, true))
                    }

                } else if (hasTarget(it)) { // Remove target if no entity is hit
                    PacketUtils.sendPacket(it, createMetadataPacket(targets[it]!!, false))
                    targets.remove(it)
                }
            } else if (hasTarget(it)) { // Remove target if the player swaps to an item which does not have targeting support
                PacketUtils.sendPacket(it, createMetadataPacket(targets[it]!!, false))
                targets.remove(it)
            }
        }
    }.repeat(checkTarget, checkTargetTime).schedule()

    /**
     * Get the [LivingEntity] that a [Player] is targeting
     *
     * @param player The [Player] which is targeting something
     * @return a [LivingEntity] that is being targeted
     */
    fun getTarget(player: Player): LivingEntity? =
        targets[player]

    /**
     * Check if a [Player] has a target
     *
     * @param player [Player] to check
     */
    fun hasTarget(player: Player): Boolean =
        targets.containsKey(player)

    private fun canTarget(item: ItemStack): Boolean {

        if (!checkIsItem(item)) return false

        val cepiItem: Item = item.meta.get(Item.key, module)!!

        // Check if any attack of an item requires a target
        return cepiItem.traits.filterIsInstance<AttackTrait>().any { it.attack.requiresTarget }

    }


    // All of the seemingly random constants here are a result of reverse engineering Minestom's metadata system
    private fun createMetadataPacket(target: Entity, glow: Boolean): EntityMetaDataPacket {
        // Clone the metadata packet to prevent sync issues
        val clonePacket = EntityMetaDataPacket()
        clonePacket.entityId = target.entityId

        val currentEntry = target.metadataPacket.entries.firstOrNull()

        // If no meta is present, the mask should be 0
        val mask = modifyMask(0x40, glow, currentEntry?.metaValue?.value as? Byte ?: 0)

        // Weird mismatch, seems redundant but for some unknown reason it's required
        clonePacket.entries = Collections.singleton(Metadata.Entry(0, Metadata.Byte(mask))) as Collection<Metadata.Entry<*>>?
        return clonePacket
    }

    private fun modifyMask(bit: Byte, value: Boolean, currentMask: Byte = 0): Byte {

        // Check if the currentMask "contains" the bit we want to set through bitwise operations.
        val currentValue = (currentMask and bit) == bit
        if (currentValue == value) {
            return currentMask
        }

        return if (value) {
            currentMask or bit // Add the bit to the mask
        } else {
            currentMask and bit.inv() // Remove the bit from the mask by adding the compliment of the bit
        }
    }
}
