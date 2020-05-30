package de.nirusu99.akan.commands;

/**
 * Error messages for {@link ICommand}.
 *
 * @author Nils Pukropp
 * @since 1.0
 */
public enum Error {
    /**
     * Error message when the user is not a owner
     */
    NOT_OWNER {
        @Override
        public String toString() {
            return "Only bot owner can do that!";
        }
    },
    /**
     * Error message when the user doesn't have the permissions
     */
    NO_PERMISSION {
        @Override
        public String toString() {
            return "You don't have the required permissions to do that";
        }
    },
    /**
     * Error message when the channel is not NSFW
     */
    NOT_NSFW {
        @Override
        public String toString() {
            return "This command is limited to nsfw channel only";
        }
    },
    /**
     * Error message when the user tries to message in private
     */
    NOT_GUILD_CHANNEL {
        @Override
        public String toString() {
            return "You can't do that in private chat";
        }
    },
    /**
     * Error message when the arguments are invalid
     */
    INVALID_ARGUMENTS {
        @Override
        public String toString() {
            return "invalid arguments";
        }
    }
}
