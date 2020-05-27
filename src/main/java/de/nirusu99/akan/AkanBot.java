package de.nirusu99.akan;

import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.commands.CommandBuilder;
import de.nirusu99.akan.core.Config;
import de.nirusu99.akan.core.Logger;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class AkanBot extends ListenerAdapter {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AkanBot.class);
    private static final String[] OWNERS = {"208979474988007425", "244607816587935746"};
    private final Config conf;
    private final Logger log;
    private String prefix;

    AkanBot(final boolean sharding) throws LoginException {
        conf = new Config();
        log = new Logger(this);
        this.prefix = conf.getPrefix();
        if (sharding) {
            DefaultShardManagerBuilder.createDefault(conf.getToken())
                    .setActivity(Activity.listening("a!help"))
                    .setAutoReconnect(true)
                    .setStatus(OnlineStatus.ONLINE)
                    .addEventListeners(this).build();
        } else {
            JDABuilder.createDefault(conf.getToken())
                    .setActivity(Activity.listening("a!help"))
                    .setAutoReconnect(true)
                    .setStatus(OnlineStatus.ONLINE)
                    .addEventListeners(this).build();
        }
    }


    /**
     * Starts the bot
     * @param args ---
     */
    public static void main(String[] args) {
        try {
            new AkanBot(true);
        } catch (LoginException e) {
            AkanBot.LOGGER.info(e.getMessage());
        }
    }

    public boolean withSuccessReaction() {
        return conf.withSuccessReaction();
    }

    public void setSuccessReaction(boolean checkMark) {
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

    public static boolean userIsOwner(final User user) {
        for (String id : AkanBot.OWNERS) {
            if (user.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();
        if (user.isBot()) {
            return;
        }
        Message msg = event.getMessage();
        if (msg.getContentRaw().startsWith(this.prefix)) {
            String contentRaw = msg.getContentRaw().substring(this.prefix.length());
            ICommand cmd = CommandBuilder.createCommand(contentRaw);
            if (cmd == null) {
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("unknown command").queue(response -> response.
                        editMessage("unknown command <:KEKW:715043065190154282>").queue());
            } else {
                try {
                    String[] split = contentRaw.split("\\s+");
                    log.addLog(event, cmd);
                    if (withSuccessReaction()) {
                        event.getMessage().addReaction("ayayayhyper:567486942086692872").queue();
                    }
                    cmd.run(new CommandContext(event, Arrays.asList(split).subList(1,split.length), this));
                } catch (IllegalArgumentException e) {
                    event.getChannel().sendTyping().queue();
                    event.getChannel().sendMessage(e.getMessage()).complete();
                }
            }
        }
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.info("{} ready", event.getJDA().getSelfUser().getAsTag());
    }




}
