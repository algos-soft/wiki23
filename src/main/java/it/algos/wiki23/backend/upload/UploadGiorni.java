package it.algos.wiki23.backend.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.enumeration.*;
import it.algos.vaad24.backend.wrapper.*;
import it.algos.wiki23.backend.enumeration.*;
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
        super.typeToc = (AETypeToc) WPref.typeTocGiorni.getEnumCurrentObj();
    }// end of constructor


    public UploadGiorni typeCrono(AETypeLista type) {
        this.typeCrono = type;
        return this;
    }

    public UploadGiorni nascita() {
        this.typeCrono = AETypeLista.giornoNascita;
        return this;
    }

    public UploadGiorni morte() {
        this.typeCrono = AETypeLista.giornoMorte;
        return this;
    }

    public UploadGiorni test() {
        this.uploadTest = true;
        return this;
    }

    public void uploadSottoPagine(String wikiTitle, String parente, String sottoPagina, int ordineSottoPagina, List<WrapLista> lista) {
        UploadGiorni giorno = appContext.getBean(UploadGiorni.class).typeCrono(typeCrono);

        if (uploadTest) {
            giorno = giorno.test();
        }

        giorno.uploadSottoPagina(wikiTitle, parente, sottoPagina, ordineSottoPagina, lista);
    }

    /**
     * Esegue la scrittura di tutte le pagine <br>
     * Tutti i giorni nati <br>
     * Tutti i giorni morti <br>
     */
    public WResult uploadAll() {
        WResult result = WResult.errato();
        logger.info(new WrapLog().type(AETypeLog.upload).message("Inizio upload liste nati e morti dei giorni"));
        long inizio = System.currentTimeMillis();
        List<String> giorni;
        String message;
        int modificatiNati;
        int modificatiMorti;

        List<String> mesi = meseBackend.findNomi();
        for (String mese : mesi) {
            giorni = giornoBackend.findNomiByMese(mese);
            modificatiNati = 0;
            modificatiMorti = 0;
            for (String nomeGiorno : giorni) {
                result = nascita().upload(nomeGiorno);
                if (result.isValido() && result.isModificata()) {
                    modificatiNati++;
                }

                result = morte().upload(nomeGiorno);
                if (result.isValido() && result.isModificata()) {
                    modificatiMorti++;
                }
            }
            message = String.format("Modificate sul server %d pagine di 'nati' e %d di 'morti' per il mese di %s", modificatiNati, modificatiMorti, mese);
            logger.info(new WrapLog().type(AETypeLog.upload).message(message));
        }

        fixUploadMinuti(inizio);
        return result;
    }

    @Override
    protected String categorie() {
        StringBuffer buffer = new StringBuffer();
        String message;
        String title = wikiUtility.wikiTitle(typeCrono, nomeLista);

        if (uploadTest) {
            buffer.append(CAPO);
            message = String.format("{{Categorie bozza|[[Categoria:Liste di %s per %s| %s]][[Categoria:%s| ]]}}", typeCrono.getTagLower(), typeCrono.getGiornoAnno(), ordineGiornoAnno, title);
            buffer.append(message);
        }
        else {
            buffer.append(CAPO);
            buffer.append(String.format("*[[Categoria:Liste di %s per %s| %s]]", typeCrono.getTagLower(), typeCrono.getGiornoAnno(), ordineGiornoAnno));
            buffer.append(CAPO);
            buffer.append(String.format("*[[Categoria:%s| ]]", title));
        }

        return buffer.toString();
    }

    @Override
    protected String categorieSotto() {
        StringBuffer buffer = new StringBuffer();

        if (uploadTest) {
            return VUOTA;
        }

        buffer.append(CAPO);
        buffer.append(String.format("*[[Categoria:Liste di %s per %s| %s]]", typeCrono.getTagLower(), typeCrono.getGiornoAnno(), ordineGiornoAnno));
        buffer.append(CAPO);

        return buffer.toString();
    }

}
