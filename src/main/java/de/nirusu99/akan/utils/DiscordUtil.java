package de.nirusu99.akan.utils;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

import javax.annotation.Nonnull;

public final class DiscordUtil {

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

    public static boolean areInSameVoice(@Nonnull Member member1, @Nonnull Member member2) {
        VoiceChannel channel = DiscordUtil.findVoiceChannel(member1);

        if (channel == null) return false;

        VoiceChannel botChannel = DiscordUtil.findVoiceChannel(member2);

        if (!channel.equals(botChannel)) return false;

        return true;
    }


}
