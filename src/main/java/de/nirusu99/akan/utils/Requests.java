package de.nirusu99.akan.utils;

import de.nirusu99.akan.images.GelbooruImage;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class Requests {
    public static GelbooruImage[] GelbooruRequest(final String url, final int amount) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder xmlBuilder;
        try {
            xmlBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        Document doc;
        try {
            doc = xmlBuilder.parse(new URL(url).openStream());
        } catch (IOException | SAXException e) {
            System.err.println(e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
        NodeList nodeList = doc.getElementsByTagName("post");
        GelbooruImage[] images = new GelbooruImage[amount];
        int max = nodeList.getLength();
        Random rand = new Random();
        for (int x = 0; x < amount; x++) {
            int i = rand.nextInt(max);
            images[x] = new GelbooruImage();
            images[x].setUrl(nodeList.item(i).getAttributes().getNamedItem("file_url").getNodeValue());
            images[x].setPreviewUrl(nodeList.item(i).getAttributes().getNamedItem("preview_url").getNodeValue());
            images[x].setSource(nodeList.item(i).getAttributes().getNamedItem("source").getNodeValue());
            images[x].setTags(nodeList.item(i).getAttributes().getNamedItem("tags").getNodeValue()
                    .split(" ",-1));
            images[x].setId(nodeList.item(i).getAttributes().getNamedItem("id").getNodeValue());
        }
        return images;
    }
}
