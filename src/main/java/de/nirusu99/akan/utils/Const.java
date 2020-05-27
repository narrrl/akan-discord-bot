package de.nirusu99.akan.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class Const {
    private Const() {throw new IllegalAccessError();}
    public static final String USER_REGEX = "<@![0-9]{18}>";
    public static final String LINE_BREAK = "\n";
    public static final String ACTIVITY_TYPE_REGEX = Arrays.stream(ActivitySetter.values()).map(Enum::name)
            .collect(Collectors.joining("|")).toLowerCase();
    public static final String TAGS_REGEX = "[\\p{L}\\d" + Const.SPECIAL_CHARS + "]+";
    public static final String STATUS_REGEX = "[\\p{L}_$&+,:;=?@#'<>.^*()%!\\- 0-9\\/]+";
    public static final String REP_REGEX = "[A-Za-z0-9äÄöÖüÜß?! ]+";
    public static final String SPECIAL_CHARS = "_$&+,:;=?@#'<>.^*()%!-";
    public static final String INT_REGEX = "\\d+";
    public static final String NAMING_REGEX = "[\\p{L}_$&+,:;=?@#'<>.^*()%!-]+";
    public static final String PREFIX_REGEX = "[\\p{L}_$&+,:;=?@#'<>.^*()%!-]+";
    public static final String BOOLEAN_REGEX = "true|false";
}
