package de.nirusu99.akan.commands.admin;

import de.nirusu99.akan.AkanBot;
import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.commands.Error;
import net.dv8tion.jda.api.entities.User;
import org.kohsuke.MetaInfServices;

import javax.annotation.Nonnull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This {@link ICommand} exits the bot.
 * Checks if the user is a bot owner with {@link AkanBot#userIsOwner(User)}.
 * Sometime the bot won't shutdown, idk why.
 *
 * @author Nils Pukropp
 * @since 1.0
 */
@MetaInfServices(ICommand.class)
public final class Exit implements ICommand {
    private static final Pattern PATTERN = Pattern.compile("exit");

    @Override
    public Matcher matches(final String input) {
        return PATTERN.matcher(input);
    }

    @Override
    public void run(@Nonnull CommandContext ctx) {
        if (AkanBot.userIsOwner(ctx.getAuthor())) {
            ctx.printInfo("Shutting down");
            if (ctx.getShardManager() != null) {
                ctx.getShardManager().shutdown(); // bot sometimes won't shutdown, hope this fixes it
            }
            ctx.getJDA().shutdown();
        } else {
            ctx.reply(Error.NOT_OWNER.toString());
        }
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public String toString() {
        return "Exits the bot";
    }

    @Override
    public String syntax() {
        return "<prefix>exit";
    }

    @Override
    public String gifHelpUrl() {
        return null;
    }
}
