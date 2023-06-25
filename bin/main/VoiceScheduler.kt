import java.util.*
import kotlin.collections.HashMap

class VoiceScheduler {
    private var joinedChannels: Map<String, Date> = HashMap()

    fun addChannel(channelId: String) {
        this.joinedChannels.plus(Pair(channelId, Date()))
    }

    fun getChannel(channelId: String): Date? {
        return joinedChannels.get(channelId)
    }

    fun getChannelList(): Map<String, Date> {
        return joinedChannels
    }
}
