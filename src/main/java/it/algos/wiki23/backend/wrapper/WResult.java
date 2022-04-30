package it.algos.wiki23.backend.wrapper;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.wrapper.*;
import org.springframework.stereotype.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 10-ago-2021
 * Time: 09:05
 * Semplice wrapper per veicolare una risposta con diverse property <br>
 */
@Component
public class WResult extends AResult {

    private WrapBio wrap;

    private String wikiBio = VUOTA;

    private String summary = VUOTA;

    private String newtimestamp = VUOTA;

    private String newtext = VUOTA;

    private long newrevid = 0;

    private long inizio;

    private long fine;

    private boolean modificata = false;

    private WResult() {
        this(null);
    }

    private WResult(WrapBio wrap) {
        super();
        this.wrap = wrap;
        this.inizio = System.currentTimeMillis();
    }

    public static WResult crea(WrapBio wrap) {
        WResult wResult = new WResult(wrap);
        return wResult;
    }

    public static WResult errato() {
        WResult wResult = new WResult();
        wResult.setValido(false);
        return wResult;
    }
    public static WResult errato(final String errorMessage) {
        WResult wResult = new WResult( );
        wResult.setErrorCode(errorMessage);
        wResult.setValido(false);
        return wResult;
    }

    public WrapBio getWrap() {
        return wrap;
    }

    public void setWrap(WrapBio wrap) {
        this.wrap = wrap;
    }

    public static WResult valido() {
        return new WResult();
    }

    public String getWikiBio() {
        return wikiBio;
    }

    public void setWikiBio(String wikiBio) {
        this.wikiBio = wikiBio;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getNewtimestamp() {
        return newtimestamp;
    }

    public void setNewtimestamp(String newtimestamp) {
        this.newtimestamp = newtimestamp;
    }

    public long getNewrevid() {
        return newrevid;
    }

    public void setNewrevid(long newrevid) {
        this.newrevid = newrevid;
    }

    public boolean isModificata() {
        return modificata;
    }

    public void setModificata(boolean modificata) {
        this.modificata = modificata;
    }

    public String getNewtext() {
        return newtext;
    }

    public void setNewtext(String newtext) {
        this.newtext = newtext;
    }


    public void setFine() {
        this.fine = System.currentTimeMillis();
    }

    public long getDurata() {
        return fine - inizio;
    }

}
