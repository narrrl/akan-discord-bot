package de.nirusu99.akan.images;

import de.nirusu99.akan.utils.Host;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public final class ImageSearch {

    /**
     * Don't instantiate
     */
    private ImageSearch() { };

    public static List<Image> searchFor(final String tags, final int amount, final int page, final Host host) {
        String url = host.home()
                + host.page() + (page - 1)
                + host.tags() + tags;
        SAXBuilder builder = new SAXBuilder();
        Document document;
        try {
            document = builder.build(new URL(url));
        } catch (IOException | JDOMException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return host.getImages(document, amount);
    }
}
