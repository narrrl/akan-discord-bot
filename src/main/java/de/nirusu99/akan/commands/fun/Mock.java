package de.nirusu99.akan.commands.fun;

import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;
import org.kohsuke.MetaInfServices;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This {@link ICommand} repeats the users input converted to emotes.
 * The emotes get converted in {@link EmoteConverter}
 *
 * @author Nils Pukropp
 * @since 1.0
 */
@MetaInfServices(ICommand.class)
public final class Mock implements ICommand {
    private static final Pattern PATTERN = Pattern.compile("mock (.+)");

    @Override
    public void run(@Nonnull CommandContext ctx) {
        List<String> args = ctx.getArgs();
        if (args.isEmpty()) {
            ctx.reply("");
            return;
        }
        StringBuilder input = new StringBuilder();
        args.forEach(i -> input.append(i).append(" "));
        Random rand = new Random();
        StringBuilder out = new StringBuilder();
        int comp;
        for (char c : input.toString().toCharArray()) {
            comp = rand.nextInt(2);
            c = comp == 1 ? Character.toUpperCase(c) : Character.toLowerCase(c);
            out.append(c);
        }
        ctx.reply(out.toString());
    }

    @Override
    public String getName() {
        return "mock";
    }

    @Override
    public String syntax() {
        return "<prefix>mock <message>";
    }

    @Override
    public String gifHelpUrl() {
        return null;
    }

    @Override
    public Matcher matches(String input) {
        return PATTERN.matcher(input);
    }

    @Override
    public String toString() {
        return "Sends the user message with random upper and lower cases";
    }
}
