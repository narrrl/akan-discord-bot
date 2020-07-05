package de.nirusu99.akan.core;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import java.util.Collections;
import java.util.ArrayList;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.List;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

/**
 * This class schedules tracks for the audio player. It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;

    /**
     * @param player The audio player this scheduler uses
     */
    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     *
     * @param track The track to play or add to queue.
     */
    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    public synchronized void shuffle() {
        ArrayList<AudioTrack> tmp = new ArrayList<>();

        for (Object o : queue.toArray()) {
            tmp.add((AudioTrack) o);
        }

        Collections.shuffle(tmp);

        queue.clear();

        for (AudioTrack t : tmp) {
            queue.add(t);
        }


    }

    public synchronized ArrayList<AudioTrackInfo> getAllTrackInfos() {
        ArrayList<AudioTrackInfo> tmp = new ArrayList<>();

        for (Object o : queue.toArray()) {
            tmp.add(((AudioTrack) o).getInfo());
        }

        return tmp;
    }

    /**
     * Start the next track, stopping the current one if it is playing.
     */
    public void nextTrack() {
        player.startTrack(queue.poll(), false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            nextTrack();
        }
    }
}
