package dev.alexthegoood.kotlinProvider.utilities.camera

import dev.alexthegoood.kotlinProvider.utilities.nms.player.serverPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.network.protocol.game.ClientboundPlayerRotationPacket
import org.bukkit.entity.Player
import kotlin.time.Duration.Companion.milliseconds

object CameraManager {

    private val scope = CoroutineScope(Dispatchers.Default)

    fun rotate(player: Player, yaw: Float, pitch: Float, packetsPerTick: Int = 1) = scope.launch {
        val packetsPerTick = packetsPerTick.coerceAtLeast(1)
        val rotationPerTick = ClientboundPlayerRotationPacket(yaw/packetsPerTick, true, pitch/packetsPerTick, true)
        val duration = (50/packetsPerTick).milliseconds
        val connection = player.serverPlayer.connection
        repeat(packetsPerTick) {
            if (!player.isOnline) return@repeat
            connection.send(rotationPerTick)
            if (packetsPerTick > 1) delay(duration)
        }
    }

}