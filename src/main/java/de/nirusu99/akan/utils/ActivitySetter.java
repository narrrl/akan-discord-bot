package de.nirusu99.akan.utils;

import de.nirusu99.akan.AkanBot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.sharding.ShardManager;

public enum ActivitySetter {
    PLAYING("playing") {
        @Override
        Activity setActivity(String input) {
            return Activity.playing(input);
        }
    },LISTENING("listening") {
        @Override
        Activity setActivity(String input) {
            return Activity.listening(input);
        }
    },WATCHING("watching") {
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

    abstract Activity setActivity(final String input);
}
