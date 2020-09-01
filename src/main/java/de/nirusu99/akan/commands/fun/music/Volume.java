package de.nirusu99.akan.commands.fun.music;

import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.Error;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.core.GuildMusicManager;
import de.nirusu99.akan.core.PlayerManager;
import de.nirusu99.akan.utils.Const;
import de.nirusu99.akan.utils.DiscordUtil;
import org.kohsuke.MetaInfServices;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@MetaInfServices(ICommand.class)
public final class Volume implements ICommand {
    private static final Pattern PATTERN = Pattern.compile("volume (" + Const.INT_REGEX + ")");

    @Override
    public void run(final CommandContext ctx) {
        if (ctx.getArgs().size() != 1) {
            throw new IllegalArgumentException(Error.INVALID_ARGUMENTS.toString());
        }
        
        if (!DiscordUtil.areInSameVoice(ctx.getMember(), ctx.getSelfMember())) {
            ctx.reply("You must be in the same voice channel!");
            return;
        }

        PlayerManager manager = PlayerManager.getInstance();
        GuildMusicManager musicManager = manager.getGuildMusicManager(ctx.getGuild());

        int volume;
        try {
            volume = Integer.parseInt(ctx.getArgs().get(0));
        } catch (NumberFormatException e) {
            ctx.getBot().printInfo(e.getMessage());
            return;
        }
        ctx.getBot().getLogger().setVolume(ctx.getGuild().getIdLong(), volume);
        musicManager.getPlayer().setVolume(volume);
    }

    @Override
    public String getName() {
        return "volume";
    }

    @Override
    public String syntax() {
        return "<prefix>volume <number>";
    }

    @Override
    public String gifHelpUrl() {
        return null;
    }

    @Override
    public String toString() {
        return "Sets the volume for the bot";
    }

    @Override
    public Matcher matches(final String input) {
        return PATTERN.matcher(input);
    }
}
