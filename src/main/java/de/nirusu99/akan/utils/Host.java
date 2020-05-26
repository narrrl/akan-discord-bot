package de.nirusu99.akan.utils;

import de.nirusu99.akan.images.Image;
import java.util.*;
import java.util.stream.Collectors;
import org.jdom2.*;

public enum Host {
    GELBOORU("https://gelbooru.com/index.php?page=dapi&s=post&q=index","&pid=","&tags=",
            "https://gelbooru.com/index.php?page=post&s=view&id="),
    SAFEBOORU("https://safebooru.org/index.php?page=dapi&s=post&q=index","&pid=","&tags=",
            "https://safebooru.org/index.php?page=post&s=view&id="),
    DANBOORU("https://danbooru.donmai.us/posts.xml","?page=","&tags=",
            "https://danbooru.donmai.us/posts/") {
        @Override
        public List<Image> getImages(Document doc, final int amount) {
            List<Image> images = new ArrayList<>();
            Element rootElement = doc.getRootElement();
            List<Content> contents = rootElement.getContent();
            Random rand = new Random();
            contents = contents.stream().filter(c ->
                    c.toString().equals(POST)).collect(Collectors.toList());
            int max = contents.size();
            for (int x = 1; x <= amount && x <= max; x++) {
                int i = rand.nextInt(max);
                Content c = contents.get(i);
                String fileUrl = ((Element) c).getChild("large-file-url").getValue();
                String previewUrl = ((Element) c).getChild("preview-file-url").getValue();
                String source = ((Element) c).getChild("source").getValue();
                String[] tags = ((Element) c).getChild("tag-string").getValue().split(" ",-1);
                String id = ((Element) c).getChild("id").getValue();
                images.add(new Image(fileUrl, previewUrl, tags, source, id, this));
            }
            return images;
        }

    };

    public final static String HOSTS_REGEX = Arrays.stream(Host.values()).map(Enum::toString)
            .collect(Collectors.joining("|")).toLowerCase();
    private final static String POST = "[Element: <post/>]";
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

    public Collection<Image> getImages(Document doc, final int amount) {
        Set<Image> images = new HashSet<>();
        Element rootElement = doc.getRootElement();
        List<Content> contents = rootElement.getContent().stream().filter(c
                -> c.toString().equals(POST)).collect(Collectors.toList());
        int max = contents.size();
        Random rand = new Random();
        for (int x = 1; x <= amount && x <= max; x++) {
            int i = rand.nextInt(max);
            Content c = contents.get(i);
            String fileUrl = ((Element) c).getAttributeValue("file_url");
            String previewUrl = ((Element) c).getAttributeValue("preview_url");
            String source = ((Element) c).getAttributeValue("source");
            String[] tags = ((Element) c).getAttributeValue("tags").split(" ");
            String id = ((Element) c).getAttributeValue("id");
            images.add(new Image(fileUrl, previewUrl, tags, source, id, this));
        }
        return images;
    }
}
