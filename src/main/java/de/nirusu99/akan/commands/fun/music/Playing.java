package de.nirusu99.akan.commands.fun.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.core.GuildMusicManager;
import de.nirusu99.akan.core.PlayerManager;
import org.kohsuke.MetaInfServices;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.Color;

@MetaInfServices(ICommand.class)
public final class Playing implements ICommand {
    private static final Pattern PATTERN = Pattern.compile("playing");

    @Override
    public void run(CommandContext ctx) {
        PlayerManager manager = PlayerManager.getInstance();
        GuildMusicManager musicManager = manager.getGuildMusicManager(ctx.getGuild());
        final AudioTrack track = manager.getPlaying(musicManager);

        if (track == null) {
            ctx.reply("No music is playing!");
            return;
        }

        EmbedBuilder emb = new EmbedBuilder();
        AudioTrackInfo info = track.getInfo();
        emb.setColor(Color.PINK).setThumbnail(ctx.getGuild().getIconUrl())
            .setTitle("Now playing:", info.uri)
            .setDescription(info.author + " - " + info.title);
        ctx.reply(emb.build());
    }

    @Override
    public String getName() {
        return "playing";
    }

    @Override
    public String syntax() {
        return "<prefix>playing";
    }

    @Override
    public String gifHelpUrl() {
        return null;
    }

    @Override
    public String toString() {
        return "Displays current playing song";
    }

    @Override
    public Matcher matches(final String input) {
        return PATTERN.matcher(input);
    }
}
