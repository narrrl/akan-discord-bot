package de.nirusu99.akan.commands.help;

import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.Error;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.utils.Const;
import net.dv8tion.jda.api.entities.Member;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeJoined implements ICommand {
    private static final Pattern PATTERN = Pattern.compile("joined (" + Const.USER_REGEX + ")");
    @Override
    public void run(CommandContext ctx) {
        if (ctx.getArgs().size() != 1) {
            ctx.getChannel().sendTyping().queue(rep ->
                    ctx.getChannel().sendMessage(Error.INVALID_ARGUMENTS.toString()).queue());
            return;
        }
        List<Member> mentioned = ctx.getMessage().getMentionedMembers();
        if (mentioned.size() == 0) {
            ctx.getChannel().sendTyping().queue(rep ->
                    ctx.getChannel().sendMessage("invalid user").queue());
            return;
        }
        ctx.getChannel().sendTyping().queue(rep ->
                ctx.getChannel().sendMessage(mentioned.get(0).getTimeJoined()
                        .format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm"))).queue());
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String syntax() {
        return "<prefix>joined <user>";
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
        return "Gets the join-date of the user";
    }
}
