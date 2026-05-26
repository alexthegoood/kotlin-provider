package dev.alexthegoood.kotlinProvider.utilities.camera.rotation

import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.entity.Player
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Experimental
abstract class CameraRotationTask(
    val player: Player,
) : AutoCloseable {

    var time: Int = 0
    protected set

    protected abstract val task: ScheduledTask?

    override fun close() {
        task?.cancel()
    }

}