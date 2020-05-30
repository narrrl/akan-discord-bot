package de.nirusu99.akan.commands.admin;

import de.nirusu99.akan.AkanBot;
import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.Error;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.utils.Const;
import net.dv8tion.jda.api.entities.User;
import org.kohsuke.MetaInfServices;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This {@link ICommand} activates/deactivates the message reaction on command execution,
 * to signal that he command gets executed.
 * This setting gets saved by {@link AkanBot#setSuccessReaction(boolean)} in {@link de.nirusu99.akan.core.Config}
 * which means that it won't reset on bot restart.
 * Checks if the user is a bot owner with {@link AkanBot#userIsOwner(User)}
 *
 * @author Nils Pukropp
 * @since 1.0
 */
@MetaInfServices(ICommand.class)
public final class SuccessReaction implements ICommand {
    private static final Pattern PATTERN = Pattern.compile("successreaction (" + Const.BOOLEAN_REGEX + ")");

    @Override
    public void run(@Nonnull CommandContext ctx) {
        if (AkanBot.userIsOwner(ctx.getAuthor())) {
            List<String> args = ctx.getArgs();
            if (args.size() != 1) {
                ctx.getChannel().sendTyping().queue(rep ->
                        ctx.getChannel().sendMessage(Error.INVALID_ARGUMENTS.toString()).queue());
                return;
            }
            ctx.setSuccessReaction(Boolean.parseBoolean(args.get(0)));

        } else {
            ctx.getChannel().sendTyping().queue(rep ->
                    ctx.getChannel().sendMessage(Error.NOT_OWNER.toString()).queue());
        }
    }

    @Override
    public String getName() {
        return "successreaction";
    }

    @Override
    public String syntax() {
        return "<prefix>successreaction <true|false>";
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
        return "The bot adds a reaction to executed commands if set to true";
    }
}
