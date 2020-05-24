package de.nirusu99.akan.ui;

import de.nirusu99.akan.AkanBot;
import de.nirusu99.akan.images.Image;
import de.nirusu99.akan.images.ImageSearch;
import de.nirusu99.akan.utils.EmoteConverter;
import de.nirusu99.akan.utils.Host;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.File;
import java.io.IOException;
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

        @Override
        String syntax() {
            return "<prefix>exit";
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
                            .editMessageFormat("Pong: %d ms <a:loading:529887640472911922>",
                                    System.currentTimeMillis() - time).queue());
        }

        @Override
        String syntax() {
            return "<prefix>ping";
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
        String syntax() {
            return "<prefix>prefix <new prefix>";
        }

        @Override
        public String toString() {
            return "Changes the prefix of the bot. Only bot owner can do that";
        }
    },
    /**
     *
     */
    SEARCH("search ("+ Host.HOSTS_REGEX +") (" + CMD.TAGS_REGEX + ") ?("
            + CMD.INT_REGEX + ")? ?(" + CMD.INT_REGEX + ")?") {
        @Override
        void run(AkanBot bot, MessageReceivedEvent event, Matcher matcher) {
            int amount;
            int page;
            if (matcher.group(3) != null) {
                amount = Integer.parseInt(matcher.group(3));
            } else {
                amount = 1;
            }
            if (matcher.group(4) != null) {
                page = Integer.parseInt(matcher.group(4));
            } else {
                page = 1;
            }
            if (amount > 5) {
                throw new IllegalArgumentException("You can't search for more then 5 pics");
            }
            if (amount < 1) {
                throw new IllegalArgumentException("You can't search for less then 1 pic");
            }
            if (page < 1) {
                throw new IllegalArgumentException("You can't search for pages less then 1");
            }
            List<String> tags = Arrays.asList(matcher.group(2).split("\\+", -1));
            Image[] images = ImageSearch.searchFor(tags, amount, page, Host.getHost(matcher.group(1)));
            if (images.length == 0) {
                StringBuilder out = new StringBuilder();
                tags.forEach(out::append);
                event.getChannel().sendMessage("no pictures found for tag " + out.toString()).queue();
            } else {
                EmbedBuilder emb;
                for (Image img : images) {
                    emb = new EmbedBuilder();
                    if (!img.isVideo()) {
                        emb.setImage(img.getUrl())
                                .setFooter(img.getSource())
                                .setTitle(img.getId(), img.getPostUrl());
                        event.getChannel().sendMessage(emb.build()).queue();
                    } else {
                        event.getChannel().sendMessage(img.getUrl()).complete();
                    }
                }
            }

        }

        @Override
        String syntax() {
            return "<prefix>search <gelbooru|safebooru> <tag+tag+tag...> <amount> <page>";
        }

        @Override
        public String toString() {
            return "Searches images by tags on Gelbooru";
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
        String syntax() {
            return "<prefix>invite";
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
            for (String out : EmoteConverter.convertRegionalIndicators(matcher.group(1)).split(" ", -1)) {
                event.getChannel().sendMessage(out).queue();
            }
        }

        @Override
        public String toString() {
            return "Sends the user message converted to emotes";
        }

        @Override
        String syntax() {
            return "<prefix>rep <message>";
        }
    },
    /**
     * Set the bot status
     */
    STATUS("status (" + CMD.ACTIVITY_TYPE_REGEX + ") (" + CMD.STATUS_REGEX + ")") {
        @Override
        void run(AkanBot bot, MessageReceivedEvent event, Matcher matcher) {
            if (CMD.userIsOwner(event.getAuthor())) {
                switch (matcher.group(1)) {
                    case "playing":
                        event.getJDA().getPresence().setActivity(Activity.playing(matcher.group(2)));
                        break;
                    case "listening":
                        event.getJDA().getPresence().setActivity(Activity.listening(matcher.group(2)));
                        break;
                    case "watching":
                        event.getJDA().getPresence().setActivity(Activity.watching(matcher.group(2)));
                        break;
                    case "streaming":
                        String[] str = matcher.group(2).split(" ", 2);
                        if (str.length != 2) {
                            throw new IllegalArgumentException("a!status streaming <streamtitle> <url>");
                        }
                        event.getJDA().getPresence().setActivity(Activity.streaming(str[0], str[1]));
                        break;
                    default:
                        event.getChannel().sendMessage("invalid status type").queue();
                }
            } else {
                event.getChannel().sendMessage("Only owner can set the bot status").queue();
            }
        }

        @Override
        String syntax() {
            return "<prefix>status (listening|playing|watching) <message>\n"
                    + "<prefix>status streaming <<message> <url>>";
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
                            .setDescription("Description:\n" + c.toString()
                                    + LINE_BREAK + "Syntax:\n" + c.syntax());
                    event.getChannel().sendMessage(emb.build()).queue();
                }
            }
        }

        @Override
        public String toString() {
            return "Shows help for specified commands";
        }

        @Override
        String syntax() {
            return "<prefix>help <command>";
        }
    },
    LIST("(help|list)") {
        @Override
        void run(AkanBot bot, MessageReceivedEvent event, Matcher matcher) {
            StringBuilder out = new StringBuilder();
            EmbedBuilder emb = new EmbedBuilder();
            int counter = 1;
            for (CMD c : CMD.values()) {
                out.append(c.name().toLowerCase());
                if (counter != CMD.values().length) {
                    if (counter % 2 == 0) {
                        out.append(LINE_BREAK);
                    } else {
                        out.append(", ");
                    }
                }
                counter++;
            }
            out.append(LINE_BREAK + LINE_BREAK + "See more with ").append(bot.getPrefix()).append("help <command>");
            emb.setTitle("Commands:")
                    .setThumbnail(event.getGuild().getIconUrl())
                    .setDescription(out.toString());
            event.getChannel().sendMessage(emb.build()).queue();
        }

        @Override
        String syntax() {
            return "<prefix>help or <prefix>list";
        }

        @Override
        public String toString() {
            return "list all commands";
        }
    },
    CHECK("check ("+ CMD.BOOLEAN_REGEX +")") {
        @Override
        void run(AkanBot bot, MessageReceivedEvent event, Matcher matcher) {
            if (CMD.userIsOwner(event.getAuthor())) {
                bot.setCheckMark(Boolean.parseBoolean(matcher.group(1)));
            } else {
                event.getChannel().sendMessage("Only owner can do that").queue();
            }
        }

        @Override
        String syntax() {
            return "<prefix>check <true|false>";
        }

        @Override
        public String toString() {
            return "reacts to your message when it gets executed";
        }
    },
    /**
     * Resets the avatar for some reason?
     */
    SETAVATAR("setavatar") {
        @Override
        void run(AkanBot bot, MessageReceivedEvent event, Matcher matcher) {
            if(CMD.userIsOwner(event.getAuthor())) {
                List<Message.Attachment> attachment = event.getMessage().getAttachments();
                if (attachment.size() != 1) throw new IllegalArgumentException("You must attach one image");
                File file = new File(attachment.get(0).getFileName());
                attachment.get(0).downloadToFile(file);
                Icon icon;
                try {

                    icon = Icon.from(file);
                } catch (IOException e) {
                    if (!file.delete()) System.err.println("couldn't delete picture!");
                    throw new IllegalArgumentException(e.getMessage());
                }
                event.getJDA().getSelfUser().getManager().setAvatar(icon).queue();
                event.getChannel().sendMessage("updated avatar!").complete();
                if (!file.delete()) System.err.println("couldn't delete picture!");
            } else {
                event.getChannel().sendMessage("Only the bot owner can change the avatar").complete();
            }
        }

        @Override
        String syntax() {
            return "<prefix>setavatar";
        }

        @Override
        public String toString() {
            return "changes the avatar of the bot. You have to attach a image";
        }
    },
    AVATAR("avatar <@!(" + CMD.USER_REGEX + ")>") {
        @Override
        void run(AkanBot bot, MessageReceivedEvent event, Matcher matcher) {
            Member user = event.getGuild().getMemberById(matcher.group(1));
            if (user == null) throw new IllegalArgumentException("couldn't find user");
            EmbedBuilder emb = new EmbedBuilder();
            emb.setTitle(user.getUser().getAsTag(),user.getUser().getAvatarUrl())
                    .setImage(user.getUser().getEffectiveAvatarUrl() + "?size=256");
            event.getChannel().sendMessage(emb.build()).complete();
        }

        @Override
        String syntax() {
            return "<prefix>avatar <@User>";
        }

        @Override
        public String toString() {
            return "Gets the avatar of the specified user";
        }
    };

    private static final String USER_REGEX = "[0-9]+";
    private static final String LINE_BREAK = "\n";
    private static final String ACTIVITY_TYPE_REGEX = "playing|watching|listening|streaming";
    private static final String TAGS_REGEX = "[\\p{L}\\d" + CMD.SPECIAL_CHARS + "]+";
    private static final String STATUS_REGEX = "[\\p{L}_$&+,:;=?@#'<>.^*()%!\\- 0-9\\/]+";
    private static final String REP_REGEX = "[A-Za-z0-9 ]+";
    private static final String SPECIAL_CHARS = "_$&+,:;=?@#'<>.^*()%!-";
    private static final String INT_REGEX = "\\d+";
    private static final String NAMING_REGEX = "[\\p{L}_$&+,:;=?@#'<>.^*()%!-]+";
    private static final String PREFIX_REGEX = "[\\p{L}_$&+,:;=?@#'<>.^*()%!-]+";
    private static final String[] OWNERS = {"208979474988007425", "244607816587935746"};
    private static final String BOOLEAN_REGEX = "true|false";
    private final Pattern pattern;

    CMD(final String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    abstract void run(AkanBot bot, MessageReceivedEvent event, Matcher matcher);
    abstract String syntax();

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
                if (bot.isCheckMark()) {
                    event.getMessage().addReaction("ayayayhyper:567486942086692872").queue();
                }
                return;
            }
        }
        throw new IllegalArgumentException("unknown command");
    }

}
