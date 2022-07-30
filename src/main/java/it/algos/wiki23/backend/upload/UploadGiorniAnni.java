package it.algos.wiki23.backend.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Sat, 30-Jul-2022
 * Time: 07:39
 * <p>
 * Superclasse astratta per le classi UploadGiorni e UploadAnni <br>
 */
public abstract class UploadGiorniAnni extends Upload {

    protected AETypeCrono typeCrono;

    protected String nomeGiornoAnno;

    protected boolean senzaParagrafi;


    protected int ordineGiornoAnno;

    protected String wikiTitle;


    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(UploadAttivita.class, attivita) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     */
    public UploadGiorniAnni() {
    }// end of constructor


    protected WResult esegue(String wikiTitle, String testoBody, int numVoci) {
        StringBuffer buffer = new StringBuffer();

        buffer.append(avviso());
        buffer.append(CAPO);
        buffer.append(includeIni());
        buffer.append(fixToc());
        buffer.append(torna());
        buffer.append(tmpListaBio(numVoci));
        buffer.append(includeEnd());
        buffer.append(CAPO);
        buffer.append(tmpListaPersoneIni(numVoci));
        buffer.append(testoBody);
        buffer.append(uploadTest ? VUOTA : DOPPIE_GRAFFE_END);
        buffer.append(includeIni());
        buffer.append(portale());
        buffer.append(categorie());
        buffer.append(includeEnd());

        return registra(wikiTitle, buffer.toString().trim());
    }


    protected String torna() {
        return textService.isValid(nomeGiornoAnno) ? String.format("{{Torna a|%s}}", nomeGiornoAnno) : VUOTA;
    }

    protected String tmpListaPersoneIni(int numVoci) {
        StringBuffer buffer = new StringBuffer();

        buffer.append("{{Lista persone per giorno");
        buffer.append(CAPO);
        buffer.append("|titolo=");
        buffer.append(wikiTitle);
        buffer.append(CAPO);
        buffer.append("|voci=");
        buffer.append(numVoci);
        buffer.append(CAPO);
        buffer.append("|testo=");
        buffer.append(senzaParagrafi ? VUOTA : "<nowiki></nowiki>");
        buffer.append(CAPO);

        return uploadTest ? VUOTA : buffer.toString();
    }


    protected String categorie() {
        StringBuffer buffer = new StringBuffer();
        String tag = typeCrono.getTagUpper();
        String title = wikiUtility.natiMortiGiorno(tag, nomeGiornoAnno);

        buffer.append(CAPO);
        buffer.append(String.format("*[[Categoria:Liste di %s per giorno| %s]]", typeCrono.getTagLower(), ordineGiornoAnno));
        buffer.append(CAPO);
        buffer.append(String.format("*[[Categoria:%s| ]]", title));
        buffer.append(CAPO);

        return buffer.toString();
    }

}
