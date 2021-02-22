package world.cepi.itemextension.item.traits.list.attacks

import net.minestom.server.chat.ChatColor
import world.cepi.itemextension.command.itemcommand.plus
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.ItemTrait
import world.cepi.itemextension.item.traits.TraitRefrenceList

abstract class AttackTrait: ItemTrait() {

    companion object: TraitRefrenceList(
            PrimaryAttackTrait::class,
            SecondaryAttackTrait::class,
            TertiaryAttackTrait::class
    )

    open val attack: Attack = Attack.STRIKE
    open val clickType: String = "None"

    override val loreIndex = 2f
    override val taskIndex = 0f

    override fun renderLore(item: Item): List<String> {
        return listOf(ChatColor.RED + attack.displayName + ChatColor.GRAY + " [${clickType}]")
    }

}