package de.nirusu99.akan.commands.fun;

import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.Error;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.images.Host;
import de.nirusu99.akan.utils.Const;
import org.kohsuke.MetaInfServices;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This {@link ICommand} sends the amount of images found for a given tag in a given domain {@link Host}.
 * The logic is implemented in {@link Host#searchForAmount(String)}
 *
 * @author Nils Pukropp
 * @since 1.1
 */
@MetaInfServices(ICommand.class)
public final class SearchTag implements ICommand {
    private static final Pattern PATTERN = Pattern.compile("searchtag (" + Const.HOSTS_REGEX + ") ("
            + Const.TAGS_REGEX + ")");

    @Override
    public void run(@Nonnull CommandContext ctx) {
        List<String> args = ctx.getArgs();
        if (args.size() != 2) {
            ctx.getChannel().sendTyping().queue(rep ->
                    ctx.getChannel().sendMessage(Error.INVALID_ARGUMENTS.toString()).queue());
            return;
        }
        String tags = args.get(1);
        String amount = Host.getHost(args.get(0)).searchForAmount(tags);
        if (amount == null) {
            throw new IllegalArgumentException("Woops something went wrong");
        }
        ctx.getChannel().sendTyping()
                .queue(rep ->
                ctx.getChannel().sendMessage("found " + amount
                        .replace("\n", "")
                        .replace(" ", "") + " pictures for tags: " + args.get(1))
                        .queue(rep2 ->
                ctx.getChannel().sendMessage("<:wink:642062516595195904>")
                        .queue()));
    }

    @Override
    public String getName() {
        return "searchtag";
    }

    @Override
    public String syntax() {
        return "<prefix>searchtag (" + Const.HOSTS_REGEX + ") <tag+tag+tag+...>";
    }

    @Override
    public String gifHelpUrl() {
        return null;
    }

    @Override
    public String toString() {
        return "Searches for the number of posts for given tags on Gelbooru, Danbooru or Sagebooru";
    }

    @Override
    public Matcher matches(final String input) {
        return PATTERN.matcher(input);
    }
}
