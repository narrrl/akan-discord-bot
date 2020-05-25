package de.nirusu99.akan.images;

import de.nirusu99.akan.utils.Host;

public final class Image{
    private final String url;
    private final String previewUrl;
    private final String[] tags;
    private final String source;
    private final String id;
    private final Host host;

    public Image(String url, String previewUrl, String[] tags, String source, String id, final Host host) {
        this.url = url;
        this.previewUrl = previewUrl;
        this.tags = tags;
        this.source = source;
        this.id = id;
        this.host = host;
    }

    public boolean isVideo() {
        return this.url.endsWith(".webm") || this.url.endsWith(".mp4");
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public String getSource() {
        return source;
    }

    public String getUrl() {
        return url;
    }

    public String[] getTags() {
        return tags;
    }

    public String getId() {
        return id;
    }

    public String getPostUrl() {
        return host.postUrl() + id;
    }
}
