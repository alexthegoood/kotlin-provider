package dev.alexthegoood.kotlinProvider.utilities.nms.player

import net.minecraft.server.level.ServerPlayer
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player

val Player.serverPlayer: ServerPlayer
    get() = this.craftPlayer.handle

val CraftPlayer.serverPlayer: ServerPlayer
    get() = this.handle