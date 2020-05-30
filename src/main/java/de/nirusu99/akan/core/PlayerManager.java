package de.nirusu99.akan.core;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.nirusu99.akan.commands.CommandContext;
import net.dv8tion.jda.api.entities.Guild;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * This class handles all the music for the bot.
 *
 * @author Nils Pukropp
 * @version 1.2
 */
public class PlayerManager {
    private static PlayerManager instance;
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public synchronized GuildMusicManager getGuildMusicManager(@Nonnull final Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }


    public void loadAndPlay(@Nonnull final CommandContext ctx, @Nonnull  String trackUrl) {
        GuildMusicManager musicManager = getGuildMusicManager(ctx.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(@Nonnull AudioTrack track) {
                play(musicManager, track);
                ctx.getChannel().sendTyping().queue(rep ->
                        ctx.getChannel().sendMessage("Loaded: " + track.getInfo().author
                                + "-" + track.getInfo().title).queue());
            }

            @Override
            public void playlistLoaded(@Nonnull final AudioPlaylist playlist) {
                playlist.getTracks().forEach(track -> play(musicManager, track));
                ctx.getChannel().sendTyping().queue(rep ->
                        ctx.getChannel().sendMessage("Loaded: Playlist: " + playlist.getName()).queue());
            }

            @Override
            public void noMatches() {
                ctx.getChannel().sendTyping().queue(rep ->
                        ctx.getChannel().sendMessage("Couldn't find: " + trackUrl).queue());
            }

            @Override
            public void loadFailed(@Nonnull FriendlyException exception) {
                ctx.getBot().printInfo(exception.getMessage());
                ctx.getChannel().sendTyping().queue(rep ->
                        ctx.getChannel().sendMessage("Well done, you destroyed the bot, volvo pls fix...").queue());
            }
        });
    }

    private void play(@Nonnull final GuildMusicManager musicManager, @Nonnull final AudioTrack track) {
        musicManager.scheduler.queue(track);
    }

    public void pause(@Nonnull final GuildMusicManager musicManager, final boolean pause) {
        musicManager.player.setPaused(pause);
    }

    public void destroy(@Nonnull final Guild guild) {
        musicManagers.get(guild.getIdLong()).player.destroy();
        musicManagers.remove(guild.getIdLong());
    }

    public void next(@Nonnull final GuildMusicManager musicManager) {
        musicManager.scheduler.nextTrack();
    }

    public AudioTrack getPlaying(@Nonnull final GuildMusicManager musicManager) {
        return musicManager.player.getPlayingTrack();
    }

    public static synchronized PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }

        return instance;
    }
}
