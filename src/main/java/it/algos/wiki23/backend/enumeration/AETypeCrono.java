package it.algos.wiki23.backend.enumeration;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Sun, 17-Jul-2022
 * Time: 13:54
 */
public enum AETypeCrono {
    giornoNascita("nati", "giorno"),
    giornoMorte("morti", "giorno"),
    annoNascita("nati", "anno"),
    annoMorte("morti", "anno"),
    listaBreve(VUOTA, VUOTA),
    listaEstesa(VUOTA, VUOTA);

    private String tagLower;

    private String tagUpper;

    private String giornoAnno;


    AETypeCrono(String tag, String giornoAnno) {
        this.tagLower = tag;
        this.giornoAnno = giornoAnno;
        this.tagUpper = tag != null && tag.length() > 0 ? tag.substring(0, 1).toUpperCase() + tag.substring(1) : VUOTA;
    }

    public String getTagLower() {
        return tagLower;
    }

    public String getTagUpper() {
        return tagUpper;
    }

    public String getGiornoAnno() {
        return giornoAnno;
    }
}
