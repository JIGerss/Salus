package team.glhf.salus.enumeration;

/**
 * @author Steveny
 * @since 2023/11/30
 */
public enum FilePathEnum {
    ARTICLE_IMAGE("article/"),
    PLACE_IMAGE("place/"),
    USER_AVATAR("avatar/");

    FilePathEnum(String path) {
        this.path = path;
    }

    private final String path;

    public String getPath() {
        return path;
    }
}
