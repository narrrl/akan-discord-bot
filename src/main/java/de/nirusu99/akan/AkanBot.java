package de.nirusu99.akan;

import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.commands.CommandBuilder;
import de.nirusu99.akan.core.Config;
import de.nirusu99.akan.core.Logger;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;

import de.nirusu99.akan.utils.ActivitySetter;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Message;
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

    AkanBot() throws LoginException {
        conf = new Config(this);
        log = new Logger(this);
        this.prefix = conf.getValue("prefix");
        DefaultShardManagerBuilder.createDefault(conf.getValue("token"))
                .setAutoReconnect(true)
                .setActivity(ActivitySetter.set(conf.getValue("activityType"), conf.getValue("activity"),
                        this))
                .setStatus(OnlineStatus.ONLINE)
                .addEventListeners(this).build();
    }


    /**
     * Starts the bot
     *
     * @param args ---
     */
    public static void main(String[] args) {
        try {
            new AkanBot();
        } catch (LoginException e) {
            AkanBot.LOGGER.info(e.getMessage());
        }
    }

    /**
     * Checks if the bot should add a reaction after getting a command message.
     * This boolean is in the config.json and gets parsed by {@link Config#withSuccessReaction}
     * @return true/false
     */
    public boolean withSuccessReaction() {
        return conf.withSuccessReaction();
    }

    /**
     * Sets if the bot should add a reaction after getting a command message.
     * checkMark gets written in the config.json in {@link Config#setSuccessReaction(boolean)}
     * @param successReaction new boolean for successReactions
     */
    public void setSuccessReaction(boolean successReaction) {
        conf.setSuccessReaction(successReaction);
    }

    /**
     * Sets the prefix of the bot
     * @param prefix the new prefix
     */
    public void setPrefix(@Nonnull final String prefix) {
        conf.setPrefix(prefix);
        this.prefix = prefix;
    }

    public void setActivity(@Nonnull final String status, @Nonnull final String type) {
        conf.setActivity(status, type);
    }

    public String getPrefix() {
        return prefix;
    }

    public void printInfo(final String info) {
        LOGGER.info(info);
    }

    public static boolean userIsOwner(@Nonnull final User user) {
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
        String content = msg.getContentRaw();
        if (content.startsWith(this.prefix)
                && content.length() > this.prefix.length()) {
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
                    cmd.run(new CommandContext(event, Arrays.asList(split).subList(1, split.length), this));
                } catch (IllegalArgumentException e) {
                    event.getChannel().sendTyping().queue(rep ->
                            event.getChannel().sendMessage(e.getMessage()).queue());

                }
            }
        }
    }


    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.info("{} ready", event.getJDA().getSelfUser().getAsTag());
    }
}
