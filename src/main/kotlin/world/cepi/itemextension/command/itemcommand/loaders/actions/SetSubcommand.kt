package world.cepi.itemextension.command.itemcommand.loaders.actions

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player
import world.cepi.itemextension.command.itemcommand.*
import world.cepi.itemextension.command.itemcommand.loaders.processTraitName
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.cepiItem
import world.cepi.itemextension.item.checkIsItem
import world.cepi.itemextension.item.traits.ItemTrait
import world.cepi.kepi.item.AddCreationalItem
import world.cepi.kepi.messages.sendFormattedTranslatableMessage
import world.cepi.kstom.command.arguments.generation.GeneratedArguments.Companion.createSyntaxesFrom
import world.cepi.kstom.command.arguments.literal
import world.cepi.kstom.command.kommand.Kommand
import kotlin.reflect.KClass
import kotlin.reflect.full.*

// TODO break down and organize
object SetSubcommand : Kommand(name = "set") {

    val traits = ItemTrait.classList

    init {

        traits.forEach { trait ->

            if (defineSubTraits(trait).isNotEmpty()) {

                defineSubTraits(trait).forEach {
                    generateCommand(trait, it)
                }

            } else
                generateCommand(trait)

        }
    }

    /**
     * Gets the subtrait arguments from a trait object
     * Must be a container that contains subtypes of ItemTraits.
     *
     * @param traitContainer The trait container to scan subtraits from.
     *
     * @return A list of klasses of subtraits from the trait container.
     */
    private fun defineSubTraits(traitContainer: KClass<out ItemTrait>): List<KClass<out ItemTrait>> {
        return traitContainer.sealedSubclasses

    }

    private fun generateCommand(vararg traits: KClass<out ItemTrait>) {

        // EX: /item set action primary, last is primary
        val lastTrait = traits.last()

        val traitArgs = traits.mapIndexed { index, loopTrait ->
            if (index != 0) { // If this trait isnt the first trait (root trait)
                // Drop the suffix, EX PrimaryActionTrait becomes Primary
                loopTrait.simpleName!!.dropLast(traits[index - 1].simpleName!!.length).lowercase().literal()
            } else {
                processTraitName(loopTrait.simpleName!!).literal()
            }
        }

        val traitConstructorSyntaxes = createSyntaxesFrom(lastTrait, *traitArgs.toTypedArray()) { instance ->
            val player = sender as Player

            if (player.itemInMainHand.isAir) {
                AddCreationalItem.put(player, Item().renderItem(1))
                player.sendFormattedTranslatableMessage("item", "created.anyways")
            }

            val itemStack = player.itemInMainHand

            if (checkIsItem(itemStack)) {
                val item = itemStack.cepiItem!!

                item.put(instance)

                player.itemInMainHand = item.renderItem(player.itemInMainHand.amount.coerceIn(1, Integer.MAX_VALUE))

                player.sendFormattedTranslatableMessage("item", "trait.add", Component.text(processTraitName(lastTrait.simpleName!!), NamedTextColor.BLUE))
            } else
                player.sendFormattedTranslatableMessage("item", "formatted.required")
        }

        traitConstructorSyntaxes.callback = { sender.sendFormattedTranslatableMessage("item", "trait.invalid") }
    }

}