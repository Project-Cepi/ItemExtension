package world.cepi.itemextension.command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.LivingEntity
import net.minestom.server.entity.Player
import net.minestom.server.utils.math.IntRange
import world.cepi.kepi.messages.sendFormattedTranslatableMessage
import world.cepi.kstom.command.kommand.Kommand

object HealCommand : Kommand({

    val targets = ArgumentType.Entity("entities")

    default {
        player.heal()

        player.sendFormattedTranslatableMessage("combat", "heal.self")
    }

    syntax(targets) {
        val entities = (context[targets].setDistance(IntRange(0, 10))).find(sender)
            .filterIsInstance<LivingEntity>()

        entities.forEach(LivingEntity::heal)

        if (entities.size == 1) {
            sender.sendFormattedTranslatableMessage(
                "combat", "heal.other",
                Component.text(
                    (entities[0] as? Player)?.username
                        ?: entities[0].customName?.let { PlainTextComponentSerializer.plainText().serialize(it) }
                        ?: "entity"
                )
            )
        } else {
            sender.sendFormattedTranslatableMessage(
                "combat", "heal.others",
                Component.text(entities.size, NamedTextColor.BLUE)
            )
        }
    }
}, "heal")