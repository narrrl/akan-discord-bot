package de.nirusu99.akan.images;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

public enum Host {
    GELBOORU("https://gelbooru.com/","index.php?page=dapi&s=post&q=index", "", "&pid=","&tags=",
            "https://gelbooru.com/index.php?page=post&s=view&id=","file_url",
            "peview_url","source","tags","id", Host.ATTRIBUTE),
    SAFEBOORU("https://safebooru.org/", "index.php?page=dapi&s=post&q=index", "", "&pid=","&tags=",
            "https://safebooru.org/index.php?page=post&s=view&id=","file_url",
            "peview_url","source","tags","id", Host.ATTRIBUTE),
    DANBOORU("https://danbooru.donmai.us/", "posts.xml?", "counts/", "page=","&tags=",
            "https://danbooru.donmai.us/posts/","large-file-url",
            "preview-file-url","source","tag-string","id", Host.CHILD) {
    };

    public final static String HOSTS_REGEX = Arrays.stream(Host.values()).map(Enum::toString)
            .collect(Collectors.joining("|")).toLowerCase();
    private final static String POST = "[Element: <post/>]";
    private final static String CHILD = "child";
    private final static String ATTRIBUTE = "attribute";
    private final String home;
    private final String post;
    private final String countSubpage;
    private final String page;
    private final String tags;
    private final String postUrl;
    private final String source_urlQuery;
    private final String preview_urlQuery;
    private final String sourceQuery;
    private final String tagsQuery;
    private final String idQuery;
    private final String xmlNode;

    Host(String home, String post, String countSubpage, String page, String tags, String postUrl, String source_urlQuery,
         String preview_urlQuery, String sourceQuery, String tagsQuery, String idQuery, String xmlNode) {
        this.home = home;
        this.post = post;
        this.countSubpage = countSubpage;
        this.page = page;
        this.tags = tags;
        this.postUrl = postUrl;
        this.source_urlQuery = source_urlQuery;
        this.preview_urlQuery = preview_urlQuery;
        this.sourceQuery = sourceQuery;
        this.tagsQuery = tagsQuery;
        this.idQuery = idQuery;
        this.xmlNode = xmlNode;
    }

    public String searchForAmount(String tags) {
        Document doc = this.getDocument(tags);
        Element root = doc.getRootElement();
        if (Host.DANBOORU.equals(this)) {
            return root.getChild("posts").getValue();
        }
        return root.getAttributeValue("count");
    }

    String postUrl() {
        return this.postUrl;
    }

    public static Host getHost(final String value) {
        for (Host h : values()) {
            if (h.toString().toLowerCase().equals(value)) {
                return h;
            }
        }
        throw new IllegalArgumentException("host " + value + " not found");
    }

    public Collection<Image> searchForImages(final String tags, final int amount, final int page) {
        Document doc = this.getDocument(tags, page);
        Set<Image> images = new HashSet<>();
        Element rootElement = doc.getRootElement();
        List<Content> contents = rootElement.getContent().stream().filter(c
                -> c.toString().equals(POST)).collect(Collectors.toList());
        int max = contents.size();
        Random rand = new Random();
            for (int x = 1; x <= amount && x <= max; x++) {
                int i = rand.nextInt(max);
                Content c = contents.get(i);
                String fileUrl;
                String previewUrl;
                String source;
                String[] tagsArray;
                String id;
                if (xmlNode.equals(ATTRIBUTE)) {
                    fileUrl = ((Element) c).getAttributeValue(source_urlQuery);
                    previewUrl = ((Element) c).getAttributeValue(preview_urlQuery);
                    source = ((Element) c).getAttributeValue(sourceQuery);
                    tagsArray = ((Element) c).getAttributeValue(tagsQuery).split(" ");
                    id = ((Element) c).getAttributeValue(idQuery);
                } else if (xmlNode.equals(CHILD)) {
                    fileUrl = ((Element) c).getChild(source_urlQuery).getValue();
                    previewUrl = ((Element) c).getChild(source_urlQuery).getValue();
                    source = ((Element) c).getChild(source_urlQuery).getValue();
                    tagsArray = ((Element) c).getChild(source_urlQuery).getValue().split(" ",-1);
                    id = ((Element) c).getChild(source_urlQuery).getValue();
                    images.add(new Image(fileUrl, previewUrl, tagsArray, source, id, this));
                } else {
                    throw new IllegalArgumentException("Invalid xml node type");
                }
                images.add(new Image(fileUrl, previewUrl, tagsArray, source, id, this));
        }
        return images;
    }

    public Document getDocument(final String tags, final int page) {
        String url = this.home + this.post
                + this.page + (page - 1)
                + this.tags + tags;
        SAXBuilder builder = new SAXBuilder();
        Document document;
        try {
            document = builder.build(new URL(url));
        } catch (IOException | JDOMException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return document;
    }

    public Document getDocument(final String tags) {
        String url = this.home + this.countSubpage + this.post
                + this.tags + tags;
        SAXBuilder builder = new SAXBuilder();
        Document document;
        try {
            document = builder.build(new URL(url));
        } catch (IOException | JDOMException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return document;
    }
}
