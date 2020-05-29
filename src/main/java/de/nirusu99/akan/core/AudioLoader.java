package de.nirusu99.akan.core;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public final class AudioLoader implements AudioLoadResultHandler {
    private final TrackScheduler scheduler;

    public AudioLoader(TrackScheduler scheduler) {
        this.scheduler = scheduler;
    }

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
        //TODO implement
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        throw new IllegalArgumentException(exception.getMessage());
    }
}
