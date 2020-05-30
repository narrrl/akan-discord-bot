package de.nirusu99.akan.commands.fun;

import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.utils.Const;
import de.nirusu99.akan.utils.EmoteConverter;
import org.kohsuke.MetaInfServices;

import javax.annotation.Nonnull;
import java.util.List;
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
public final class Rep implements ICommand {
    private static final Pattern PATTERN = Pattern.compile("rep( " + Const.REP_REGEX + ")?");

    @Override
    public void run(@Nonnull CommandContext ctx) {
        List<String> args = ctx.getArgs();
        if (args.isEmpty()) {
            ctx.getChannel().sendTyping().queue(rep ->
                    ctx.getChannel().sendMessage(EmoteConverter.convertRegionalIndicators("kek")).queue());
            return;
        }
        StringBuilder out = new StringBuilder();
        args.forEach(i -> out.append(i).append(" "));
        for (String str : EmoteConverter
                .convertRegionalIndicators(out.substring(0, out.length() - 1)).split(" ", -1)) {
            ctx.getChannel().sendTyping().queue(rep ->
                    ctx.getChannel().sendMessage(str).queue());
        }
    }

    @Override
    public String getName() {
        return "rep";
    }

    @Override
    public String syntax() {
        return "<prefix>rep <message>";
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
        return "Sends the user message converted to emotes";
    }
}
