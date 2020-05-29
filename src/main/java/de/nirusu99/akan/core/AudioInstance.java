package de.nirusu99.akan.core;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.nirusu99.akan.commands.CommandContext;

public final class AudioInstance {
    private final AudioPlayer player;
    private final TrackScheduler scheduler;
    private final AudioPlayerManager manager;
    private final AudioPlayerSendHandler handler;

    public AudioInstance(AudioPlayerManager manager) {
        this.manager = manager;
        this.player = manager.createPlayer();
        this.scheduler = new TrackScheduler(this.player);
        player.addListener(scheduler);
        this.handler = new AudioPlayerSendHandler(this.player);
    }

    public void load(final String id, CommandContext ctx) {
        manager.loadItem(id, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                scheduler.queue(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (AudioTrack track : playlist.getTracks()) {
                    scheduler.queue(track);
                }
            }


            @Override
            public void noMatches() {
                ctx.getChannel().sendTyping().queue(rep ->
                        ctx.getChannel().sendMessage("Track not found!").queue());
            }

            @Override
            public void loadFailed(FriendlyException throwable) {
                ctx.getBot().printInfo(throwable.getMessage());
            }
        });
    }

    public AudioPlayerSendHandler getHandler() {
        return handler;
    }

    public AudioPlayerManager getManager() {
        return manager;
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public TrackScheduler getScheduler() {
        return scheduler;
    }
}

