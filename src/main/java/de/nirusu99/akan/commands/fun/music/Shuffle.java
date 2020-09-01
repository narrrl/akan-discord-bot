package de.nirusu99.akan.commands.fun.music;

import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.core.GuildMusicManager;
import de.nirusu99.akan.core.PlayerManager;
import de.nirusu99.akan.utils.DiscordUtil;

import org.kohsuke.MetaInfServices;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@MetaInfServices(ICommand.class)
public final class Shuffle implements ICommand {
    private static final Pattern PATTERN = Pattern.compile("shuffle");

    @Override
    public void run(CommandContext ctx) {

        if (!DiscordUtil.areInSameVoice(ctx.getMember(), ctx.getSelfMember())) {
            ctx.reply("You must be in the same voice channel!");
            return;
        }

        PlayerManager manager = PlayerManager.getInstance();
        GuildMusicManager musicManager = manager.getGuildMusicManager(ctx.getGuild());

        if (musicManager.getPlayer().getPlayingTrack() == null) {
            ctx.reply("No music is playing!");
            return;
        }

        manager.shuffle(musicManager);
        ctx.reply("Shuffled queue!");
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
