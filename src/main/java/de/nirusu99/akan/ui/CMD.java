package de.nirusu99.akan.ui;

import de.nirusu99.akan.AkanBot;
import de.nirusu99.akan.images.GelbooruImage;
import de.nirusu99.akan.images.ImageSearch;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is the command class where all commands get executed and all the user input gets parsed.
 *
 * @author Nils Pukropp
 * @version 1.0-SNAPSHOT
 */
public enum CMD {
    /**
     * Shutdown the bot, didn't find a better way just yet.
     * Checks if the user is a owner {@link de.nirusu99.akan.ui.CMD#OWNERS}.
     */
    EXIT("exit") {
        @Override
        void run(final AkanBot bot, final MessageReceivedEvent event, final Matcher matcher) {
            if (CMD.userIsOwner(event.getAuthor())) {
                event.getChannel().sendMessage("bai bai!").queue();
                System.exit(0);
            } else {
                throw new IllegalArgumentException("Only bot owner can do that!");
            }
        }
    },
    /**
     * Pings the bot and sends a not so accurate ping, but it will do its job
     */
    PING("ping") {
        @Override
        void run(AkanBot bot, MessageReceivedEvent event, Matcher matcher) {
            MessageChannel channel = event.getChannel();
            long time = System.currentTimeMillis();
            channel.sendMessage("Pong!")
                    .queue(response -> response
                            .editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time).queue());
        }

        @Override
        public String toString() {
            return "Pings the bot";
        }
    },
    /**
     * Changes the bot prefix with {@link de.nirusu99.akan.AkanBot#setPrefix(String)}
     * Checks if the user is a owner {@link de.nirusu99.akan.ui.CMD#OWNERS}.
     */
    PREFIX("prefix (" + CMD.PREFIX_REGEX + ")") {
        @Override
        void run(AkanBot bot, MessageReceivedEvent event, Matcher matcher) {
            if (CMD.userIsOwner(event.getAuthor())) {
                String newPrefix = matcher.group(1);
                bot.setPrefix(newPrefix);
                event.getChannel()
                        .sendMessage("Prefix was set to " + newPrefix + " <:remV:639621688887083018>")
                        .queue();
            } else {
                throw new IllegalArgumentException("Only bot owner can do that!");
            }
        }

        @Override
        public String toString() {
            return "Changes the prefix of the bot. Only bot owner can do that";
        }
    },
    /**
     *
     */
    SEARCH("search (" + CMD.NAMING_REGEX + ") (" + CMD.INT_REGEX + ") (" + CMD.INT_REGEX + ")") {
        @Override
        void run(AkanBot bot, MessageReceivedEvent event, Matcher matcher) {
            int amount = Integer.parseInt(matcher.group(2));
            int page = Integer.parseInt(matcher.group(3));
            if (amount > 5) {
                throw new IllegalArgumentException("You can't search for more then 5 pics");
            }
            if (amount < 1) {
                throw new IllegalArgumentException("You can't search for less then 1 pic");
            }
            if (page < 1) {
                throw new IllegalArgumentException("You can't search for pages less then 1");
            }
            List<String> tags = Arrays.asList(matcher.group(1).split("\\+", -1));
            GelbooruImage[] images = ImageSearch.getImagesFor(tags, amount, page);
            if (images.length == 0) {
                StringBuilder out = new StringBuilder();
                tags.forEach(out::append);
                event.getChannel().sendMessage("no pictures found for tag " + out.toString()).queue();
            } else {
                EmbedBuilder emb = new EmbedBuilder();
                for (GelbooruImage img : images) {
                    emb.setImage(img.getUrl())
                            .setFooter(img.getSource())
                            .setTitle(img.getId(), img.getPostUrl());
                    event.getChannel().sendMessage(emb.build()).queue();
                }
            }

        }

        @Override
        public String toString() {
            return "Searches images by tags on Gelbooru. Tags are separated by a +\n"
                    + "Syntax is <prefix>search <tags> <amount> <page>";
        }
    },
    /**
     * Sends the invite link for the bot
     */
    INVITE("invite") {
        @Override
        void run(AkanBot bot, MessageReceivedEvent event, Matcher matcher) {
            event.getChannel().sendMessage("Invite link: " + "https://discordapp.com/oauth2/authorize?&client_id="
                    + event.getJDA().getSelfUser().getId() + "&scope=bot&permissions=8").queue();
        }

        @Override
        public String toString() {
            return "Sends the invite-link for the bot";
        }
    },
    /**
     * Help command that checks if a command with than name exists and then sends its toString to the channel.
     * Just a temporary help command.
     */
    HELP("help (" + CMD.NAMING_REGEX + ")") {
        @Override
        void run(AkanBot bot, MessageReceivedEvent event, Matcher matcher) {
            EmbedBuilder emb = new EmbedBuilder();
            String input = matcher.group(1);
            for (CMD c : CMD.values()) {
                if (input.equals(c.name().toLowerCase())) {
                    emb.setTitle(c.name())
                            .setThumbnail(event.getGuild().getIconUrl())
                            .setDescription(c.toString());
                    event.getChannel().sendMessage(emb.build()).queue();
                }
            }
        }
    };
    
    private static final String INT_REGEX = "[0-9]+";
    private static final String NAMING_REGEX = "[\\p{L}_$&+,:;=?@#'<>.^*()%!-]+";
    private static final String PREFIX_REGEX = "[\\p{L}_$&+,:;=?@#'<>.^*()%!-]+";
    private static final String[] OWNERS = {"208979474988007425", "244607816587935746"};
    private final Pattern pattern;

    CMD(final String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    abstract void run(AkanBot bot, MessageReceivedEvent event, Matcher matcher);

    private static boolean userIsOwner(final User user) {
        for (String id : CMD.OWNERS) {
            if (user.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public static void execute(final AkanBot bot, final MessageReceivedEvent event, final String input) {
        if (event.getAuthor().isBot()) {
            return;
        }
        for (final CMD i : values()) {
            final Matcher matcher = i.pattern.matcher(input);
            if (matcher.matches()) {
                i.run(bot, event, matcher);
                return;
            }
        }
        throw new IllegalArgumentException("unknown command");
    }

}
