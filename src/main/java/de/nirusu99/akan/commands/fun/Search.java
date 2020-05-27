package de.nirusu99.akan.commands.fun;

import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.images.Host;
import de.nirusu99.akan.images.Image;
import de.nirusu99.akan.images.ImageSearch;
import de.nirusu99.akan.utils.Const;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Search implements ICommand {
    private final static Pattern pattern = Pattern.compile("search ("+ Host.HOSTS_REGEX +") (" + Const.TAGS_REGEX + ")( "
            + Const.INT_REGEX + ")?( " + Const.INT_REGEX + ")?");

    @Override
    public void run(CommandContext cfx) {
        List<String> args = cfx.getArgs();
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
        if (amount > 5 || amount < 1 ||page < 1) {
            throw new IllegalArgumentException("You can't search for more than 5 pics or less than 1 pic. " +
                    "Pages start at 1");
        }
        String tags = args.get(1);
        Collection<Image> images = ImageSearch.searchFor(tags, amount, page, Host.getHost(args.get(0)));
        if (images.size() == 0) {
            cfx.getChannel().sendTyping().queue(rep ->
                    cfx.getChannel().sendMessage("no pictures found for tags " + tags).queue());
        } else {
            for (Image img : images) {
                final EmbedBuilder emb;emb = new EmbedBuilder();
                if (!img.isVideo()) {
                    emb.setImage(img.getUrl())
                            .setFooter(img.getSource())
                            .setTitle(img.getId(), img.getPostUrl())
                            .setColor(Color.PINK);
                    cfx.getChannel().sendTyping().queue(rep ->
                            cfx.getChannel().sendMessage(emb.build()).queue());
                } else {
                    cfx.getChannel().sendTyping().queue(rep ->
                            cfx.getChannel().sendMessage(img.getUrl()).queue());
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
        return "<prefix>search <" + Host.HOSTS_REGEX + "> <tag+tag+tag...> <amount> <page>";
    }

    @Override
    public String gifHelpUrl() {
        return "https://media.giphy.com/media/KCwd6UCEPyDUuhje5R/giphy.gif";
    }

    @Override
    public String toString() {
        return "Searches images by tags on Gelbooru";
    }

    @Override
    public Matcher matches(String input) {
        return pattern.matcher(input);
    }
}
