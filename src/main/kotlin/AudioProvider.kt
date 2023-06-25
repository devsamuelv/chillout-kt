import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame
import java.nio.ByteBuffer
import net.dv8tion.jda.api.audio.AudioSendHandler

class AudioProvider(private var player: AudioPlayer) : AudioSendHandler {
  private var lastFrame: AudioFrame? = null

  override fun canProvide(): Boolean {
    lastFrame = player.provide()

    return lastFrame != null
  }

  override fun isOpus(): Boolean {
    return true
  }

  override fun provide20MsAudio(): ByteBuffer? {
    val _lastFrame = lastFrame

    return ByteBuffer.wrap(_lastFrame?.data)
  }
}
