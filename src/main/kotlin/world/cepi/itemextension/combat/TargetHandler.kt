package world.cepi.itemextension.combat

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Entity
import net.minestom.server.entity.LivingEntity
import net.minestom.server.entity.Metadata
import net.minestom.server.entity.Player
import net.minestom.server.item.ItemStack
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket
import net.minestom.server.utils.PacketUtils
import net.minestom.server.utils.Vector
import net.minestom.server.utils.time.TimeUnit
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.checkIsItem
import world.cepi.itemextension.item.module
import world.cepi.itemextension.item.traits.list.attacks.AttackTrait
import world.cepi.kstom.item.get
import world.cepi.kstom.raycast.HitType
import world.cepi.kstom.raycast.RayCast.castRay
import java.util.*
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or

object TargetHandler {
    private val targets = mutableMapOf<Player, LivingEntity>()

    private const val checkTarget = 1L
    private val checkTargetTime = TimeUnit.SECOND

    fun register() {
        MinecraftServer.getSchedulerManager().buildTask {
            MinecraftServer.getConnectionManager().onlinePlayers.forEach {
                val itemInHand = it.getItemInHand(Player.Hand.MAIN)
                val itemInOffHand = it.getItemInHand(Player.Hand.OFF)

                if((canTarget(itemInHand) || canTarget(itemInOffHand)) && it.instance != null) {
                    // TODO: Base max distance upon the item
                    //val result = castRay(it.instance!!, it, it.position.x, it.position.y + it.eyeHeight, it.position.z, it.position.direction.x, it.position.direction.y, it.position.direction.z, 100.0, 0.25, { pos ->  it.instance!!.getBlockStateId(pos) == Block.AIR.blockId }, {})
                    val result = castRay(it.instance!!, it, Vector(it.position.x, it.position.y + it.eyeHeight, it.position.z), it.position.direction, 100.0, 0.25)

                    if(result.hitType == HitType.ENTITY) {
                        val target = result.hitEntity!!
                        if(target is Player) {
                            return@forEach
                        }

                        if(hasTarget(it)) {
                            val currentTarget = targets[it]!!
                            if(currentTarget != target) {
                                targets[it] = target
                                PacketUtils.sendPacket(it, createMetadataPacket(currentTarget, false))
                                PacketUtils.sendPacket(it, createMetadataPacket(target, true))
                            }
                        } else {
                            targets[it] = target
                            PacketUtils.sendPacket(it, createMetadataPacket(target, true))
                        }
                    } else if(hasTarget(it)) {
                        PacketUtils.sendPacket(it, createMetadataPacket(targets[it]!!, false))
                        targets.remove(it)
                    }
                } else if(hasTarget(it)) {
                    PacketUtils.sendPacket(it, createMetadataPacket(targets[it]!!, false))
                    targets.remove(it)
                }
            }
        }.repeat(checkTarget, checkTargetTime).schedule()
    }

    fun getTarget(player: Player): LivingEntity? {
        return targets[player]
    }

    fun hasTarget(player: Player): Boolean {
        return targets.containsKey(player)
    }

    private fun canTarget(item: ItemStack): Boolean {
        if(checkIsItem(item)) {
            val cepiItem: Item = item.meta.get(Item.key, module)!!

            return cepiItem.traits.filterIsInstance<AttackTrait>().any { it.attack.requiresTarget } // Check if any attack of an item requires a target
        }

        return false
    }


    // All of the seemingly random constants here are a result of reverse engineering Minestom's metadata system
    private fun createMetadataPacket(target: Entity, glow: Boolean): EntityMetaDataPacket {
        // Clone the metadata packet to prevent sync issues
        val clonePacket = EntityMetaDataPacket()
        clonePacket.entityId = target.entityId

        val currentEntry = target.metadataPacket.entries.firstOrNull()
        val mask = if (currentEntry != null && currentEntry.metaValue.value is Byte) {
            modifyMask(0x40, glow, currentEntry.metaValue.value as Byte) // 0x40 being the bit for if an entity is glowing
        } else {
            modifyMask(0x40, glow) // Assume that no meta is present, thereby the current mask is 0
        }

        clonePacket.entries = Collections.singleton(Metadata.Entry(0, Metadata.Byte(mask))) as Collection<Metadata.Entry<*>>? // Weird mismatch, seems redundant but for some unknown reason it's required
        return clonePacket
    }

    private fun modifyMask(bit: Byte, value: Boolean, currentMask: Byte = 0): Byte {
        val currentValue = (currentMask and bit) == bit // Check if the currentMask "contains" the bit we want to set through bitwise operations.
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
