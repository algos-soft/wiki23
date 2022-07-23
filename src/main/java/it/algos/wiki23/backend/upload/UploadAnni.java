package it.algos.wiki23.backend.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.liste.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.time.*;
import java.time.format.*;
import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Fri, 22-Jul-2022
 * Time: 10:20
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UploadAnni extends Upload {

    private static final int MAX_LINK = 200;

    protected AETypeCrono typeCrono;

    protected AETypeDidascalia typeDidascalia;

    private String nomeAnno;

    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(UploadAttivita.class, attivita) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     */
    public UploadAnni() {
    }// end of constructor


    protected WResult esegue(String wikiTitle, LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaDidascalie) {
        StringBuffer buffer = new StringBuffer();
        int numVoci = wikiUtility.getSizeAll(mappaDidascalie);

        buffer.append(avviso());
        buffer.append(CAPO);
        buffer.append(includeIni());
        buffer.append(forceToc());
        buffer.append(torna(VUOTA));
        buffer.append(tmpListaBio(numVoci));
        buffer.append(includeEnd());
        buffer.append(CAPO);
        buffer.append(tmpListaPersoneIni(numVoci));
        buffer.append(numVoci <= MAX_LINK ? "{{Div col}}" + CAPO : VUOTA);
        buffer.append(body(wikiTitle, mappaDidascalie));
        buffer.append(numVoci <= MAX_LINK ? "{{Div col end}}" + CAPO : VUOTA);
        buffer.append("}}");
        buffer.append(includeIni());
        buffer.append(portale());
        buffer.append(categorie());
        buffer.append(includeEnd());

        return registra(wikiTitle, buffer.toString());
    }

    @Override
    protected String torna(String wikiTitle) {
        return textService.isValid(nomeAnno) ? String.format("{{Torna a|%s}}", nomeAnno) : VUOTA;
    }

    @Override
    protected String incipit() {
        return VUOTA;
    }

    protected String tmpListaPersoneIni(int numVoci) {
        StringBuffer buffer = new StringBuffer();

        buffer.append("{{Lista persone per anno");
        buffer.append(CAPO);
        buffer.append("|titolo=Morti nel 4 a.C.");
        buffer.append(CAPO);
        buffer.append("|voci=");
        buffer.append(numVoci);
        buffer.append(CAPO);
        buffer.append("|testo=");
        buffer.append(CAPO);

        return buffer.toString();
    }

    protected String fixCorpoParagrafo(String wikiTitle, String titoloParagrafo, int numVoci, LinkedHashMap<String, List<String>> mappaSub) {
        StringBuffer buffer = new StringBuffer();
        List<String> lista;

        for (String key : mappaSub.keySet()) {
            lista = mappaSub.get(key);
            if (lista.size() == 1) {
                if (textService.isValid(key)) {
                    buffer.append(ASTERISCO);
                    buffer.append(key);
                    buffer.append(SEP);
                    buffer.append(lista.get(0));
                    buffer.append(CAPO);
                }
                else {
                    buffer.append(ASTERISCO);
                    buffer.append(lista.get(0));
                    buffer.append(CAPO);
                }
            }
            else {
                if (textService.isValid(key)) {
                    buffer.append(ASTERISCO);
                    buffer.append(key);
                    buffer.append(CAPO);
                    for (String didascalia : lista) {
                        buffer.append(ASTERISCO);
                        buffer.append(ASTERISCO);
                        buffer.append(didascalia);
                        buffer.append(CAPO);
                    }
                }
                else {
                    for (String didascalia : lista) {
                        buffer.append(ASTERISCO);
                        buffer.append(didascalia);
                        buffer.append(CAPO);
                    }
                }
            }
        }

        return buffer.toString();
    }


    @Override
    protected String note() {
        return VUOTA;
    }


    protected String categorie() {
        StringBuffer buffer = new StringBuffer();
        String cat = SPAZIO + ANNO_INIZIALE + nomeAnno;

        buffer.append(CAPO);
        buffer.append(String.format("*[[Categoria:Liste di nati per anno|%s]]", cat));
        buffer.append(CAPO);
        buffer.append(String.format("*[[Categoria:Nati nel %s| ]]", nomeAnno));

        return buffer.toString();
    }

    protected String fixTitoloParagrafo(String titoloParagrafo, int numVoci) {
        int numeroVisibile = numVoci <= MAX_LINK ? 0 : numVoci;

        if (numVoci <= MAX_LINK) {
            return VUOTA;
        }

        if (textService.isValid(titoloParagrafo)) {
            return wikiUtility.fixTitolo(titoloParagrafo, numeroVisibile);
        }
        else {
            //            return switch (typeCrono) {
            //                case nascita -> wikiUtility.setParagrafo("Senza giorno di nascita",numeroVisibile);
            //                case morte -> wikiUtility.setParagrafo("Senza giorno di morte",numeroVisibile);
            //                default -> VUOTA;
            //            };
            return wikiUtility.setParagrafo("Senza giorno specificato", numeroVisibile);
        }
    }

    /**
     * Esegue la scrittura della pagina <br>
     */
    public void uploadTestNascita(String wikiTitle, String nomeAnno) {
        wikiTitle = UPLOAD_TITLE_DEBUG + textService.primaMaiuscola(wikiTitle);
        this.nomeAnno = nomeAnno;
        typeCrono = AETypeCrono.nascita;
        typeDidascalia = AETypeDidascalia.annoNascita;
        mappaDidascalie = appContext.getBean(ListaAnni.class).nascita(nomeAnno).mappaDidascalie();
        this.esegue(wikiTitle, mappaDidascalie);
    }

    /**
     * Esegue la scrittura della pagina <br>
     */
    public void uploadTestMorte(String wikiTitle, String nomeAnno) {
        wikiTitle = UPLOAD_TITLE_DEBUG + textService.primaMaiuscola(wikiTitle);
        this.nomeAnno = nomeAnno;
        typeCrono = AETypeCrono.morte;
        typeDidascalia = AETypeDidascalia.annoMorte;
        mappaDidascalie = appContext.getBean(ListaAnni.class).morte(nomeAnno).mappaDidascalie();
        this.esegue(wikiTitle, mappaDidascalie);
    }

}
