package it.algos.wiki23.backend.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Fri, 22-Jul-2022
 * Time: 10:20
 * Classe specializzata per caricare (upload) le liste di anni (nati/morti) sul server wiki. <br>
 * Usata fondamentalmente da GiornoWikiView con appContext.getBean(UploadAnni.class).nascita/morte().upload(nomeAnno) <br>
 * <p>
 * Necessita del login come bot <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UploadAnni extends UploadGiorniAnni {


    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(UploadAnni.class).nascita/morte().upload(nomeAnno) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     */
    public UploadAnni() {
        super.summary = "[[Utente:Biobot/anniBio|anniBio]]";
        super.lastUpload = WPref.uploadAnni;
        super.durataUpload = WPref.uploadAnniTime;
        super.nextUpload = WPref.uploadAttivitaPrevisto;
        super.usaParagrafi = WPref.usaParagrafiAnni.is();
        super.typeToc = (AETypeToc) WPref.typeTocAnni.getEnumCurrentObj();
    }// end of constructor


    public UploadAnni typeCrono(AETypeLista type) {
        this.typeCrono = type;
        return this;
    }

    public UploadAnni nascita() {
        this.typeCrono = AETypeLista.annoNascita;
        return this;
    }

    public UploadAnni morte() {
        this.typeCrono = AETypeLista.annoMorte;
        return this;
    }

    public UploadAnni test() {
        this.uploadTest = true;
        return this;
    }

    public void uploadSottoPagine(String wikiTitle, String parente, String sottoPagina, int ordineSottoPagina, List<WrapLista> lista) {
        UploadAnni anno = appContext.getBean(UploadAnni.class).typeCrono(typeCrono);

        if (uploadTest) {
            anno = anno.test();
        }

        anno.uploadSottoPagina(wikiTitle, parente, sottoPagina, ordineSottoPagina, lista);
    }

    /**
     * Esegue la scrittura di tutte le pagine <br>
     * Tutti gli anni nati <br>
     * Tutti gli anni morti <br>
     */
    public WResult uploadAll() {
        WResult result = WResult.errato();
        long inizio = System.currentTimeMillis();

        List<String> anni = annoWikiBackend.findAllNomi();
        for (String nomeAnno : anni) {
            nascita().upload(nomeAnno);
            morte().upload(nomeAnno);
        }

        fixUploadMinuti(inizio);
        return result;
    }


}
