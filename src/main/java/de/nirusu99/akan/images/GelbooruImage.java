package de.nirusu99.akan.images;

public class GelbooruImage {
    private static final String INVALID = "invalid";
    private String url;
    private String previewUrl;
    private String[] tags;
    private String source;
    private String id;

    public GelbooruImage() {
        url = INVALID;
        previewUrl = INVALID;
        tags = null;
        source = INVALID;
        id = INVALID;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public void setPreviewUrl(final String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public void setTags(final String[] tags) {
        this.tags = tags;
    }

    public void setId(final String id) {
        this.id = id;
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
        return "https://gelbooru.com/index.php?page=post&s=view&id=" + id;
    }
}
