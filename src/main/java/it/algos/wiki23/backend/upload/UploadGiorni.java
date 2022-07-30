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
public class UploadGiorni extends UploadGiorniAnni {


    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(UploadAttivita.class, attivita) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     */
    public UploadGiorni() {
        super.summary="[[Utente:Biobot/giorniBio|giorniBio]]";
    }// end of constructor

    //    /**
    //     * Esegue la scrittura base della pagina <br>
    //     */
    //    public void uploxxxxad(String nomeGiorno) {
    //        Giorno giorno = giornoBackend.findByNome(nomeGiorno);
    //        this.nomeGiornoAnno = nomeGiorno;
    //        this.ordineGiornoAnno = giorno != null ? giorno.getOrdine() : 0;
    //
    //        WResult result = appContext.getBean(ListaGiorni.class).nascita(nomeGiorno).testoBody();
    //        this.esegue(wikiTitle, result.getContent(), result.getIntValue());
    //    }


    /**
     * Esegue la scrittura della pagina <br>
     */
    public void uploadNascita(String nomeGiorno) {
        Giorno giorno;
        WResult result;
        this.typeCrono = AETypeCrono.giornoNascita;
        this.nomeGiornoAnno = nomeGiorno;
        giorno = giornoBackend.findByNome(nomeGiorno);
        this.ordineGiornoAnno = giorno != null ? giorno.getOrdine() : 0;
        this.wikiTitle = wikiUtility.wikiTitleNatiGiorno(nomeGiorno);
        result = appContext.getBean(ListaGiorni.class).nascita(nomeGiorno).testoBody();
        this.esegue(wikiTitle, result.getContent(), result.getIntValue());
    }

    /**
     * Esegue la scrittura della pagina <br>
     */
    public void uploadMorte(String nomeGiorno) {
        Giorno giorno;
        WResult result;
        this.typeCrono = AETypeCrono.giornoMorte;
        this.nomeGiornoAnno = nomeGiorno;
        giorno = giornoBackend.findByNome(nomeGiorno);
        this.ordineGiornoAnno = giorno != null ? giorno.getOrdine() : 0;
        this.wikiTitle = wikiUtility.wikiTitleMortiGiorno(nomeGiorno);
        result = appContext.getBean(ListaGiorni.class).morte(nomeGiorno).testoBody();
        this.esegue(wikiTitle, result.getContent(), result.getIntValue());
    }

    /**
     * Esegue la scrittura della pagina <br>
     */
    public void uploadTestNascita(String nomeGiorno) {
        Giorno giorno;
        WResult result;
        this.typeCrono = AETypeCrono.giornoNascita;
        this.nomeGiornoAnno = nomeGiorno;
        this.uploadTest = true;
        giorno = giornoBackend.findByNome(nomeGiorno);
        this.ordineGiornoAnno = giorno != null ? giorno.getOrdine() : 0;
        this.wikiTitle = UPLOAD_TITLE_DEBUG + wikiUtility.wikiTitleNatiGiorno(nomeGiorno);
        result = appContext.getBean(ListaGiorni.class).nascita(nomeGiorno).testoBody();
        this.esegue(wikiTitle, result.getContent(), result.getIntValue());
    }

    /**
     * Esegue la scrittura della pagina <br>
     */
    public void uploadTestMorte(String nomeGiorno) {
        Giorno giorno;
        WResult result;
        this.typeCrono = AETypeCrono.giornoMorte;
        this.nomeGiornoAnno = nomeGiorno;
        this.uploadTest = true;
        giorno = giornoBackend.findByNome(nomeGiorno);
        this.ordineGiornoAnno = giorno != null ? giorno.getOrdine() : 0;
        this.wikiTitle = UPLOAD_TITLE_DEBUG + wikiUtility.wikiTitleMortiGiorno(nomeGiorno);
        result = appContext.getBean(ListaGiorni.class).morte(nomeGiorno).testoBody();
        this.esegue(wikiTitle, result.getContent(), result.getIntValue());

    }

}
