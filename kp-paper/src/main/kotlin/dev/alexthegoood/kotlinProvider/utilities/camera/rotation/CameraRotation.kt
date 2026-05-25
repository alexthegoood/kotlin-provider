package dev.alexthegoood.kotlinProvider.utilities.camera.rotation

import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.entity.Player

abstract class CameraRotation(
    val player: Player,
) : AutoCloseable {

    var time: Int = 0
    protected set

    protected abstract val task: ScheduledTask?

    override fun close() {
        task?.cancel()
    }

}