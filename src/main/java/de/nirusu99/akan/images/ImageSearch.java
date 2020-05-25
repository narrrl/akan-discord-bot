package de.nirusu99.akan.images;

import de.nirusu99.akan.utils.Host;
import de.nirusu99.akan.utils.Requests;
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
        return Requests.request(url, amount, host);
    }
}
