package de.nirusu99.akan.commands;

import java.util.regex.Matcher;

public interface ICommand {
    void run(CommandContext ctx);

    String getName();

    String syntax();

    String gifHelpUrl();

    Matcher matches(final String input);
}
