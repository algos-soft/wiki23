package it.algos.wiki23.backend.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.liste.*;
import it.algos.wiki23.backend.packages.giorno.*;
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
 * Classe specializzata per caricare (upload) le liste di giorni (nati/morti) sul server wiki. <br>
 * Usata fondamentalmente da GiornoWikiView con appContext.getBean(UploadGiorni.class).nascita/morte().upload(nomeGiorno) <br>
 * <p>
 * Necessita del login come bot <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UploadGiorni extends UploadGiorniAnni {


    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(UploadGiorni.class).nascita/morte().upload(nomeGiorno) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     */
    public UploadGiorni() {
        super.summary = "[[Utente:Biobot/giorniBio|giorniBio]]";
        super.lastUpload = WPref.uploadGiorni;
        super.durataUpload = WPref.uploadGiorniTime;
        super.nextUpload = WPref.uploadGiorniPrevisto;
        super.usaParagrafi = WPref.usaParagrafiGiorni.is();
    }// end of constructor


    public UploadGiorni nascita() {
        this.typeCrono = AETypeLista.giornoNascita;
        return this;
    }

    public UploadGiorni morte() {
        this.typeCrono = AETypeLista.giornoMorte;
        return this;
    }


    /**
     * Esegue la scrittura di tutte le pagine <br>
     * Tutti i giorni nati <br>
     * Tutti i giorni morti <br>
     */
    public WResult uploadAll() {
        WResult result = WResult.errato();
        long inizio = System.currentTimeMillis();

        List<String> giorni = giornoWikiBackend.findAllNomi();
        for (String nomeGiorno : giorni) {
            nascita().upload(nomeGiorno);
            morte().upload(nomeGiorno);
        }

        fixUploadMinuti(inizio);
        return result;
    }

}
