package dev.alexthegoood.kotlinProvider.utilities.camera.rotation

import dev.alexthegoood.kotlinProvider.utilities.nms.player.serverPlayer
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.network.protocol.game.ClientboundPlayerRotationPacket
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import kotlin.time.Duration.Companion.milliseconds

typealias Time = Int

fun Player.rotate(
    plugin: JavaPlugin,
    yaw: (Time) -> Float,
    pitch: (Time) -> Float,
    taskDuration: Long,
    packetsPerTick: Int = 1,
    relative: Boolean = true,
    onTick: (Time) -> Unit = {},
    retired: () -> Unit = {},
) = object : CameraRotationTask(this) {
    val scope = CoroutineScope(Dispatchers.Default)
    override val task: ScheduledTask? = player.scheduler.runAtFixedRate(plugin, { task ->
        time++
        if (time > taskDuration) {
            close()
            return@runAtFixedRate
        }
        val yaw = yaw(time)
        val pitch = pitch(time)
        val rotation = ClientboundPlayerRotationPacket(yaw/packetsPerTick, relative, pitch/packetsPerTick, relative)
        val duration = (50.0/packetsPerTick).milliseconds
        val connection = player.serverPlayer.connection
        scope.launch {
            repeat(packetsPerTick) {
                connection.send(rotation)
                if (packetsPerTick > 1) delay(duration)
            }
            onTick(time)
        }
    }, retired, 1, 1)
}

fun Player.rotate(
    plugin: JavaPlugin,
    yaw: Float,
    pitch: Float,
    packetsPerTick: Int = 1,
    relative: Boolean = true,
    retired: () -> Unit = {},
) = object : CameraRotationTask(this) {
    val scope = CoroutineScope(Dispatchers.Default)
    override val task: ScheduledTask? = player.scheduler.run(plugin, { task ->
        val rotation = ClientboundPlayerRotationPacket(yaw/packetsPerTick, relative, pitch/packetsPerTick, relative)
        val duration = (50.0/packetsPerTick).milliseconds
        val connection = player.serverPlayer.connection
        scope.launch {
            repeat(packetsPerTick) {
                connection.send(rotation)
                if (packetsPerTick > 1) delay(duration)
            }
        }
    }, retired)
}