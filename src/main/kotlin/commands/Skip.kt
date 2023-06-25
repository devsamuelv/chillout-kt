package commands

import SlashEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class Skip : SlashEvent {
  override fun execute(event: SlashCommandInteractionEvent) {}

  override fun getName(): String {
    return "skip"
  }

  override fun getDescription(): String {
    return "Skip the current song"
  }
}
