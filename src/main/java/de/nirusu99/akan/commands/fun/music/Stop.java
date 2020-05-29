package de.nirusu99.akan.commands.fun.music;

import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.core.AudioInstance;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Stop implements ICommand {
    private static final Pattern PATTERN = Pattern.compile("stop");

    @Override
    public void run(CommandContext ctx) {
        AudioInstance instance = ctx.getBot().getAudioInstance(ctx.getGuild());
        if (instance == null) {
            ctx.getChannel().sendTyping().queue(rep ->
                    ctx.getChannel().sendMessage("No music is playing!").queue());
            return;
        }
        instance.getPlayer().stopTrack();
    }

    @Override
    public String getName() {
        return "stop";
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
    public Matcher matches(String input) {
        return PATTERN.matcher(input);
    }
}
