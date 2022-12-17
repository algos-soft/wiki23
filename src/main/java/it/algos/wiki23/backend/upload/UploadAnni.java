package it.algos.wiki23.backend.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.enumeration.*;
import it.algos.vaad24.backend.packages.crono.secolo.*;
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

    /**
     * Esegue la scrittura della pagina <br>
     *
     */
    @Override
    public WResult upload(String nomeAnno) {
        anno = annoWikiBackend.findByNome(nomeAnno);
        return super.upload(nomeAnno);
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
        logger.info(new WrapLog().type(AETypeLog.upload).message("Inizio upload liste nati e morti degli anni"));
        long inizio = System.currentTimeMillis();
        List<String> anni;
        String message;
        int modificatiNati;
        int modificatiMorti;

        List<String> secoli = secoloBackend.findNomi();
        for (String secolo : secoli) {
            anni = annoBackend.findNomiBySecolo(secolo);
            modificatiNati = 0;
            modificatiMorti = 0;

            for (String nomeAnno : anni) {
                result = nascita().upload(nomeAnno);
                if (result.isValido() && result.isModificata()) {
                    modificatiNati++;
                }

                result = morte().upload(nomeAnno);
                if (result.isValido() && result.isModificata()) {
                    modificatiMorti++;
                }
            }
            message = String.format("Modificate sul server %d pagine di 'nati' e %d di 'morti' per il secolo %s", modificatiNati, modificatiMorti, secolo);
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
        Secolo secolo = secoloBackend.getSecolo(anno.secolo);
        String secoloTxt = secolo != null ? secolo.nome : VUOTA;

        if (uploadTest) {
            buffer.append(CAPO);
            if (WPref.sottoCategorieNatiPerAnno.is()) {
                message = String.format("{{Categorie bozza|[[Categoria:Liste di %s nel %s| %s]][[Categoria:%s| ]]}}", typeCrono.getTagLower(), secoloTxt, ordineGiornoAnno, title);
            }
            else {
                message = String.format("{{Categorie bozza|[[Categoria:Liste di %s per %s| %s]][[Categoria:%s| ]]}}", typeCrono.getTagLower(), typeCrono.getGiornoAnno(), ordineGiornoAnno, title);
            }
            buffer.append(message);
        }
        else {
            if (WPref.sottoCategorieNatiPerAnno.is()) {
                buffer.append(CAPO);
                buffer.append(String.format("*[[Categoria:Liste di %s nel %s| %s]]", typeCrono.getTagLower(), secoloTxt, ordineGiornoAnno));
                buffer.append(CAPO);
                buffer.append(String.format("*[[Categoria:%s| ]]", title));
            }
            else {
                buffer.append(CAPO);
                buffer.append(String.format("*[[Categoria:Liste di %s per %s| %s]]", typeCrono.getTagLower(), typeCrono.getGiornoAnno(), ordineGiornoAnno));
                buffer.append(CAPO);
                buffer.append(String.format("*[[Categoria:%s| ]]", title));
            }
        }

        return buffer.toString();
    }

    @Override
    protected String categorieSotto() {
        StringBuffer buffer = new StringBuffer();
        Secolo secolo = secoloBackend.getSecolo(anno.secolo);
        String secoloTxt = secolo != null ? secolo.nome : VUOTA;

        if (uploadTest) {
            return VUOTA;
        }

        if (WPref.sottoCategorieNatiPerAnno.is()) {
            buffer.append(CAPO);
            buffer.append(String.format("*[[Categoria:Liste di %s nel %s| %s]]", typeCrono.getTagLower(), secoloTxt, ordineGiornoAnno));
            buffer.append(CAPO);
        }
        else {
            buffer.append(CAPO);
            buffer.append(String.format("*[[Categoria:Liste di %s per %s| %s]]", typeCrono.getTagLower(), typeCrono.getGiornoAnno(), ordineGiornoAnno));
            buffer.append(CAPO);
        }

        return buffer.toString();
    }

}
