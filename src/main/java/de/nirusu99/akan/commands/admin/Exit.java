package de.nirusu99.akan.commands.admin;

import de.nirusu99.akan.AkanBot;
import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.commands.Error;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Exit implements ICommand {
    private static final Pattern PATTERN = Pattern.compile("exit");

    @Override
    public Matcher matches(final String input) {
        return PATTERN.matcher(input);
    }

    @Override
    public void run(CommandContext cfx) {
        if (AkanBot.userIsOwner(cfx.getAuthor())) {
            cfx.getChannel().sendTyping().queue(rep ->
                    cfx.getChannel().sendMessage("bai bai!").queue(rep2 ->
                            cfx.getChannel().sendMessage("<:megu:666743067755151360>").queue()));
            cfx.printInfo("shutting down");
            cfx.getJDA().shutdown();
        } else {
            cfx.getChannel().sendTyping().queue(rep ->
                    cfx.getChannel().sendMessage(Error.NOT_OWNER.toString()).queue());
        }
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public String toString() {
        return "exits the bot";
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
