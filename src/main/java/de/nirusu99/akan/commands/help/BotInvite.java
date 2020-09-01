package de.nirusu99.akan.commands.help;

import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import org.kohsuke.MetaInfServices;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This {@link ICommand} sends a invite link for the bot embedded.
 *
 * @author Nils Pukropp
 * @since 1.0
 */
@MetaInfServices(ICommand.class)
public final class BotInvite implements ICommand {
    private static final Pattern PATTERN = Pattern.compile("botinvite");

    @Override
    public void run(CommandContext ctx) {
        final EmbedBuilder emb = new EmbedBuilder();
        emb.setTitle("Invite Akan to your Server", "https://discordapp.com/oauth2/authorize?&client_id="
                + ctx.getJDA().getSelfUser().getId() + "&scope=bot&permissions=8")
                .setThumbnail(ctx.getJDA().getSelfUser().getEffectiveAvatarUrl())
                .setDescription("See you soon, master!").setColor(Color.PINK);
        ctx.reply(emb.build());
    }

    @Override
    public String getName() {
        return "botinvite";
    }

    @Override
    public String toString() {
        return "Sends the invite link for the bot";
    }

    @Override
    public String syntax() {
        return null;
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
