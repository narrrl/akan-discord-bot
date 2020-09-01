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
import java.util.ArrayList;
import java.awt.Color;

@MetaInfServices(ICommand.class)
public final class List implements ICommand {
    private static final Pattern PATTERN = Pattern.compile("list");

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
        emb.setColor(Color.PINK).setThumbnail(ctx.getGuild().getIconUrl())
            .setTitle("Current Queue:");

        ArrayList<AudioTrackInfo> tracks = musicManager.getScheduler()
            .getAllTrackInfos();

        StringBuilder out = new StringBuilder();

        out.append("Current: [" + track.getInfo().title + "](" + track.getInfo().uri
                + ")\n");

        int it = 1;

        int totalEmbs = 0;

        for (AudioTrackInfo i : tracks) {

            if (totalEmbs == 2) break;

            out.append(it + ": [" + i.title + "](" + i.uri + ")\n");

            if (out.length() > 1800) {

                emb.setDescription(out.toString().substring(0, out.length()));
                ctx.reply(emb.build());
                out = new StringBuilder();

                totalEmbs++;

            }

            it++;
        }

       if (out.length() != 0) {
           emb.setDescription(out.toString().substring(0, out.length()));
           ctx.reply(emb.build());

       }

    }

    @Override
    public String getName() {
        return "list";
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
