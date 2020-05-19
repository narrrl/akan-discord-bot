package de.nirusu99.akan;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URISyntaxException;

public class Bot extends ListenerAdapter {
    private static final String TOKEN = "token";
    private static String PREFIX = "akan!";
    private static final String NIRUSU_ID = "208979474988007425";
    public static void start() throws LoginException, URISyntaxException {
        File file = new File(Bot.class.getResource(File.separator).toURI().getPath() + Bot.TOKEN);
        final String token;
        try {
            token = new BufferedReader(new FileReader(file)).readLine();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }
        if (token == null) {
            System.err.println("token is empty!");
            return;
        }
        build(token);
    }

    public static void build(final String token)
            throws LoginException
    {
        JDABuilder jda = JDABuilder.createDefault(token);
        jda.addEventListeners(new Bot()).setActivity(Activity.playing("Hewwo Senpai")).build();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Message msg = event.getMessage();
        if (msg.getContentRaw().equals(Bot.PREFIX + "ping"))
        {
            MessageChannel channel = event.getChannel();
            long time = System.currentTimeMillis();
            channel.sendMessage("Pong!")
                    .queue(response -> {
                        response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time).queue();
                    });
        } else if (msg.getContentRaw().equals(Bot.PREFIX + "stop")) {
            event.getChannel().sendMessage("Bai Bai <:megu:666743067755151360>").queue();
            System.exit(0); // I'm sry
        }
    }
}
