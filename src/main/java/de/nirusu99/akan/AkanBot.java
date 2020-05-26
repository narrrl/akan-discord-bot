package de.nirusu99.akan;

import de.nirusu99.akan.core.Config;
import de.nirusu99.akan.core.Logger;
import de.nirusu99.akan.commands.CMD;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import org.slf4j.LoggerFactory;

public class AkanBot extends ListenerAdapter {
    private final Config conf;
    private final Logger log;
    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AkanBot.class);
    private String prefix;

    AkanBot(final boolean sharding) throws LoginException, InterruptedException {
        conf = new Config();
        log = new Logger(this);
        this.prefix = conf.getPrefix();
        if (sharding) {
            DefaultShardManagerBuilder shardManager = new DefaultShardManagerBuilder();
            shardManager.setToken(conf.getToken())
                    .setActivity(Activity.listening("a!help"))
                    .addEventListeners(this).build();
        } else {
            JDABuilder jda = JDABuilder.createDefault(conf.getToken());
            jda.addEventListeners(this)
                    .setActivity(Activity.playing("Hewwo Senpai")).build().awaitReady();
            jda.setAutoReconnect(true)
                    .setStatus(OnlineStatus.ONLINE);
        }
    }


    /**
     * Starts the bot
     * @param args ---
     */
    public static void main(String[] args) {
        try {
            AkanBot.start();
        } catch (LoginException | InterruptedException e) {
            AkanBot.LOGGER.info(e.getMessage());
        }
    }

    public static void start() throws LoginException, InterruptedException {
        new AkanBot(false);
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

    public Logger getLog() {
        return log;
    }

    public void printInfo(final String info) {
        LOGGER.info(info);
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        Message msg = event.getMessage();
        if (msg.getContentRaw().startsWith(this.prefix)) {
            try {
                CMD.execute(this, event, msg.getContentRaw().substring(this.prefix.length()));
            } catch (IllegalArgumentException e) {
                event.getChannel().sendMessage(e.getMessage()).queue();
            }
        }
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.info("{} ready", event.getJDA().getSelfUser().getAsTag());
    }




}
