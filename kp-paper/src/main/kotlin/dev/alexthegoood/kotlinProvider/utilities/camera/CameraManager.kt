package dev.alexthegoood.kotlinProvider.utilities.camera

import dev.alexthegoood.kotlinProvider.utilities.nms.player.serverPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.network.protocol.game.ClientboundPlayerRotationPacket
import org.bukkit.entity.Player
import kotlin.time.Duration.Companion.nanoseconds

class CameraManager(val player: Player) {
    companion object {

        private val scope = CoroutineScope(Dispatchers.Default)

        fun rotate(player: Player, yaw: Float, pitch: Float, packetsPerTick: Int = 1) {
            scope.launch {
                val PPT = packetsPerTick.coerceAtLeast(1)
                val rotationPerMS = ClientboundPlayerRotationPacket(yaw/PPT, true, pitch/PPT, true)
                val duration = (50/PPT).nanoseconds
                val connection = player.serverPlayer.connection
                repeat(PPT) {
                    connection.send(rotationPerMS)
                    delay(duration)
                }
            }
        }

    }

    fun rotate(yaw: Float, pitch: Float, packetsPerMS: Int = 1) =
        rotate(player, yaw, pitch, packetsPerMS)
}