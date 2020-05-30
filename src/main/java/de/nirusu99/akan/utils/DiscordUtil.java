package de.nirusu99.akan.utils;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

import javax.annotation.Nonnull;

public class DiscordUtil {

    /**
     * Don't instantiate
     */
    private DiscordUtil() {
        throw new IllegalAccessError();
    }

    public static VoiceChannel findVoiceChannel(@Nonnull Member member) {
        GuildVoiceState state = member.getVoiceState();
        return state != null ? state.getChannel() : null;
    }
}
