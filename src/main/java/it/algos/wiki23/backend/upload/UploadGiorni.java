package it.algos.wiki23.backend.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.packages.crono.giorno.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.liste.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Tue, 26-Jul-2022
 * Time: 08:47
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UploadGiorni extends Upload {

    private static final boolean SENZA_PARAGRAFI = false;

    protected AETypeCrono typeCrono;

    protected AETypeDidascalia typeDidascalia;

    private String nomeGiorno;

    private int ordineGiorno;

    private boolean senzaParagrafi;

    private String wikiTitle;


    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(UploadAttivita.class, attivita) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     */
    public UploadGiorni() {
    }// end of constructor


    protected WResult esegue(String wikiTitle, String testoBody, int numVoci) {
        StringBuffer buffer = new StringBuffer();

        buffer.append(avviso());
        buffer.append(CAPO);
        buffer.append(includeIni());
        buffer.append(fixToc());
        buffer.append(torna(VUOTA));
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
        String title = wikiUtility.natiMortiGiorno(tag, nomeGiorno);

        buffer.append(CAPO);
        buffer.append(String.format("*[[Categoria:Liste di %s per giorno| %s]]", typeCrono.getTagLower(), ordineGiorno));
        buffer.append(CAPO);
        buffer.append(String.format("*[[Categoria:%s| ]]", title));
        buffer.append(CAPO);

        return buffer.toString();
    }

    /**
     * Esegue la scrittura della pagina <br>
     */
    public void uploadTestNascita(String nomeGiorno) {
        Giorno giorno = giornoBackend.findByNome(nomeGiorno);
        this.uploadTest = true;
        this.nomeGiorno = nomeGiorno;
        this.wikiTitle = UPLOAD_TITLE_DEBUG + wikiUtility.wikiTitleNatiGiorno(nomeGiorno);
        this.ordineGiorno = giorno != null ? giorno.getOrdine() : 0;
        typeCrono = AETypeCrono.nascita;
        typeDidascalia = AETypeDidascalia.giornoNascita;
        WResult result = appContext.getBean(ListaGiorni.class).nascita(nomeGiorno).testoBody();
        this.esegue(wikiTitle, result.getContent(), result.getIntValue());
    }

    /**
     * Esegue la scrittura della pagina <br>
     */
    public void uploadTestMorte(String nomeGiorno) {
        Giorno giorno = giornoBackend.findByNome(nomeGiorno);
        this.uploadTest = true;
        this.nomeGiorno = nomeGiorno;
        this.wikiTitle = UPLOAD_TITLE_DEBUG + wikiUtility.wikiTitleMortiGiorno(nomeGiorno);
        this.ordineGiorno = giorno != null ? giorno.getOrdine() : 0;
        typeCrono = AETypeCrono.morte;
        typeDidascalia = AETypeDidascalia.giornoMorte;
        WResult result = appContext.getBean(ListaGiorni.class).morte(nomeGiorno).testoBody();
        this.esegue(wikiTitle, result.getContent(), result.getIntValue());
    }

}
