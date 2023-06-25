package commands

import SlashEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class Leave : SlashEvent {
    override fun execute(event: SlashCommandInteractionEvent) {

    }

    override fun getName(): String {
        return "leave"
    }

    override fun getDescription(): String {
        return "Leave the voice channel"
    }
}