package de.nirusu99.akan.commands;

import de.nirusu99.akan.AkanBot;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.regex.Matcher;

public interface ICommand {
    void run(CommandContext ctx);

    String getName();

    String syntax();

    String gifHelpUrl();

    Matcher matches(final String input);
}
