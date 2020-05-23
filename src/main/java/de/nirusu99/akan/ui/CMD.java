package de.nirusu99.akan.ui;

import de.nirusu99.akan.AkanBot;
import de.nirusu99.akan.images.GelbooruImage;
import de.nirusu99.akan.images.ImageSearch;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

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
                event.getChannel().sendMessage("<:megu:666743067755151360>").complete();
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
                            .editMessageFormat("Pong: %d ms <a:loading:529887640472911922>"
                                    , System.currentTimeMillis() - time).queue());
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
    SEARCH("search (" + CMD.TAGS_REGEX + ") (" + CMD.INT_REGEX + ") (" + CMD.INT_REGEX + ")") {
        @Override
        void run(AkanBot bot, MessageReceivedEvent event, Matcher matcher) {
            if (event.isFromGuild() && !event.getTextChannel().isNSFW()) {
                event.getChannel().sendMessage("Not here, senpai <a:blushDS:639619041920548884>").queue();
                return;
            }
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
     * under constuction. Converts input to emotes.
     */
    REP("rep (" + CMD.REP_REGEX + ")") {
        @Override
        void run(AkanBot bot, MessageReceivedEvent event, Matcher matcher) {
            event.getMessage().delete().queue();
            char[] chars = matcher.group(1).toLowerCase().toCharArray();
            String[] output = new String[chars.length];
            for (int i = 0; i < output.length; i++) {
                switch (chars[i]) {
                    case '0':
                        output[i] = ":zero:";
                        break;
                    case '1':
                        output[i] = ":one:";
                        break;
                    case '2':
                        output[i] = ":two:";
                        break;
                    case '3':
                        output[i] = ":three:";
                        break;
                    case '4':
                        output[i] = ":four:";
                        break;
                    case '5':
                        output[i] = ":five:";
                        break;
                    case '6':
                        output[i] = ":six:";
                        break;
                    case '7':
                        output[i] = ":seven:";
                        break;
                    case '8':
                        output[i] = ":eight:";
                        break;
                    case '9':
                        output[i] = ":nine:";
                        break;
                    case ' ':
                        output[i] = " ";
                        break;
                    default:
                        output[i] = ":regional_indicator_" + chars[i] + ":";
                }
            }
            StringBuilder out = new StringBuilder();
            for (String str : output) {
                out.append(str);
            }
            event.getChannel().sendMessage(out.toString().replace(" ", "\n")).queue();
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
    
    private static final String TAGS_REGEX = "[\\p{L}\\d" + CMD.SPECIAL_CHARS + "]+";
    private static final String REP_REGEX = "[A-Za-z0-9 ]+";
    private static final String SPECIAL_CHARS = "_$&+,:;=?@#'<>.^*()%!-";
    private static final String INT_REGEX = "\\d+";
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
