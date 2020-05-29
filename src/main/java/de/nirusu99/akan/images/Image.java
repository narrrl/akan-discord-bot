package de.nirusu99.akan.images;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public final class Image implements Comparable<Image> {
    private final String url;
    private final String previewUrl;
    private final String[] tags;
    private final String source;
    private final String id;
    private final Host host;

    public Image(@Nonnull String url,@Nonnull  String previewUrl,@Nonnull  String[] tags,@Nonnull  String source,
                 @Nonnull  String id,@Nonnull  final Host host) {
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

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (o.getClass() == this.getClass()) {
            Image img = (Image) o;
            return img.id.equals(this.id);
        }
        return false;
    }

    @Override
    public int compareTo(@NotNull Image image) {
        return this.hashCode() - image.hashCode();
    }
}
