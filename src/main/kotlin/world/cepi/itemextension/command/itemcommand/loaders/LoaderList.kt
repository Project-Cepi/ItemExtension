package world.cepi.itemextension.command.itemcommand.loaders

import world.cepi.itemextension.command.itemcommand.loaders.actions.*

val loaders = listOf(
    // loaders that add pure action-based functionality to the cmd
    CreateAction,
    RemoveAction,
    ResetAction,
    SetAction,
    UpdateAction,

    // regular loaders
    ConditionLoader,
    DefaultExecutorLoader,
    TraitListLoader
)