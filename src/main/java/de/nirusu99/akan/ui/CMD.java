package de.nirusu99.akan.ui;

import de.nirusu99.akan.AkanBot;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CMD {
    EXIT("exit") {
        @Override
        void run(final AkanBot bot, final MessageReceivedEvent event, final Matcher matcher) {
            if (CMD.userIsOwner(event.getAuthor())) {
                System.exit(0);
            }
        }
    },
    PING("ping") {
        @Override
        void run(AkanBot bot, MessageReceivedEvent event, Matcher matcher) {
            MessageChannel channel = event.getChannel();
            long time = System.currentTimeMillis();
            channel.sendMessage("Pong!")
                    .queue(response -> {
                        response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time).queue();
                    });
        }
    },
    SET_PREFIX("prefix (" + CMD.PREFIX_REGEX + ")") {
        @Override
        void run(AkanBot bot, MessageReceivedEvent event, Matcher matcher) {
            if (CMD.userIsOwner(event.getAuthor())) {
                String newPrefix = matcher.group(1);
                bot.setPrefix(newPrefix);
                event.getChannel()
                        .sendMessage("Prefix was set to " + newPrefix + " <:remV:639621688887083018>")
                        .queue();
            }
        }
    },
    RANDOM_IMAGE("") {
        @Override
        void run(AkanBot bot, MessageReceivedEvent event, Matcher matcher) {

        }
    };
    
    private static final String EMPTY_STRING = "";
    private static final String PREFIX_REGEX = "[\\p{L}$&+,:;=?@#|'<>.^*()%!-]+";
    private static String[] OWNERS = {"208979474988007425","244607816587935746"};
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
