package de.nirusu99.akan.commands.fun;

import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.images.Host;
import de.nirusu99.akan.images.Image;
import de.nirusu99.akan.utils.Const;
import net.dv8tion.jda.api.EmbedBuilder;
import org.kohsuke.MetaInfServices;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This {@link ICommand} searches for images in given domains ({@link Host}).
 * This class only gets the user input and embeds the images into messages,
 * the logic is implemented in {@link Host#searchForImages(String, int, int)}.
 *
 * @author Nils Pukropp
 * @since 1.0
 */
@MetaInfServices(ICommand.class)
public final class Search implements ICommand {
    private static final Pattern PATTERN = Pattern.compile("search (" + Const.HOSTS_REGEX + ") (" + Const.TAGS_REGEX
            + ")( " + Const.INT_REGEX + ")?( " + Const.INT_REGEX + ")?");

    @Override
    public void run(@Nonnull CommandContext ctx) {
        List<String> args = ctx.getArgs();
        int amount;
        int page;
        if (args.size() >= 3) {
            amount = Integer.parseInt(args.get(2));
        } else {
            amount = 1;
        }
        if (args.size() == 4) {
            page = Integer.parseInt(args.get(3));
        } else {
            page = 1;
        }
        if (amount > 5 || amount < 1 || page < 1) {
            throw new IllegalArgumentException("You can't search for more than 5 pics or less than 1 pic. "
                    + "Pages start at 1");
        }
        String tags = args.get(1);
        Collection<Image> images = Host.getHost(args.get(0)).searchForImages(tags, amount, page);
        if (images.size() == 0) {
            ctx.reply("no pictures found for tags " + tags);
        } else {
            for (Image img : images) {
                final EmbedBuilder emb; emb = new EmbedBuilder();
                if (!img.isVideo()) {
                    emb.setImage(img.getUrl())
                            .setFooter(img.getSource())
                            .setTitle(img.getId(), img.getPostUrl())
                            .setColor(Color.PINK);
                    ctx.reply(emb.build());
                } else {
                    ctx.reply(img.getUrl());
                }
            }
        }
    }

    @Override
    public String getName() {
        return "search";
    }

    @Override
    public String syntax() {
        return "<prefix>search <" + Const.HOSTS_REGEX + "> <tag+tag+tag...> <amount> <page>";
    }

    @Override
    public String gifHelpUrl() {
        return "https://media.giphy.com/media/KCwd6UCEPyDUuhje5R/giphy.gif";
    }

    @Override
    public String toString() {
        return "Searches images by tags on Gelbooru, Danbooru or Safebooru";
    }

    @Override
    public Matcher matches(String input) {
        return PATTERN.matcher(input);
    }
}
