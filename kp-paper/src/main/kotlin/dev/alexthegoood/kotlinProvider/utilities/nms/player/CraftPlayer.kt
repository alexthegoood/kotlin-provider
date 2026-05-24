package dev.alexthegoood.kotlinProvider.utilities.nms.player

import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player

val Player.craftPlayer: CraftPlayer
    get() = this as CraftPlayer