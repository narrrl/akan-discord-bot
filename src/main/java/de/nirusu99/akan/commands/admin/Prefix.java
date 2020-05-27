package de.nirusu99.akan.commands.admin;

import de.nirusu99.akan.AkanBot;
import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.Error;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.utils.Const;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Prefix implements ICommand {
    private final static Pattern PATTERN = Pattern.compile("prefix " + Const.PREFIX_REGEX);

    @Override
    public void run(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        if (args.size() != 1) {
            ctx.getChannel().sendTyping().queue(rep ->
                    ctx.getChannel().sendMessage("invalid arguments").queue());
        }
        if (AkanBot.userIsOwner(ctx.getAuthor())) {
            String newPrefix = args.get(0);
            ctx.setPrefix(newPrefix);
            ctx.getChannel().sendTyping().queue(rep ->
                    ctx.getChannel()
                            .sendMessage("Prefix was set to " + newPrefix + " <:remV:639621688887083018>")
                            .queue());
        } else {
            throw new IllegalArgumentException(Error.NOT_OWNER.toString());
        }
    }

    @Override
    public String getName() {
        return "prefix";
    }

    @Override
    public String syntax() {
        return "<prefix>prefix <new prefix>";
    }

    @Override
    public String toString() {
        return "Changes the prefix of the bot. Only bot owner can do that";
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
