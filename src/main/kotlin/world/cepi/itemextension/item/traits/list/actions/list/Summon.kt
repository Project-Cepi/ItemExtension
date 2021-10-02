package world.cepi.itemextension.item.traits.list.actions.list

import kotlinx.serialization.Serializable
import net.minestom.server.entity.LivingEntity
import net.minestom.server.entity.Player
import world.cepi.itemextension.item.Item
import world.cepi.kstom.command.arguments.generation.annotations.ParameterContext
import world.cepi.mob.arguments.MobOffHandContextParser
import world.cepi.mob.mob.Mob
import world.cepi.projectile.context.ProjectileOffHandContextParser
import world.cepi.projectile.structure.Projectile

@Serializable
data class Summon(
    @param:ParameterContext(ProjectileOffHandContextParser::class)
    val projectile: Projectile,
    @param:ParameterContext(MobOffHandContextParser::class)
    val mob: Mob
) : Action() {

    override val displayName = "Projectile"

    override val requiresTarget = false

    override fun invoke(player: Player, target: LivingEntity?, item: Item): Boolean {
        projectile.shoot(mob, player)
        return false
    }
}