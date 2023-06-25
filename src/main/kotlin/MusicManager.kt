import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.managers.AudioManager

class MusicManager(
    var voiceScheduler: VoiceScheduler, var playerManager: AudioPlayerManager,
    var musicManagers: HashMap<Long, GuildManager>
) {
    init {
        AudioSourceManagers.registerRemoteSources(playerManager)
    }

    fun getGuildPlayer(guild: Guild): GuildManager {
        val guildId = guild.id.toLong()
        var manager = musicManagers.get(guildId)

        if (manager == null) {
            manager = GuildManager(playerManager)

            musicManagers[guildId] = manager
        }

        guild.audioManager.sendingHandler = manager.getSendHandler()

        return manager
    }

    fun loadAndPlay(slashCommandInteractionEvent: SlashCommandInteractionEvent, trackUrl: String, userId: String) {
        val guild = slashCommandInteractionEvent.guild

        if (guild != null) {
            val manager = getGuildPlayer(guild)

            var items = playerManager.loadItemOrdered(
                manager,
                trackUrl,
                audioResultBuilder(slashCommandInteractionEvent, trackUrl, manager, userId)
            )
        }
    }

    private fun audioResultBuilder(event: SlashCommandInteractionEvent, trackUrl: String, manager: GuildManager, userId: String): AudioLoadResultHandler {
        class E : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) {
                event.reply("Adding to queue: " + track.info.title).queue()

                if (event.guild != null) {
                    play(event.guild!!, userId, manager, track)
                }
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                val firstTrack: AudioTrack = playlist.selectedTrack

                event.reply("Queuing: " + firstTrack.info.title).queue()

                play(event.guild!!, userId, manager, firstTrack)
            }

            override fun noMatches() {
                event.reply("No matches found.").queue()

                println("[error] Video not found. Link: $trackUrl")
            }

            override fun loadFailed(exception: FriendlyException) {
                event.reply("Failed to load")
            }
        }

        return E()
    }

    /**
     * Note: this should use the guild for queueing unlike in the old version
     * where it had a queue for the text-channel the message was sent in.
     */
    fun play(guild: Guild, userId: String, manager: GuildManager, track: AudioTrack) {
        val channel = connectToVoice(guild.audioManager, userId)

        if (channel == null) {
            println("[error] Channel not found")
            return
        }

        val justJoined: Boolean = voiceScheduler.getChannel(channel.id) == null

        if (justJoined) {
            voiceScheduler.addChannel(channel.id)
        }

        manager.scheduler.queue(track)
    }

    private fun connectToVoice(manager: AudioManager, userId: String): VoiceChannel? {
        var _channel: VoiceChannel? = null

        if (!manager.isConnected()) {
            for (channel: VoiceChannel in manager.guild.voiceChannels) {
                for (member: Member in channel.members) {
                    if (member.id == userId) {
                        manager.openAudioConnection(channel)
                        _channel = channel
                        break
                    }
                }
            }
        }

        return _channel
    }
}