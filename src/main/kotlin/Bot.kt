import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import commands.*
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands

class Bot(token: String) : ListenerAdapter() {
    private val jdaBot: JDA = JDABuilder.createDefault(token).build()

    private var guildManagers: HashMap<Long, GuildManager> = HashMap()
    private var voiceScheduler = VoiceScheduler()
    private var playerManager = DefaultAudioPlayerManager()
    private var musicManager = MusicManager(voiceScheduler, playerManager, guildManagers)

    private var pingCommand = Ping()
    private var leaveCommand = Leave()
    private var playCommand = Play(musicManager)
    private var stopCommand = Stop(musicManager)
    private var loopCommand = Loop(musicManager)
    private var skipCommand = Skip()

    init {
        jdaBot.presence.setPresence(OnlineStatus.ONLINE, false)
        jdaBot.addEventListener(this)

        jdaBot.updateCommands()
                .addCommands(
                        Commands.slash(pingCommand.getName(), pingCommand.getDescription()),
                        Commands.slash(playCommand.getName(), playCommand.getDescription())
                                .addOption(OptionType.STRING, "link", "song link"),
                        Commands.slash(stopCommand.getName(), stopCommand.getDescription()),
                        Commands.slash(loopCommand.getName(), loopCommand.getDescription())
                )
                .queue()
    }

    override fun onReady(event: ReadyEvent) {
        println("Chillout Ready.")
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        when (event.name) {
            pingCommand.getName() -> pingCommand.execute(event)
            playCommand.getName() -> playCommand.execute(event)
            leaveCommand.getName() -> leaveCommand.execute(event)
            stopCommand.getName() -> stopCommand.execute(event)
            loopCommand.getName() -> loopCommand.execute(event)
            skipCommand.getName() -> skipCommand.execute(event)
        }
    }
}
