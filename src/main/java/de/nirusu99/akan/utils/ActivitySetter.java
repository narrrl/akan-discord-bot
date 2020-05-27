package de.nirusu99.akan.utils;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public enum ActivitySetter {
    PLAYING("playing") {
        @Override
        void setActivity(String input, final GuildMessageReceivedEvent event) {
            event.getJDA().getPresence().setActivity(Activity.playing(input));
        }
    },LISTENING("listening") {
        @Override
        void setActivity(String input, final GuildMessageReceivedEvent event) {
            event.getJDA().getPresence().setActivity(Activity.listening(input));
        }
    },WATCHING("watching") {
        @Override
        void setActivity(String input, final GuildMessageReceivedEvent event) {
            event.getJDA().getPresence().setActivity(Activity.watching(input));
        }
    };

    private final String type;

    ActivitySetter(final String type) {
        this.type = type;
    }

    public static void set(final String type, final String input, final GuildMessageReceivedEvent event) {
        for (ActivitySetter a : ActivitySetter.values()) {
            if (a.type.equals(type)) {
                a.setActivity(input, event);
                return;
            }
        }
        throw new IllegalArgumentException("invalid status type");
    }

    abstract void setActivity(final String input, final GuildMessageReceivedEvent event);
}
