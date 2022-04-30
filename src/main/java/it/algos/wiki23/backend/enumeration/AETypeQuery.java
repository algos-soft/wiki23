package it.algos.wiki23.backend.enumeration;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 27-lug-2021
 * Time: 09:25
 */
public enum AETypeQuery {
    get("GET senza loginCookies"),
    getCookies("GET con loginCookies"),
    post("POST"),
    postCookies("POST con loginCookies"),
    login("preliminary GET + POST"),
    ;

    private String tag;


    AETypeQuery(String tag) {
        this.tag = tag;
    }

    public String get() {
        return tag;
    }
}
