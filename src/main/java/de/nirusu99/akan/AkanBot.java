package de.nirusu99.akan;

import de.nirusu99.akan.core.Config;
import de.nirusu99.akan.ui.CMD;
import java.io.File;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AkanBot extends ListenerAdapter {
    private static final String DEFAULT_PREFIX = "a!";
    private final Config conf;
    private String prefix;

    AkanBot() throws LoginException, InterruptedException {
        conf = new Config();
        this.prefix = conf.getPrefix();
        JDABuilder jda = JDABuilder.createDefault(conf.getToken());
        jda.addEventListeners(this)
                .setActivity(Activity.playing("Hewwo Senpai")).build().awaitReady();
        jda.setAutoReconnect(true)
                .setStatus(OnlineStatus.ONLINE);
    }

    /**
     * Starts the bot
     * @param args ---
     */
    public static void main(String[] args) {
        try {
            AkanBot.start();
        } catch (LoginException | InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void start() throws LoginException, InterruptedException {
        new AkanBot();
    }

    public boolean isCheckMark() {
        return conf.withCheckMark();
    }

    public void setCheckMark(boolean checkMark) {
        conf.setCheckMark(checkMark);
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        if (msg.getContentRaw().startsWith(this.prefix)) {
            try {
                CMD.execute(this, event, msg.getContentRaw().substring(this.prefix.length()));
            } catch (IllegalArgumentException e) {
                event.getChannel().sendMessage(e.getMessage()).queue();
            }
        }
    }

}
