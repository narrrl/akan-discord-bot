package de.nirusu99.akan.commands;

import de.nirusu99.akan.AkanBot;
import me.duncte123.botcommons.commands.ICommandContext;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nonnull;
import java.util.List;

public class CommandContext implements ICommandContext {
    private final GuildMessageReceivedEvent event;
    private final List<String> args;
    private final AkanBot bot;

    public CommandContext(@Nonnull final GuildMessageReceivedEvent event,@Nonnull  final List<String> args,
                          @Nonnull  final AkanBot bot) {
        this.event = event;
        this.args = args;
        this.bot = bot;
    }

    public AkanBot getBot() {
        return bot;
    }

    @Override
    public Guild getGuild() {
        return this.getEvent().getGuild();
    }

    @Override
    public GuildMessageReceivedEvent getEvent() {
        return this.event;
    }

    public void printInfo(final String message) {
        bot.printInfo(message);
    }

    public List<String> getArgs() {
        return args;
    }

    public void setPrefix(@Nonnull final String newPrefix) {
        bot.setPrefix(newPrefix);
    }

    public void setSuccessReaction(final boolean parseBoolean) {
        bot.setSuccessReaction(parseBoolean);
    }
}