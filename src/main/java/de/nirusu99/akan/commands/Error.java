package de.nirusu99.akan.commands;

/**
 * Error messages for {@link ICommand}.
 *
 * @author Nils Pukropp
 * @since 1.0
 */
public enum Error {
    NOT_OWNER {
        @Override
        public String toString() {
            return "Only bot owner can do that!";
        }
    },
    NO_PERMISSION {
        @Override
        public String toString() {
            return "You don't have the required permissions to do that";
        }
    },
    NOT_NSFW {
        @Override
        public String toString() {
            return "This command is limited to nsfw channel only";
        }
    },
    NOT_GUILD_CHANNEL {
        @Override
        public String toString() {
            return "You can't do that in private chat";
        }
    },
    INVALID_ARGUMENTS {
        @Override
        public String toString() {
            return "invalid arguments";
        }
    }
}
