package de.nirusu99.akan.commands.help;

import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Ping implements ICommand {
    private final Pattern pattern = Pattern.compile("ping");
    @Override
    public void run(CommandContext ctx) {
        MessageChannel channel = ctx.getChannel();
        channel.sendTyping().queue(rep ->
                channel.sendMessage("Pong!").queue(response ->
                        response.editMessageFormat("Pong: %d ms <a:loading:529887640472911922>",
                                ctx.getJDA().getGatewayPing()).queue()));
        ;
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String syntax() {
        return "<prefix>ping";
    }

    @Override
    public String gifHelpUrl() {
        return null;
    }

    @Override
    public Matcher matches(String input) {
        return pattern.matcher(input);
    }
}
