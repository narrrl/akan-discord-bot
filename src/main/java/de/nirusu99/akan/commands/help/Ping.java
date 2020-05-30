package de.nirusu99.akan.commands.help;

import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.kohsuke.MetaInfServices;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This {@link ICommand} sends a ping pong message to test the bot. Returns also the {@link JDA#getGatewayPing()}.
 *
 * @author Nils Pukropp
 * @since 1.0
 */
@MetaInfServices(ICommand.class)
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
    public String toString() {
        return "Gets the current bot ping to the Discord gateway. Useful to test the bot :)";
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
