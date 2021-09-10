package world.cepi.itemextension.combat

import net.minestom.server.entity.Entity
import java.util.*

object ImmunityHandler {

    private const val cooldownMilliseconds = 200

    private val immunityHandler = WeakHashMap<Entity, Long>()

    fun triggerImmune(entity: Entity) {
        immunityHandler[entity] = System.currentTimeMillis()
    }

    fun isImmune(entity: Entity) =
        immunityHandler.containsKey(entity) && (System.currentTimeMillis() - (immunityHandler[entity] ?: 0L) < cooldownMilliseconds)
}