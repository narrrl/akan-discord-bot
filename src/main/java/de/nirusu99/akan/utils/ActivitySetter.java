package de.nirusu99.akan.utils;

import de.nirusu99.akan.AkanBot;
import net.dv8tion.jda.api.entities.Activity;

public enum ActivitySetter {
    /**
     * Returns the playing activity type
     */
    PLAYING("playing") {
        @Override
        Activity setActivity(String input) {
            return Activity.playing(input);
        }
    },
    /**
     * Returns the listening activity type
     */
    LISTENING("listening") {
        @Override
        Activity setActivity(String input) {
            return Activity.listening(input);
        }
    },
    /**
     * Returns the watching activity type
     */
    WATCHING("watching") {
        @Override
        Activity setActivity(String input) {
            return Activity.watching(input);
        }
    };

    private final String type;

    ActivitySetter(final String type) {
        this.type = type;
    }

    public static Activity set(final String type, final String input, AkanBot bot) {
        for (ActivitySetter a : ActivitySetter.values()) {
            if (a.type.equals(type)) {
                bot.setActivity(input, type);
                return a.setActivity(input);
            }
        }
        throw new IllegalArgumentException("invalid status type");
    }

    abstract Activity setActivity(String input);
}
