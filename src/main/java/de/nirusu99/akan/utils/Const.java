package de.nirusu99.akan.utils;

import de.nirusu99.akan.images.Host;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class Const {

    /**
     * Regex for a https link
     */
    public static final String LINK = "https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    /**
     * regex for a user
     */
    public static final String USER_REGEX = "<@![0-9]{18}>";

    /**
     * linebreak const
     */
    public static final String LINE_BREAK = "\n";

    /**
     * regex for activity types
     */
    public static final String ACTIVITY_TYPE_REGEX = Arrays.stream(ActivitySetter.values()).map(Enum::name)
            .collect(Collectors.joining("|")).toLowerCase();

    /**
     * regex for image search tags
     */
    public static final String TAGS_REGEX = "[\\p{L}\\d" + Const.SPECIAL_CHARS + "]+";

    /**
     * regex for bot status
     */
    public static final String STATUS_REGEX = "[\\p{L}_$&+,:;=?@#'<>.^*()%!\\- 0-9\\/]+";

    /**
     * regex for rep messages
     */
    public static final String REP_REGEX = "[A-Za-z0-9äÄöÖüÜß?! ]+";

    /**
     * regex for special characters
     */
    public static final String SPECIAL_CHARS = "_$&+,:;=?@#'<>.^*()%!-";

    /**
     * regex for int
     */
    public static final String INT_REGEX = "\\d+";

    /**
     * regex for names
     */
    public static final String NAMING_REGEX = "[\\p{L}_$&+,:;=?@#'<>.^*()%!-]+";

    /**
     * Regex for bot prefix
     */
    public static final String PREFIX_REGEX = "[\\p{L}_$&+,:;=?@#'<>.^*()%!-]+";

    /**
     * Regex for boolean
     */
    public static final String BOOLEAN_REGEX = "true|false";

    /**
     * Regex for image hosts
     */
    public static final String HOSTS_REGEX = Arrays.stream(Host.values()).map(Enum::toString)
            .collect(Collectors.joining("|")).toLowerCase();

    /**
     * Don't instantiate
     */
    private Const() {
        throw new IllegalAccessError();
    }
}
