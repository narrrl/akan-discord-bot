package de.nirusu99.akan.commands;

import java.util.ServiceLoader;
import java.util.regex.Matcher;

public class CommandBuilder {
    private CommandBuilder() {throw new IllegalAccessError();}

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
