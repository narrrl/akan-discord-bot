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
    public void run(CommandContext ctx) {
        if (ctx.getArgs().isEmpty()) {
            StringBuilder out = new StringBuilder();
            CommandBuilder.getCommands().forEach(cmd -> out.append(cmd.getName()).append("\n"));
            EmbedBuilder emb = new EmbedBuilder();
            emb.setTitle("All Commands:").setDescription(out.toString().substring(0, out.length() - 1))
                    .setThumbnail(ctx.getSelfUser().getAvatarUrl()).setColor(Color.PINK);
            ctx.reply(emb.build());
            return;
        }
        for (ICommand cmd : CommandBuilder.getCommands()) {
            if (cmd.getName().equals(ctx.getArgs().get(0))) {
                EmbedBuilder emb = new EmbedBuilder();
                emb.setTitle(cmd.getName())
                        .setThumbnail(ctx.getGuild().getIconUrl())
                        .setDescription("**Description**:\n" + cmd.toString()
                                + LINE_BREAK + "\n\n**Syntax**:\n" + cmd.syntax())
                        .setImage(cmd.gifHelpUrl())
                        .setColor(Color.PINK);
                ctx.reply(emb.build());
                return;
            }
        }
        ctx.reply("Command not found");
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
