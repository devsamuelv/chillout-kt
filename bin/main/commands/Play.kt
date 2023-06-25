package commands

import MusicManager
import SlashEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionMapping

class Play(private var musicManager: MusicManager) : SlashEvent {
    override fun execute(event: SlashCommandInteractionEvent) {
        val songLink: String? = event.getOption("link", OptionMapping::getAsString)

        if (songLink == null) {
            event.reply("Please define a song.").queue()
            return
        }

        musicManager.loadAndPlay(event, songLink, event.user.id)
    }

    override fun getName(): String {
        return "play"
    }

    override fun getDescription(): String {
        return "play music with provided link"
    }
}