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
public class UploadAnni extends UploadGiorniAnni {


    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(UploadAttivita.class, attivita) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     */
    public UploadAnni() {
        super.summary = "[[Utente:Biobot/anniBio|anniBio]]";
        super.lastUpload = WPref.uploadAnni;
        super.durataUpload = WPref.uploadAnniTime;
        super.nextUpload = WPref.uploadAttivitaPrevisto;
    }// end of constructor

    /**
     * Esegue la scrittura di tutte le pagine <br>
     * Tutti i giorni nati <br>
     * Tutti i giorni morti <br>
     */
    public WResult uploadAll() {
        WResult result = WResult.errato();
        long inizio = System.currentTimeMillis();

        List<String> anni = annoWikiBackend.findAllNomi();
        for (String nomeAnno : anni.subList(1075, 1077)) {
            uploadNascita(nomeAnno);
            uploadMorte(nomeAnno);
        }

        fixUploadMinuti(inizio);
        return result;
    }

    /**
     * Esegue la scrittura della pagina <br>
     */
    public void uploadNascita(String nomeAnno) {
        AnnoWiki anno;
        WResult result;
        this.typeCrono = AETypeCrono.annoNascita;
        this.nomeGiornoAnno = nomeAnno;
        anno = annoWikiBackend.findByNome(nomeAnno);
        this.ordineGiornoAnno = anno != null ? anno.getOrdine() : 0;
        this.wikiTitle = wikiUtility.wikiTitleNatiAnno(nomeAnno);
        result = appContext.getBean(ListaAnni.class).nascita(nomeAnno).testoBody();
        if (result.getIntValue() > 0) {
            this.esegue(wikiTitle, result.getContent(), result.getIntValue());
        }
    }


    /**
     * Esegue la scrittura della pagina <br>
     */
    public void uploadMorte(String nomeAnno) {
        AnnoWiki anno;
        WResult result;
        this.typeCrono = AETypeCrono.annoMorte;
        this.nomeGiornoAnno = nomeAnno;
        anno = annoWikiBackend.findByNome(nomeAnno);
        this.ordineGiornoAnno = anno != null ? anno.getOrdine() : 0;
        this.wikiTitle = wikiUtility.wikiTitleMortiAnno(nomeAnno);
        result = appContext.getBean(ListaAnni.class).morte(nomeAnno).testoBody();
        if (result.getIntValue() > 0) {
            this.esegue(wikiTitle, result.getContent(), result.getIntValue());
        }
    }


    /**
     * Esegue la scrittura della pagina <br>
     */
    public void uploadTestNascita(String nomeAnno) {
        AnnoWiki anno;
        WResult result;
        this.typeCrono = AETypeCrono.annoNascita;
        this.nomeGiornoAnno = nomeAnno;
        this.uploadTest = true;
        anno = annoWikiBackend.findByNome(nomeAnno);
        this.ordineGiornoAnno = anno != null ? anno.getOrdine() : 0;
        this.wikiTitle = UPLOAD_TITLE_DEBUG + wikiUtility.wikiTitleNatiAnno(nomeAnno);
        result = appContext.getBean(ListaAnni.class).nascita(nomeAnno).testoBody();
        if (result.getIntValue() > 0) {
            this.esegue(wikiTitle, result.getContent(), result.getIntValue());
        }
    }

    /**
     * Esegue la scrittura della pagina <br>
     */
    public void uploadTestMorte(String nomeAnno) {
        AnnoWiki anno;
        WResult result;
        this.typeCrono = AETypeCrono.annoMorte;
        this.nomeGiornoAnno = nomeAnno;
        this.uploadTest = true;
        anno = annoWikiBackend.findByNome(nomeAnno);
        this.ordineGiornoAnno = anno != null ? anno.getOrdine() : 0;
        this.wikiTitle = UPLOAD_TITLE_DEBUG + wikiUtility.wikiTitleMortiAnno(nomeAnno);
        result = appContext.getBean(ListaAnni.class).morte(nomeAnno).testoBody();
        if (result.getIntValue() > 0) {
            this.esegue(wikiTitle, result.getContent(), result.getIntValue());
        }
    }


}
