package world.cepi.itemextension.combat

import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap
import net.minestom.server.entity.Entity

object ImmunityHandler {

    private const val cooldownMilliseconds = 500

    private val immunityHandler = Object2LongOpenHashMap<Entity>()

    fun triggerImmune(entity: Entity) {
        immunityHandler[entity] = System.currentTimeMillis()
    }

    fun isImmune(entity: Entity) =
        immunityHandler.containsKey(entity) && (System.currentTimeMillis() - immunityHandler.getLong(entity) < cooldownMilliseconds)
}