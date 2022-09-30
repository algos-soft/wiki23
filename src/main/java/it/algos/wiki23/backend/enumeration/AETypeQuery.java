package it.algos.wiki23.backend.enumeration;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 27-lug-2021
 * Time: 09:25
 * GET più semplice
 * POST più elaborata
 */
public enum AETypeQuery {
    getSenzaLoginSenzaCookies("GET senza login e senza cookies"),
    getLoggatoConCookies("GET loggato con cookies di botLogin"),
    post("POST senza loginCookies"),
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
