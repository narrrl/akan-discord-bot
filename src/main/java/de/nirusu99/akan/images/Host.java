package de.nirusu99.akan.images;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.jdom2.Content;
import org.jdom2.JDOMException;
import org.jdom2.Document;
import org.jdom2.Element;
import java.util.stream.Collectors;
import org.jdom2.input.SAXBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.annotation.Nonnull;

/**
 * Not perfect but works TODO: Needs some rework
 */
public enum Host {
    /**
     * Gelbooru Host
     */
    GELBOORU("https://gelbooru.com/", "index.php?page=dapi&s=post&q=index", "", "&pid=", "&tags=",
            "https://gelbooru.com/index.php?page=post&s=view&id=", "file_url", "peview_url", "source", "tags", "id",
            Host.ATTRIBUTE),
    /**
     * Safebooru Host
     */
    SAFEBOORU("https://safebooru.org/", "index.php?page=dapi&s=post&q=index", "", "&pid=", "&tags=",
            "https://safebooru.org/index.php?page=post&s=view&id=", "file_url", "peview_url", "source", "tags", "id",
            Host.ATTRIBUTE),
    /**
     * Danbooru Host
     */
    DANBOORU("https://danbooru.donmai.us/", "posts.xml?", "counts/", "page=", "&tags=",
            "https://danbooru.donmai.us/posts/", "large-file-url", "preview-file-url", "source", "tag-string", "id",
            Host.CHILD),
    NEKOAPI("https://nekos.life/api/v2/img/", null, null, null, null, null, null, null, null, null, null, null);

    private static final String POST = "[Element: <post/>]";
    private static final String CHILD = "child";
    private static final String ATTRIBUTE = "attribute";
    private final String home;
    private final String post;
    private final String countSubpage;
    private final String page;
    private final String tags;
    private final String postUrl;
    private final String sourceUrlQuery;
    private final String previewUrlQuery;
    private final String sourceQuery;
    private final String tagsQuery;
    private final String idQuery;
    private final String xmlNode;

    Host(String home, String post, String countSubpage, String page, String tags, String postUrl, String sourceUrlQuery,
            String previewUrlQuery, String sourceQuery, String tagsQuery, String idQuery, String xmlNode) {
        this.home = home;
        this.post = post;
        this.countSubpage = countSubpage;
        this.page = page;
        this.tags = tags;
        this.postUrl = postUrl;
        this.sourceUrlQuery = sourceUrlQuery;
        this.previewUrlQuery = previewUrlQuery;
        this.sourceQuery = sourceQuery;
        this.tagsQuery = tagsQuery;
        this.idQuery = idQuery;
        this.xmlNode = xmlNode;
    }

    public String searchForAmount(@Nonnull String tags) {
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

    public Collection<Image> searchForImages(@Nonnull final String tags, final int amount, final int page) {
        Document doc = this.getDocument(tags, page);
        Set<Image> images = new HashSet<>();
        Element rootElement = doc.getRootElement();
        List<Content> contents = rootElement.getContent().stream().filter(c -> c.toString().equals(POST))
                .collect(Collectors.toList());
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
                fileUrl = ((Element) c).getAttributeValue(sourceUrlQuery);
                previewUrl = ((Element) c).getAttributeValue(previewUrlQuery);
                source = ((Element) c).getAttributeValue(sourceQuery);
                tagsArray = ((Element) c).getAttributeValue(tagsQuery).split(" ");
                id = ((Element) c).getAttributeValue(idQuery);
            } else if (xmlNode.equals(CHILD)) {
                fileUrl = ((Element) c).getChild(sourceUrlQuery).getValue();
                previewUrl = ((Element) c).getChild(sourceUrlQuery).getValue();
                source = ((Element) c).getChild(sourceUrlQuery).getValue();
                tagsArray = ((Element) c).getChild(sourceUrlQuery).getValue().split(" ", -1);
                id = ((Element) c).getChild(sourceUrlQuery).getValue();
                images.add(new Image(fileUrl, previewUrl, tagsArray, source, id, this));
            } else {
                throw new IllegalArgumentException("Invalid xml node type");
            }
            images.add(new Image(fileUrl, previewUrl, tagsArray, source, id, this));
        }
        return images;
    }

    public Document getDocument(@Nonnull final String tags, final int page) {
        String url = this.home + this.post + this.page + (page - 1) + this.tags + tags;
        SAXBuilder builder = new SAXBuilder();
        Document document;
        try {
            document = builder.build(new URL(url));
        } catch (IOException | JDOMException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return document;
    }

    public Document getDocument(@Nonnull final String tags) {
        String url = this.home + this.countSubpage + this.post + this.tags + tags;
        SAXBuilder builder = new SAXBuilder();
        Document document;
        try {
            document = builder.build(new URL(url));
        } catch (IOException | JDOMException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return document;
    }

    public static Collection<Image> getNekos(final String tag, final int amount) throws IOException {
        Set<Image> images = new HashSet<>();
        for (int i = 1; i <= amount; i++) {
            URL url = new URL(String.format("https://nekos.life/api/v2/img/%s", tag));
            InputStream is = url.openStream();

            Scanner sc = new Scanner(is, "UTF-8").useDelimiter("\\A");
            String dataString = sc.next();
            sc.close();
            JSONObject json = null;
            try {
                json = (JSONObject) new JSONParser().parse(dataString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (json != null){
                String source = (String) json.get("url");
                images.add(new Image(source, source, new String[]{tag}, source, source, Host.NEKOAPI));
            }
        }
        return images;
    }
}
