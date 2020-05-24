package de.nirusu99.akan;

import de.nirusu99.akan.ui.CMD;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AkanBot extends ListenerAdapter {
    private static final String DEFAULT_PREFIX = "a!";
    private final JDABuilder jda;
    private String prefix;
    private boolean checkMark;

    AkanBot(final String token, final String prefix) throws LoginException, InterruptedException {
        this.prefix = prefix;
        this.jda = JDABuilder.createDefault(token);
        jda.addEventListeners(this)
                .setActivity(Activity.playing("Hewwo Senpai")).build().awaitReady();
        jda.setAutoReconnect(true)
                .setStatus(OnlineStatus.ONLINE);
        checkMark = false;
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
        File file = new File(new File("").getAbsolutePath().concat("/build/resources/main/"
                + "de/nirusu99/akan/config.json"));
        final String token;
        final String prefix;
        JSONObject obj = new JSONObject();
        try {
            JSONParser parser = new JSONParser();
            obj = (JSONObject) parser.parse(new FileReader(file));
        } catch (IOException | ParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        token = (String) obj.get("token");
        prefix = (String) obj.get("prefix");
        if (token == null) {
            throw new IllegalArgumentException("token is empty!");
        }
        new AkanBot(token, prefix);
    }

    public boolean isCheckMark() {
        return checkMark;
    }

    public void setCheckMark(boolean checkMark) {
        this.checkMark = checkMark;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public JDABuilder getJDA() {
        return jda;
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
