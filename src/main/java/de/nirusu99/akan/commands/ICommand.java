package de.nirusu99.akan.commands;

import java.util.regex.Matcher;

/**
 * Basic implementation for a bot command
 *
 * @author Nils Pukropp
 * @since 1.0
 */
public interface ICommand {
    void run(CommandContext ctx);

    String getName();

    String syntax();

    String gifHelpUrl();

    Matcher matches(final String input);
}
