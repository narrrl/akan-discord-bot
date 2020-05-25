package de.nirusu99.akan.utils;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public enum ActivitySetter {
    PLAYING("playing") {
        @Override
        void setActivity(String input, final MessageReceivedEvent event) {
            event.getJDA().getPresence().setActivity(Activity.playing(input));
        }
    },STREAMING("listening") {
        @Override
        void setActivity(String input, final MessageReceivedEvent event) {
            event.getJDA().getPresence().setActivity(Activity.listening(input));
        }
    },WATCHING("watching") {
        @Override
        void setActivity(String input, final MessageReceivedEvent event) {
            event.getJDA().getPresence().setActivity(Activity.watching(input));
        }
    },LISTENING("streaming") {
        @Override
        void setActivity(String input, final MessageReceivedEvent event) {
            String[] str = input.split(" ", 2);
            if (str.length != 2) {
                throw new IllegalArgumentException("a!status streaming <streamtitle> <url>");
            }
            event.getJDA().getPresence().setActivity(Activity.streaming(str[0], str[1]));
        }
    };

    private final String type;

    ActivitySetter(final String type) {
        this.type = type;
    }

    public static void set(final String type, final String input, final MessageReceivedEvent event) {
        for (ActivitySetter a : ActivitySetter.values()) {
            if (a.type.equals(type)) {
                a.setActivity(input, event);
                return;
            }
        }
        throw new IllegalArgumentException("invalid status type");
    }

    abstract void setActivity(final String input, final MessageReceivedEvent event);
}
