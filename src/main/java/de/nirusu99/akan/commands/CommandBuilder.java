package de.nirusu99.akan.commands;

import java.util.ServiceLoader;
import java.util.regex.Matcher;

public class CommandBuilder {
    private CommandBuilder() {throw new IllegalAccessError();}

    /**
     * Finds the {@link ICommand} that matches the input and returns its.
     * The commands get loaded from {@link ServiceLoader}.
     * @param input the user input
     * @return the matching {@link ICommand} or null
     */
    public static ICommand createCommand(final String input) {
        for (ICommand cmd : ServiceLoader.load(ICommand.class)) {
            Matcher matcher = cmd.matches(input);
            if (matcher.matches()) {
                return cmd;
            }
        }
        return null;
    }

    public static Iterable<ICommand> getCommands() {
        return ServiceLoader.load(ICommand.class);
    }
}
