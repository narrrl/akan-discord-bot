package de.nirusu99.akan.commands.fun.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.core.GuildMusicManager;
import de.nirusu99.akan.core.PlayerManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Next implements ICommand {
    private static final Pattern PATTERN = Pattern.compile("next");

    @Override
    public void run(CommandContext ctx) {
        PlayerManager manager = PlayerManager.getInstance();
        GuildMusicManager musicManager = manager.getGuildMusicManager(ctx.getGuild());
        AudioTrack prev = musicManager.player.getPlayingTrack();
        if (prev == null) {
            ctx.getChannel().sendTyping().queue(rep ->
                    ctx.getChannel().sendMessage("Nothing is playing!").queue());
            return;
        }
        manager.next(musicManager);
        AudioTrack next = musicManager.player.getPlayingTrack();
        String prevText = prev.getInfo().author + " - " + prev.getInfo().title;
        String nextText = next != null ? next.getInfo().author + " - " + next.getInfo().title : "End of queue";
        ctx.getChannel().sendTyping().queue(rep ->
                ctx.getChannel().sendMessage("Skipped: " + prevText).queue(rep2 ->
                        ctx.getChannel().sendMessage("Now playing: " + nextText).queue()));
    }

    @Override
    public String getName() {
        return "next";
    }

    @Override
    public String syntax() {
        return "<prefix>next";
    }

    @Override
    public String gifHelpUrl() {
        return null;
    }

    @Override
    public String toString() {
        return "Plays the next track";
    }

    @Override
    public Matcher matches(String input) {
        return PATTERN.matcher(input);
    }
}
