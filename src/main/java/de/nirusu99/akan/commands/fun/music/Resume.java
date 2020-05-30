package de.nirusu99.akan.commands.fun.music;

import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.core.GuildMusicManager;
import de.nirusu99.akan.core.PlayerManager;
import org.kohsuke.MetaInfServices;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@MetaInfServices(ICommand.class)
public final class Resume implements ICommand {
    private final static Pattern PATTERN = Pattern.compile("resume");

    @Override
    public void run(CommandContext ctx) {
        PlayerManager manager = PlayerManager.getInstance();
        GuildMusicManager musicManager = manager.getGuildMusicManager(ctx.getGuild());
        manager.pause(musicManager, false);
    }

    @Override
    public String getName() {
        return "resume";
    }

    @Override
    public String syntax() {
        return "<prefix>resume";
    }

    @Override
    public String gifHelpUrl() {
        return null;
    }

    @Override
    public String toString() {
        return "Resumes the music bot";
    }

    @Override
    public Matcher matches(String input) {
        return PATTERN.matcher(input);
    }
}