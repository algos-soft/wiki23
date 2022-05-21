package it.algos.wiki23.backend.enumeration;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 27-lug-2021
 * Time: 09:25
 */
public enum AETypeQuery {
    getSenzaLoginSenzaCookies("GET senza login e senza cookies"),
    getLoggatoConCookies("GET loggato con cookies di botLogin"),
    post("POST"),
    postPiuCookies("POST con loginCookies"),
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
