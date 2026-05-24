package dev.alexthegoood.kotlinProvider.utilities.nms.player

import org.bukkit.craftbukkit.CraftWorld
import org.bukkit.entity.Player

val Player.craftWorld: CraftWorld
    get() = this.world as CraftWorld