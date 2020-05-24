package de.nirusu99.akan.utils;

import de.nirusu99.akan.images.Image;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public final class Requests {
    /**
     * Don't instantiate
     */
    private Requests() { }
    public static Image[] request(final String url, final int amount, final Host host) {
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
        Image[] images = new Image[amount];
        int max = nodeList.getLength();
        Random rand = new Random();
        for (int x = 0; x < amount; x++) {
            int i = rand.nextInt(max);
            String fileUrl = nodeList.item(i).getAttributes().getNamedItem("file_url").getNodeValue();
            String previewUrl = nodeList.item(i).getAttributes().getNamedItem("preview_url").getNodeValue();
            String source = nodeList.item(i).getAttributes().getNamedItem("source").getNodeValue();
            String[] tags = nodeList.item(i).getAttributes().getNamedItem("tags").getNodeValue()
                    .split(" ", -1);
            String id = nodeList.item(i).getAttributes().getNamedItem("id").getNodeValue();
            images[x] = new Image(fileUrl, previewUrl, tags, source, id, host);
        }
        return images;
    }
}
