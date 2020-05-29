package de.nirusu99.akan.commands.fun.music;

import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.core.AudioInstance;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import org.kohsuke.MetaInfServices;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@MetaInfServices(ICommand.class)
public final class Play implements ICommand {
    private static final Pattern PATTERN =
            Pattern.compile("play (https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])");

    @Override
    public void run(CommandContext ctx) {
        Guild guild = ctx.getGuild();
        GuildVoiceState state = ctx.getMember().getVoiceState();
        VoiceChannel channel = state != null ? state.getChannel() : null;
        if (channel == null) {
            ctx.getChannel().sendTyping().queue(rep ->
                    ctx.getChannel().sendMessage("You must be in a voice channel!").queue());
            return;
        }
        AudioInstance instance = ctx.getBot().getAudioInstance(guild);
        if (instance == null) {
            instance = new AudioInstance(ctx.getBot().getPlayerManager());
            AudioManager audioManager = guild.getAudioManager();
            ctx.getBot().getPlayers().put(guild, instance);
            audioManager.setSendingHandler(instance.getHandler());
            audioManager.openAudioConnection(channel);
        }
        instance.load(ctx.getArgs().get(0), ctx);
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String syntax() {
        return null;
    }

    @Override
    public String gifHelpUrl() {
        return null;
    }

    @Override
    public String toString() {
        return "<prefix>play <link>";
    }

    @Override
    public Matcher matches(String input) {
        return PATTERN.matcher(input);
    }
}
