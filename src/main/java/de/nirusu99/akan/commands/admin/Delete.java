package de.nirusu99.akan.commands.admin;

import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.Error;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.utils.Const;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.kohsuke.MetaInfServices;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@MetaInfServices(ICommand.class)
public class Delete implements ICommand {
    private static final Pattern PATTERN = Pattern.compile("delete( " + Const.USER_REGEX
            + ")?( " + Const.INT_REGEX + ")?");
    @Override
    public void run(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        if (args.size() > 2) {
            ctx.getChannel().sendTyping().queue(rep ->
                    ctx.getChannel().sendMessage(Error.INVALID_ARGUMENTS.toString()).queue());
            return;
        }
        if (!ctx.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            ctx.getChannel().sendTyping().queue(rep ->
                    ctx.getChannel().sendMessage(Error.NO_PERMISSION.toString()).queue());
            return;
        }
        final TextChannel channel = ctx.getChannel();
        int amount;
        final List<Member> targets = ctx.getMessage().getMentionedMembers();
        if (args.size() == 2) {
            amount = Integer.parseInt(args.get(1));
        } else {
            try {
                amount = Integer.parseInt(args.get(0));
            } catch (NumberFormatException e) {
                amount = 100;
            }
        }
        if (targets.isEmpty()) {
            channel.getHistory().retrievePast(amount).complete().forEach(msg -> msg.delete().queue());
            return;
        }
        final Member target = targets.get(0);
        if (target == null) {
            ctx.getChannel().sendTyping().queue(rep ->
                    ctx.getChannel().sendMessage("invalid user!").queue());
            return;
        }
        channel.getHistory().retrievePast(amount).complete().forEach(msg -> {
            if (msg.getAuthor().getIdLong() == target.getIdLong()) {
                msg.delete().queue();
            }
        });
    }

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String syntax() {
        return "<prefix>delete <user> <amount?default=100>";
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
        return "Deletes all messages of a given user";
    }
}
