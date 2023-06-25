import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import java.util.concurrent.LinkedBlockingQueue

class TrackScheduler(var player: AudioPlayer) : AudioEventAdapter() {
    private var queue: LinkedBlockingQueue<AudioTrack> = LinkedBlockingQueue()
    private var loopList: HashMap<String, AudioTrack> = HashMap()

    fun queue(track: AudioTrack) {
        if (!player.startTrack(track, true)) {
            queue.offer(track)
        }
    }

    fun clear() {
        queue.clear()
    }

    // this will check to see if this channel has a song set to loop
    fun isChannelLooping(channelId: String): AudioTrack? {
        return loopList[channelId]
    }

    fun removeLoop(id: String) {
       loopList.remove(id)
    }

    fun addLoop(id: String, requestor: String, guildId: String, track: AudioTrack): AudioTrack {
        track.userData = TrackData(id, requestor, guildId)

        loopList[id] = track

        return track
    }

    // Start the next track, stopping the current one if it is playing.
    fun nextTrack() {
        player.startTrack(queue.poll(), false)
    }

    override fun onTrackException(
            player: AudioPlayer?,
            track: AudioTrack?,
            exception: FriendlyException?
    ) {
        if (exception != null) {
            println(exception.message)
        }
    }

    override fun onTrackEnd(
            player: AudioPlayer,
            track: AudioTrack,
            endReason: AudioTrackEndReason
    ) {
        val trackData = track.userData as TrackData
        val isChannelLooping = loopList[trackData.channelId] != null

        if (isChannelLooping) {
            val loopingTrack: AudioTrack? = loopList[trackData.channelId]

            if (!player.startTrack(loopingTrack, false)) {
                queue.offer(track)
            }
        }

        if (endReason.mayStartNext) {
            nextTrack()
        }
    }
}
