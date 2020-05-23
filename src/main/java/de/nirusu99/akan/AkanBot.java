package de.nirusu99.akan;

import de.nirusu99.akan.ui.CMD;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.net.URISyntaxException;

public class AkanBot extends ListenerAdapter {
    public static final String DEFAULT_PREFIX = "akan!";
    private static final String TOKEN = "token";
    private final JDABuilder jda;
    private String prefix = AkanBot.DEFAULT_PREFIX;

    AkanBot(final String token) throws LoginException, InterruptedException {
        this.jda = JDABuilder.createDefault(token);
        jda.addEventListeners(this).setActivity(Activity.playing("Hewwo Senpai")).build().awaitReady();
        jda.setAutoReconnect(true)
                .setStatus(OnlineStatus.ONLINE);
    }

    public static void main(String[] args){
        try {
            AkanBot.start();
        } catch (LoginException | URISyntaxException | InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void start() throws LoginException, URISyntaxException, InterruptedException {
        File file = new File(AkanBot.class.getResource(File.separator).toURI().getPath() + AkanBot.TOKEN);
        final String token;
        try {
            token = new BufferedReader(new FileReader(file)).readLine();
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        if (token == null) {
            throw new IllegalArgumentException("token is empty!");
        }
        new AkanBot(token);
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public JDABuilder getJda() {
        return jda;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Message msg = event.getMessage();
        if (msg.getContentRaw().startsWith(this.prefix)) {
            try {
                CMD.execute(this, event, msg.getContentRaw().replace(this.prefix,""));
            } catch (IllegalArgumentException e) {
                event.getChannel().sendMessage(e.getMessage()).queue();
            }
        }
        /*
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
        }*/
    }

}
