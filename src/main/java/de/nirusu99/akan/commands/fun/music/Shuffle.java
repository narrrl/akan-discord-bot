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
public final class Shuffle implements ICommand {
    private static final Pattern PATTERN = Pattern.compile("shuffle");

    @Override
    public void run(CommandContext ctx) {
        PlayerManager manager = PlayerManager.getInstance();
        GuildMusicManager musicManager = manager.getGuildMusicManager(ctx.getGuild());
        manager.shuffle(musicManager);
        ctx.getChannel().sendTyping().queue(rep ->
                ctx.getChannel().sendMessage("Shuffled queue!").queue());
    }

    @Override
    public String getName() {
        return "shuffle";
    }

    @Override
    public String syntax() {
        return "<prefix>shuffle";
    }

    @Override
    public String gifHelpUrl() {
        return null;
    }

    @Override
    public String toString() {
        return "Shuffles current playing queue";
    }

    @Override
    public Matcher matches(final String input) {
        return PATTERN.matcher(input);
    }
}
