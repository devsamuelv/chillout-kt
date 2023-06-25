package commands

import MusicManager
import SlashEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class Stop(private var manager: MusicManager) : SlashEvent {
    override fun execute(event: SlashCommandInteractionEvent) {
        val guildManager = manager.getGuildPlayer(event.guild!!)
        val currentTrack = guildManager.player.playingTrack

        event.reply("Stopping: " + currentTrack.info.title).queue()

        guildManager.player.stopTrack()
    }

    override fun getName(): String {
        return "stop"
    }

    override fun getDescription(): String {
        return "Stop the track that is currently playing"
    }
}