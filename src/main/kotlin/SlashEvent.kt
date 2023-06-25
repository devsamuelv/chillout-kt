import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

interface SlashEvent {
    fun execute(event: SlashCommandInteractionEvent)
    fun getName(): String
    fun getDescription(): String
}