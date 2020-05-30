package de.nirusu99.akan.commands.fun.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.core.GuildMusicManager;
import de.nirusu99.akan.core.PlayerManager;
import org.kohsuke.MetaInfServices;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@MetaInfServices(ICommand.class)
public class Playing implements ICommand {
    private final static Pattern PATTERN = Pattern.compile("playing");

    @Override
    public void run(CommandContext ctx) {
        PlayerManager manager = PlayerManager.getInstance();
        GuildMusicManager musicManager = manager.getGuildMusicManager(ctx.getGuild());
        final AudioTrack track = manager.getPlaying(musicManager);
        if (track == null) {
            ctx.getChannel().sendTyping().queue(rep ->
                    ctx.getChannel().sendMessage("No music is playing!").queue());
            return;
        }
        ctx.getChannel().sendTyping().queue(rep ->
                ctx.getChannel().sendMessage("Now playing: "
                        + track.getInfo().author + " - " + track.getInfo().title).queue());
    }

    @Override
    public String getName() {
        return "playing";
    }

    @Override
    public String syntax() {
        return "<prefix>playing";
    }

    @Override
    public String gifHelpUrl() {
        return null;
    }

    @Override
    public String toString() {
        return "Displays current playing song";
    }

    @Override
    public Matcher matches(final String input) {
        return PATTERN.matcher(input);
    }
}
