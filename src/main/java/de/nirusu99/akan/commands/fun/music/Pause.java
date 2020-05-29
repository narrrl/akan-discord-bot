package de.nirusu99.akan.commands.fun.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.core.AudioInstance;
import net.dv8tion.jda.api.entities.Guild;
import org.kohsuke.MetaInfServices;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@MetaInfServices(ICommand.class)
public final class Pause implements ICommand {
    private static final Pattern PATTERN = Pattern.compile("pause");

    @Override
    public void run(CommandContext ctx) {
        AudioInstance instance = ctx.getBot().getAudioInstance(ctx.getGuild());
        if (instance == null) {
            ctx.getChannel().sendTyping().queue(rep ->
                    ctx.getChannel().sendMessage("No music is playing!").queue());
            return;
        }
        instance.getPlayer().setPaused(true);
    }

    @Override
    public String getName() {
        return "pause";
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
