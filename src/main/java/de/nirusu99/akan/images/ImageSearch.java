package de.nirusu99.akan.images;

import de.nirusu99.akan.utils.Host;
import de.nirusu99.akan.utils.Requests;
import java.util.List;

public final class ImageSearch {

    /**
     * Don't instantiate
     */
    private ImageSearch() { };

    public static List<Image> searchFor(final List<String> tags, final int amount, final int page, final Host host) {
        StringBuilder tag = new StringBuilder();
        tags.forEach(str -> {
            tag.append(str.toLowerCase().trim());
            if (!str.equals(tags.get(tags.size() - 1))) {
                tag.append("+");
            }
        });
        String url = host.home()
                + host.page() + (page - 1)
                + host.tags() + tag.toString();
        return Requests.request(url, amount, host);
    }
}
