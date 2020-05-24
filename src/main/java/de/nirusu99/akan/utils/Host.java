package de.nirusu99.akan.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Host {
    GELBOORU("https://gelbooru.com/index.php?page=dapi&s=post&q=index","&pid=","&tags=",
            "https://gelbooru.com/index.php?page=post&s=view&id="),
    SAFEBOORU("https://safebooru.org/index.php?page=dapi&s=post&q=index","&pid=","&tags=",
            "https://safebooru.org/index.php?page=post&s=view&id=");

    public final static String HOSTS_REGEX = Arrays.stream(Host.values()).map(Enum::toString)
            .collect(Collectors.joining("|")).toLowerCase();
    final String home;
    final String page;
    final String tags;
    final String postUrl;

    Host(final String home, final String page, final String tags, final String postUrl) {
        this.home = home;
        this.page = page;
        this.tags = tags;
        this.postUrl = postUrl;
    }

    public String postUrl() {
        return this.postUrl;
    }

    public String home() {
        return this.home;
    }

    public String page() {
        return this.page;
    }

    public String tags() {
        return this.tags;
    }

    public static Host getHost(final String value) {
        for (Host h : values()) {
            if (h.toString().toLowerCase().equals(value)) {
                return h;
            }
        }
        throw new IllegalArgumentException("host " + value + " not found");
    }
}
