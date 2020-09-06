package de.nirusu99.akan;

import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.commands.CommandBuilder;
import de.nirusu99.akan.core.Config;
import de.nirusu99.akan.core.GuildManager;
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
	public static final String PREFIX = "a!";
    private final Config conf;
    private final Logger log;

    AkanBot() throws LoginException {
        conf = new Config(this);
        log = new Logger(this);
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

    public void setActivity(@Nonnull final String status, @Nonnull final String type) {
        conf.setActivity(status, type);
    }

    public Logger getLogger() {
        return this.log;
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
        GuildManager gm = GuildManager.getManager(event.getGuild().getIdLong(), this);
        Message msg = event.getMessage();
        String content = msg.getContentRaw();
        final String pf = gm.getPrefix();
        if (content.startsWith(pf)
                && content.length() > pf.length()) {
            String contentRaw = msg.getContentRaw().substring(pf.length());
            ICommand cmd = CommandBuilder.createCommand(contentRaw);
            if (cmd == null) {
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("unknown command").queue(response -> response.
                        editMessage("unknown command <:KEKW:715043065190154282>").queue());
            } else {
                try {
                    String[] split = contentRaw.split("\\s+");
                    log.addLog(event, cmd);
                    if (gm.withSuccessReaction()) {
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
