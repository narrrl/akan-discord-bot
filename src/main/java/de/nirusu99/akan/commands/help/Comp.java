package de.nirusu99.akan.commands.help;

import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.core.GuildManager;

import org.kohsuke.MetaInfServices;

import javax.annotation.Nonnull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@MetaInfServices(ICommand.class)
public final class Comp implements ICommand {
    private static final Pattern PATTERN = Pattern.compile("comp .*");

    @Override
    public Matcher matches(final String input) {
        return PATTERN.matcher(input);
    }

    @Override
    public void run(@Nonnull CommandContext ctx) {
        GuildManager gm = GuildManager.getManager(ctx.getGuild().getIdLong(), ctx.getBot());
    }

    @Override
    public String getName() {
        return "comp";
    }

    @Override
    public String toString() {
        return "Creates a competetiv channel that can be edited from an user via the bot \n Get more help with <prefix>comp help";
    }

    @Override
    public String syntax() {
        return "<prefix>comp [operation] [args]";
    }

    @Override
    public String gifHelpUrl() {
        return null;
    }
}
