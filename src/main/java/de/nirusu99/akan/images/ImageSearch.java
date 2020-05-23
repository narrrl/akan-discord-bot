package de.nirusu99.akan.images;

import de.nirusu99.akan.utils.Requests;
import java.util.List;

public final class ImageSearch {

    /**
     * Don't instantiate
     */
    private ImageSearch() { };

    public static GelbooruImage[] getImagesFor(final List<String> tags, final int amount, final int page) {
        StringBuilder tag = new StringBuilder();
        tags.forEach(str -> {
            tag.append(str.toLowerCase().trim());
            if (!str.equals(tags.get(tags.size() - 1))) {
                tag.append("+");
            }
        });
        String url = "https://gelbooru.com/index.php?page=dapi&s=post&q=index"
                + "&pid=" + (page - 1)
                + "&tags=" + tag.toString();
        return Requests.GelbooruRequest(url, amount);
    }
}
