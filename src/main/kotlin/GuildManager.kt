import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager

class GuildManager(manager: AudioPlayerManager) {
  var player: AudioPlayer = manager.createPlayer()
  var scheduler: TrackScheduler = TrackScheduler(player)

  init {
    player.addListener(scheduler)
  }

  fun getSendHandler(): AudioProvider {
    return AudioProvider(player)
  }
}
