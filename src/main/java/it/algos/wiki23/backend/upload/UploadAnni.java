package it.algos.wiki23.backend.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaad23.backend.boot.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.packages.crono.anno.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.liste.*;
import it.algos.wiki23.backend.packages.anno.*;
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

    //    private static final int MAX_LINK = 200;
    private static final int MAX_LINK = 50;

    protected AETypeCrono typeCrono;

    protected AETypeDidascalia typeDidascalia;

    private String nomeAnno;

    private int ordineAnno;

    private boolean senzaParagrafi;

    private boolean oltre3Voci;

    private String wikiTitle;

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
        senzaParagrafi = numVoci < MAX_LINK;
        oltre3Voci = numVoci > 3;

        buffer.append(avviso());
        buffer.append(CAPO);
        buffer.append(includeIni());
        buffer.append(fixToc());
        buffer.append(torna(VUOTA));
        buffer.append(tmpListaBio(numVoci));
        buffer.append(includeEnd());
        buffer.append(CAPO);
        buffer.append(tmpListaPersoneIni(numVoci));
        buffer.append(oltre3Voci && senzaParagrafi ? "{{Div col}}" + CAPO : VUOTA);
        buffer.append(body(wikiTitle, mappaDidascalie));
        buffer.append(oltre3Voci && senzaParagrafi ? "{{Div col end}}" : VUOTA);
        buffer.append(uploadTest ? VUOTA : DOPPIE_GRAFFE_END);
        buffer.append(includeIni());
        buffer.append(portale());
        buffer.append(categorie());
        buffer.append(includeEnd());

        return registra(wikiTitle, buffer.toString());
    }

    protected String fixToc() {
        //        return senzaParagrafi ? AETypeToc.noToc.get() : AETypeToc.forceToc.get();
        return AETypeToc.noToc.get();
    }

    @Override
    protected String torna(String wikiTitle) {
        return textService.isValid(nomeAnno) ? String.format("{{Torna a|%s}}", nomeAnno) : VUOTA;
    }

    protected String tmpListaPersoneIni(int numVoci) {
        StringBuffer buffer = new StringBuffer();

        buffer.append("{{Lista persone per anno");
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


    protected String fixTitoloParagrafo(String titoloParagrafo, int numVoci) {
        String titoloVisibile;
        int numeroVisibile = senzaParagrafi ? 0 : numVoci;

        if (senzaParagrafi) {
            return VUOTA;
        }

        if (textService.isValid(titoloParagrafo)) {
            titoloVisibile = wikiUtility.fixTitolo(titoloParagrafo, numeroVisibile);
        }
        else {
            titoloVisibile = switch (typeDidascalia) {
                case annoNascita, annoMorte -> wikiUtility.setParagrafo("Senza giorno specificato", numeroVisibile);
                case giornoNascita, giornoMorte -> wikiUtility.setParagrafo("Senza anno specificato", numeroVisibile);
                default -> VUOTA;
            };
        }

        if (!senzaParagrafi) {
            titoloVisibile += "{{Div col}}" + CAPO;
        }

        return titoloVisibile;
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
        if (!senzaParagrafi) {
            buffer.append("{{Div col end}}" + CAPO);
        }

        return buffer.toString();
    }


    protected String categorie() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(CAPO);
        switch (typeCrono) {
            case nascita -> {
                buffer.append(String.format("*[[Categoria:Liste di nati per anno| %s]]", ordineAnno));
                buffer.append(CAPO);
                buffer.append(String.format("*[[Categoria:Nati nel %s| ]]", nomeAnno));
            }
            case morte -> {
                buffer.append(String.format("*[[Categoria:Liste di morti per anno| %s]]", ordineAnno));
                buffer.append(CAPO);
                buffer.append(String.format("*[[Categoria:Morti nel %s| ]]", nomeAnno));
            }
        } ;

        return buffer.toString();
    }


    /**
     * Esegue la scrittura della pagina <br>
     */
    public void uploadTestNascita(String nomeAnno) {
        Anno anno = annoBackend.findByNome(nomeAnno);
        this.uploadTest = true;
        this.nomeAnno = nomeAnno;
        this.wikiTitle = UPLOAD_TITLE_DEBUG + wikiUtility.wikiTitleNatiAnno(nomeAnno);
        this.ordineAnno = anno != null ? anno.getOrdine() : 0;
        typeCrono = AETypeCrono.nascita;
        typeDidascalia = AETypeDidascalia.annoNascita;
        mappaDidascalie = appContext.getBean(ListaAnni.class).nascita(nomeAnno).mappaDidascalie();
        this.esegue(wikiTitle, mappaDidascalie);
    }

    /**
     * Esegue la scrittura della pagina <br>
     */
    public void uploadTestMorte(String nomeAnno) {
        Anno anno = annoBackend.findByNome(nomeAnno);
        this.uploadTest = true;
        this.nomeAnno = nomeAnno;
        this.wikiTitle = UPLOAD_TITLE_DEBUG + wikiUtility.wikiTitleMortiAnno(nomeAnno);
        this.ordineAnno = anno != null ? anno.getOrdine() : 0;
        typeCrono = AETypeCrono.morte;
        typeDidascalia = AETypeDidascalia.annoMorte;
        mappaDidascalie = appContext.getBean(ListaAnni.class).morte(nomeAnno).mappaDidascalie();
        this.esegue(wikiTitle, mappaDidascalie);
    }


    /**
     * Esegue la scrittura della pagina <br>
     */
    public void uploadNascita(AnnoWiki annoWiki) {
        uploadNascita(annoWiki.nome);
    }

    /**
     * Esegue la scrittura della pagina <br>
     */
    public void uploadNascita(String nomeAnno) {
        Anno anno = annoBackend.findByNome(nomeAnno);
        this.nomeAnno = nomeAnno;
        this.wikiTitle = wikiUtility.wikiTitleNatiAnno(nomeAnno);
        this.ordineAnno = anno != null ? anno.getOrdine() : 0;
        typeCrono = AETypeCrono.nascita;
        typeDidascalia = AETypeDidascalia.annoNascita;
        mappaDidascalie = appContext.getBean(ListaAnni.class).nascita(nomeAnno).mappaDidascalie();
        this.esegue(wikiTitle, mappaDidascalie);
    }




    /**
     * Esegue la scrittura della pagina <br>
     */
    public void uploadMorte(AnnoWiki annoWiki) {
        uploadMorte(annoWiki.nome);
    }

    /**
     * Esegue la scrittura della pagina <br>
     */
    public void uploadMorte(String nomeAnno) {
        Anno anno = annoBackend.findByNome(nomeAnno);
        this.nomeAnno = nomeAnno;
        this.wikiTitle = wikiUtility.wikiTitleMortiAnno(nomeAnno);
        this.ordineAnno = anno != null ? anno.getOrdine() : 0;
        typeCrono = AETypeCrono.morte;
        typeDidascalia = AETypeDidascalia.annoMorte;
        mappaDidascalie = appContext.getBean(ListaAnni.class).morte(nomeAnno).mappaDidascalie();
        this.esegue(wikiTitle, mappaDidascalie);
    }

}
