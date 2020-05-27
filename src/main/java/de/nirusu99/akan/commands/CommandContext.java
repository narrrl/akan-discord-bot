package de.nirusu99.akan.commands;

import de.nirusu99.akan.AkanBot;
import me.duncte123.botcommons.commands.ICommandContext;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class CommandContext implements ICommandContext {
    private final GuildMessageReceivedEvent event;
    private final List<String> args;
    private final AkanBot bot;

    public CommandContext(final GuildMessageReceivedEvent event, final List<String> args, final AkanBot bot) {
        this.event = event;
        this.args = args;
        this.bot = bot;
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

    public void setPrefix(final String newPrefix) {
        bot.setPrefix(newPrefix);
    }

    public void setSuccessReaction(final boolean parseBoolean) {
        bot.setSuccessReaction(parseBoolean);
    }
}
