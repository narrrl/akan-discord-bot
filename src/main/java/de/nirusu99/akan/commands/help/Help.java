package de.nirusu99.akan.commands.help;

import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.commands.CommandBuilder;
import de.nirusu99.akan.utils.Const;
import net.dv8tion.jda.api.EmbedBuilder;
import org.kohsuke.MetaInfServices;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.nirusu99.akan.utils.Const.LINE_BREAK;


/**
 * This {@link ICommand} sends help for given commands or without any args sends a total list of all commands loaded by
 * {@link CommandBuilder#getCommands()}.
 *
 * @author Nils Pukropp
 * @since 1.0
 */
@MetaInfServices(ICommand.class)
public final class Help implements ICommand {
    private static final Pattern PATTERN = Pattern.compile("help( " + Const.NAMING_REGEX + ")?");

    @Override
    public void run(CommandContext cfx) {
        if (cfx.getArgs().isEmpty()) {
            StringBuilder out = new StringBuilder();
            CommandBuilder.getCommands().forEach(cmd -> out.append(cmd.getName()).append("\n"));
            EmbedBuilder emb = new EmbedBuilder();
            emb.setTitle("All Commands:").setDescription(out.toString().substring(0, out.length() - 1))
                    .setThumbnail(cfx.getSelfUser().getAvatarUrl()).setColor(Color.PINK);
            cfx.getChannel().sendTyping().queue(rep ->
                    cfx.getChannel().sendMessage(emb.build()).queue());
            return;
        }
        for (ICommand cmd : CommandBuilder.getCommands()) {
            if (cmd.getName().equals(cfx.getArgs().get(0))) {
                EmbedBuilder emb = new EmbedBuilder();
                emb.setTitle(cmd.getName())
                        .setThumbnail(cfx.getGuild().getIconUrl())
                        .setDescription("**Description**:\n" + cmd.toString()
                                + LINE_BREAK + "**Syntax**:\n" + cmd.syntax())
                        .setImage(cmd.gifHelpUrl())
                        .setColor(Color.PINK);
                cfx.getChannel().sendTyping().queue(rep ->
                        cfx.getChannel().sendMessage(emb.build()).queue());
                return;
            }
        }
        cfx.getChannel().sendTyping().queue(rep ->
                cfx.getChannel().sendMessage("Command not found").queue());
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String toString() {
        return "Shows help for specified commands";
    }

    @Override
    public String syntax() {
        return "<prefix>help <command>";
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
