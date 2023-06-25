package commands

import SlashEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class Ping : SlashEvent {
    override fun execute(event: SlashCommandInteractionEvent) {
        event.reply("Pong").queue()
    }

    override fun getName(): String {
        return "ping"
    }

    override fun getDescription(): String {
        return "Ping the user!!"
    }
}