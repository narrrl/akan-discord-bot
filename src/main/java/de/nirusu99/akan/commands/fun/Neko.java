package de.nirusu99.akan.commands.fun;

import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.images.Host;
import de.nirusu99.akan.images.Image;
import net.dv8tion.jda.api.EmbedBuilder;

import org.kohsuke.MetaInfServices;

import javax.annotation.Nonnull;

import java.awt.Color;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This {@link ICommand} repeats the users input converted to emotes.
 * The emotes get converted in {@link EmoteConverter}
 *
 * @author Nils Pukropp
 * @since 1.0
 */
@MetaInfServices(ICommand.class)
public final class Neko implements ICommand {
    private static final Pattern PATTERN = Pattern.compile("neko( \\d+)?");

    @Override
    public void run(@Nonnull CommandContext ctx) {
        List<String> args = ctx.getArgs();
        Collection<Image> images = null;
        int amount;
        if (args.isEmpty()) {
            amount = 1;
        } else {
            try {
                amount = Integer.parseInt(args.get(0));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                ctx.getChannel().sendMessage("Wups something went wrong, contact admin pls :(").queue();
                return;
            }
        }

        amount = amount > 5 ? 5 : amount;

        try {
            images = Host.getNekos("neko", amount);
        } catch (IOException e) {
            e.printStackTrace();
            ctx.getChannel().sendMessage("Wups something went wrong, contact admin pls :(").queue();
            return;
        }


        EmbedBuilder emb;

        for (Image image : images) {
            emb = new EmbedBuilder();
            emb.setColor(Color.PINK).setImage(image.getUrl()).setTitle("Nya~", image.getUrl());
            ctx.getChannel().sendMessage(emb.build()).queue();;
        }
    }

    @Override
    public String getName() {
        return "neko";
    }

    @Override
    public String syntax() {
        return "<prefix>neko <amount>";
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
        return "some neko pics :D";
    }
}
