package dev.alexthegoood.kotlinProvider.utilities.camera.rotation

import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import org.bukkit.entity.Player

abstract class CameraRotation(
    val player: Player,
) : AutoCloseable {

    protected val scope = CoroutineScope(Dispatchers.Default)

    var time: Int = 0
    protected set

    protected abstract val task: ScheduledTask?

    override fun close() {
        task?.cancel()
        scope.cancel()
    }

}