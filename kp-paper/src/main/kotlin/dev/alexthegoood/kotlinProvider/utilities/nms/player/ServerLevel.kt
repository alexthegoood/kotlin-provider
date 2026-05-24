package dev.alexthegoood.kotlinProvider.utilities.nms.player

import net.minecraft.server.level.ServerLevel
import org.bukkit.craftbukkit.CraftWorld
import org.bukkit.entity.Player

val Player.serverLevel: ServerLevel
    get() = this.craftWorld.handle

val CraftWorld.serverLevel: ServerLevel
    get() = this.handle