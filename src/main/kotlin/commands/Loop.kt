package commands

import MusicManager
import SlashEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class Loop(private var musicManager: MusicManager) : SlashEvent {
    override fun execute(event: SlashCommandInteractionEvent) {
        if (event.guild == null) {
            event.reply("Error: Guild not found!").queue()
            return
        }

        val guildManager = musicManager.getGuildPlayer(event.guild!!)
        val isLooping = guildManager.scheduler.isChannelLooping(event.guild!!.id) != null

        val userId = event.user.id
        val guildId = event.guild!!.id
        val playingTrack = guildManager.player.playingTrack

        if (isLooping) {
            event.reply("No longer looping: " + playingTrack.info.title).queue()

            guildManager.scheduler.removeLoop(guildId)
        } else {
            guildManager.scheduler.addLoop(
                playingTrack.identifier,
                userId,
                guildId,
                playingTrack
            )

            event.reply("Looping: " + playingTrack.info.title).queue()
        }
    }

    override fun getName(): String {
        return "loop"
    }

    override fun getDescription(): String {
        return "Loop the current song"
    }
}