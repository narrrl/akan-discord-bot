package de.nirusu99.akan.images;

import okhttp3.HttpUrl;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImageSearch {
    public static ArrayList<String> getImagesFor(final List<String> tags, final int amount, final int page) {
        if (amount > 5) throw new IllegalArgumentException("You can't search for more then 5 pics");
        if (amount < 1) throw new IllegalArgumentException("You can't search for less then 1 pic");
        if (page < 1) throw new IllegalArgumentException("You can't search for pages less then 1");
        ArrayList<String> out = new ArrayList<>();
        HttpUrl.Builder builder = new HttpUrl.Builder();
        StringBuilder tag = new StringBuilder();
        tags.forEach(str -> {
            tag.append(str.toLowerCase().trim());
            if (!str.equals(tags.get(tags.size() - 1))) {
                tag.append("+");
            }
        });
        StringBuilder url = new StringBuilder();
        url.append("https://gelbooru.com/index.php?page=dapi&s=post&q=index")
                .append("&limit=").append(amount)
                .append("&pid=").append(page - 1)
                .append("&tags=").append(tag.toString());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder xmlBuilder;
        try {
            xmlBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.err.println(e.getMessage());
            return new ArrayList<>();
        }
        Document doc;
        try {
            doc = xmlBuilder.parse(new URL(url.toString()).openStream());
        } catch (IOException | SAXException e) {
            System.err.println(e.getMessage());
            return new ArrayList<>();
        }
        NodeList nodeList = doc.getElementsByTagName("post");
        for(int x = 0; x < nodeList.getLength(); x++) {
            out.add(nodeList.item(x).getAttributes().getNamedItem("file_url").getNodeValue());
        }
        return out;
    }
}
