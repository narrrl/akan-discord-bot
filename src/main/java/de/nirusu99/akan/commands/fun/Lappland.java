package de.nirusu99.akan.commands.fun;

import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;

import org.kohsuke.MetaInfServices;

import javax.annotation.Nonnull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@MetaInfServices(ICommand.class)
public final class Lappland implements ICommand {
    private static final Pattern PATTERN = Pattern.compile("lappland");

    @Override
    public Matcher matches(final String input) {
        return PATTERN.matcher(input);
    }

    @Override
    public void run(@Nonnull CommandContext ctx) {
        ctx.reply("https://cdn.discordapp.com/attachments/657546014810570753/751790013817290782/This-Is-Where-Id-Put-My-Trophy-If-I-Had-One.png");
    }

    @Override
    public String getName() {
        return "lappland";
    }

    @Override
    public String toString() {
        return "troll max";
    }

    @Override
    public String syntax() {
        return "<prefix>lappland";
    }

    @Override
    public String gifHelpUrl() {
        return null;
    }
}
