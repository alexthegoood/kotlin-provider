package dev.alexthegoood.kotlinProvider.utilities.nms.player

import net.minecraft.core.BlockPos
import org.bukkit.Location
import org.bukkit.entity.Entity

val Entity.blockPos: BlockPos
    get() = with(this.location) { BlockPos(blockX, blockY, blockZ) }

val Location.blockPos: BlockPos
    get() = BlockPos(blockX, blockY, blockZ)