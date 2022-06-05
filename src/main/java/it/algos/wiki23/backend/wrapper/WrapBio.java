package it.algos.wiki23.backend.wrapper;

import it.algos.wiki23.backend.enumeration.*;

import java.time.*;
import java.time.format.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 30-lug-2021
 * Time: 21:48
 * <p>
 * Semplice wrapper per i dati essenziali (tutti) di una pagina biografica recuperata da MediaWiki <br>
 */
public class WrapBio {

    private AETypePage type;

    private long pageid;

    private String title;

    private LocalDateTime time;

    private String templBio;

    private boolean valida;

    public WrapBio() {
    }


    public WrapBio type(final AETypePage type) {
        this.type = type;
        return this;
    }

    public WrapBio pageid(final long pageid) {
        this.pageid = pageid;
        return this;
    }

    public WrapBio title(final String title) {
        this.title = title;
        return this;
    }

    public WrapBio time(final LocalDateTime time) {
        this.time = time;
        return this;
    }

    public WrapBio templBio(final String templBio) {
        this.templBio = templBio;
        return this;
    }

    public WrapBio valida(final boolean valida) {
        this.valida = valida;
        return this;
    }

    public WrapBio time(final String stringTimestamp) {
        this.time = (stringTimestamp != null && stringTimestamp.length() > 0) ? LocalDateTime.parse(stringTimestamp, DateTimeFormatter.ISO_DATE_TIME) : null;
        return this;
    }

    //    public WrapBio(final String title, final AETypePage type) {
    //        this(0, title, VUOTA, VUOTA, type);
    //    }
    //
    //    public WrapBio(final long pageid, final String title, final AETypePage type) {
    //        this(pageid, title, VUOTA, VUOTA, type);
    //    }
    //
    //    public WrapBio(final long pageid, final String title, final String templBio, final String stringTimestamp, final AETypePage type) {
    //        this.pageid = pageid;
    //        this.title = title;
    //        this.templBio = templBio;
    //        this.time = (stringTimestamp != null && stringTimestamp.length() > 0) ? LocalDateTime.parse(stringTimestamp, DateTimeFormatter.ISO_DATE_TIME) : null;
    //        this.type = type;
    //        this.valido = type == AETypePage.testoConTmpl;
    //    }

    public AETypePage getType() {
        return type;
    }

    public long getPageid() {
        return pageid;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getTemplBio() {
        return templBio;
    }

    public boolean isValida() {
        return valida;
    }

}
