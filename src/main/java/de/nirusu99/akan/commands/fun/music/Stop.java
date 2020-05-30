package de.nirusu99.akan.commands.fun.music;

import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.core.PlayerManager;
import org.kohsuke.MetaInfServices;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@MetaInfServices(ICommand.class)
public class Stop implements ICommand {
    private final static Pattern PATTERN = Pattern.compile("stop");

    @Override
    public void run(CommandContext ctx) {
        PlayerManager manager = PlayerManager.getInstance();
        manager.destroy(ctx.getGuild());
        ctx.getGuild().getAudioManager().closeAudioConnection();
    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String syntax() {
        return "<prefix>stop";
    }

    @Override
    public String gifHelpUrl() {
        return null;
    }

    @Override
    public String toString() {
        return "Stops the music bot and delete the complete queue";
    }

    @Override
    public Matcher matches(String input) {
        return PATTERN.matcher(input);
    }
}
