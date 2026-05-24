package dev.alexthegoood.kotlinProvider.utilities.camera

import dev.alexthegoood.kotlinProvider.utilities.nms.player.serverPlayer
import kotlinx.coroutines.*
import net.minecraft.network.protocol.game.ClientboundPlayerRotationPacket
import org.bukkit.entity.Player
import kotlin.time.Duration.Companion.milliseconds

class CameraManager(private val player: Player) {

    private val scope = CoroutineScope(Dispatchers.Default)

    fun rotate(yaw: Float, pitch: Float, packetsPerTick: Int = 1): Job {
        val packetsPerTick = packetsPerTick.coerceAtLeast(1)
        val rotationPerTick = ClientboundPlayerRotationPacket(yaw/packetsPerTick, true, pitch/packetsPerTick, true)
        val duration = (50/packetsPerTick).milliseconds
        val connection = player.serverPlayer.connection
        return scope.launch {
            repeat(packetsPerTick) {
                if (!player.isOnline) return@launch
                connection.send(rotationPerTick)
                if (packetsPerTick > 1) delay(duration)
            }
        }
    }

}