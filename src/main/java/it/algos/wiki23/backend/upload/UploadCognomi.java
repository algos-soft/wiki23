package it.algos.wiki23.backend.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.liste.*;
import it.algos.wiki23.backend.packages.cognome.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Tue, 18-Oct-2022
 * Time: 14:13
 * Classe specializzata per caricare (upload) le liste di cognomi sul server wiki. <br>
 * Usata fondamentalmente da CognomiView con appContext.getBean(UploadCognomi.class).upload(cognomeTxt) <br>
 * <p>
 * Necessita del login come bot <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UploadCognomi extends Upload {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public CognomeBackend cognomeBackend;

    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(UploadAttivita.class).upload(nomeAttivitaPlurale) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     */
    public UploadCognomi() {
        super.summary = "[[Utente:Biobot/cognomiBio|cognomiBio]]";
        super.titoloLinkParagrafo = TITOLO_LINK_PARAGRAFO_NAZIONALITA;
        super.titoloLinkVediAnche = PATH_COGNOMI;
        super.typeToc = AETypeToc.forceToc;
        super.typeCrono = AETypeLista.cognomi;
        super.lastUpload = WPref.uploadCognomi;
        super.durataUpload = WPref.uploadCognomiTime;
        super.nextUpload = WPref.uploadCognomiPrevisto;
    }// end of constructor


    public UploadCognomi test() {
        this.uploadTest = true;
        return this;

    }


    /**
     * Esegue la scrittura della pagina <br>
     */
    public WResult upload(final String cognomeTxt) {
        this.nomeLista = textService.primaMaiuscola(cognomeTxt);

        if (textService.isValid(nomeLista)) {
            wikiTitle = PATH_COGNOMI + nomeLista;

            mappaWrap = appContext.getBean(ListaCognomi.class, nomeLista).mappaWrap();

            if (uploadTest) {
                this.wikiTitle = UPLOAD_TITLE_DEBUG + wikiTitle;
            }

            if (textService.isValid(wikiTitle) && mappaWrap != null && mappaWrap.size() > 0) {
               return esegueUpload(wikiTitle, mappaWrap);
            }
        }

        return WResult.crea();
    }


    /**
     * Esegue la scrittura della sottopagina <br>
     */
    public WResult uploadSottoPagina(final String wikiTitle, String nomeLista, String nomeSottoPagina, List<WrapLista> lista) {
        this.wikiTitle = wikiTitle;
        this.nomeLista = nomeLista;
        this.nomeSottoPagina = nomeSottoPagina;

        if (uploadTest) {
            this.wikiTitle = UPLOAD_TITLE_DEBUG + wikiTitle;
        }

        if (textService.isValid(this.wikiTitle) && lista != null) {
            this.esegueUploadSotto(this.wikiTitle, nomeLista, nomeSottoPagina, lista);
        }

        return WResult.crea();
    }

    protected WResult esegueUpload(String wikiTitle, LinkedHashMap<String, List<WrapLista>> mappa) {
        StringBuffer buffer = new StringBuffer();
        int numVoci = wikiUtility.getSizeAllWrap(mappa);

        if (numVoci < 1) {
            logger.info(new WrapLog().message(String.format("Non creata la pagina %s perché non ci sono abbastanza voci", wikiTitle, numVoci)));
            return WResult.crea();
        }

        buffer.append(avviso());
        buffer.append(CAPO);
        buffer.append(includeIni());
        buffer.append(fixToc());
        buffer.append(fixUnconnected());
        //        buffer.append(torna(wikiTitle));
        buffer.append(tmpListaBio(numVoci));
        buffer.append(includeEnd());
        buffer.append(CAPO);
        buffer.append(incipit());
        buffer.append(testoBody(mappa));
        //        buffer.append(uploadTest ? VUOTA : DOPPIE_GRAFFE_END);
        //                buffer.append(note());
        buffer.append(CAPO);
        buffer.append(portale());
        buffer.append(categorie());

        return registra(wikiTitle, buffer.toString().trim());
    }


    protected WResult esegueUploadSotto(String wikiTitle, String nomeLista, String nomeSottoPagina, List<WrapLista> lista) {
        StringBuffer buffer = new StringBuffer();
        int numVoci = lista.size();

        buffer.append(avviso());
        buffer.append(CAPO);
        buffer.append(includeIni());
        buffer.append(AETypeToc.noToc.get());
        buffer.append(fixUnconnected());
        buffer.append(torna(wikiTitle));
        buffer.append(tmpListaBio(numVoci));
        buffer.append(includeEnd());
        buffer.append(CAPO);
        //        buffer.append(incipitSottoPagina(nomeLista, nomeSottoPagina, numVoci));
        buffer.append(CAPO);
        buffer.append(testoSottoBody(lista));
//        buffer.append(note());
        buffer.append(CAPO);
        buffer.append(correlate());
        buffer.append(CAPO);
        buffer.append(portale());
        buffer.append(categorie());

        return registra(wikiTitle, buffer.toString().trim());
    }

    protected String incipit() {
        StringBuffer buffer = new StringBuffer();
        if (true) {
            buffer.append(String.format("{{incipit lista cognomi|cognome=%s}}", nomeLista));
        }
        else {
            buffer.append("Questa");
            buffer.append(textService.setRef(INFO_PAGINA_COGNOMI));
            buffer.append(" è una lista");
            buffer.append(textService.setRef(INFO_DIDASCALIE));
            buffer.append(textService.setRef(INFO_ORDINE));
            buffer.append(" di persone");
            buffer.append(" presenti");
            buffer.append(textService.setRef(INFO_LISTA));
            buffer.append(" nell'enciclopedia che hanno come cognome");
            buffer.append(String.format(" quello di '''%s'''.", nomeLista));
            buffer.append(" Le persone sono suddivise");
            buffer.append(textService.setRef(INFO_PARAGRAFI_ATTIVITA));
            buffer.append(" per attività.");
            buffer.append(textService.setRef(INFO_ATTIVITA_PREVISTE));
            if (mappaWrap.containsKey(TAG_LISTA_ALTRE)) {
                buffer.append(textService.setRef(INFO_ALTRE_ATTIVITA));
            }
        }
        buffer.append(CAPO);

        return buffer.toString();
    }

    public String testoBody(Map<String, List<WrapLista>> mappa) {
        StringBuffer buffer = new StringBuffer();
        List<WrapLista> lista;
        int numVoci;
        int max = WPref.sogliaSottoPagina.getInt();
        int maxDiv = WPref.sogliaDiv.getInt();
        boolean usaDivBase = WPref.usaDivAttNaz.is();
        boolean usaDiv;
        String titoloParagrafoLink;
        String vedi;
        String parente;

        for (String keyParagrafo : mappa.keySet()) {
            lista = mappa.get(keyParagrafo);
            numVoci = lista.size();
            titoloParagrafoLink = lista.get(0).titoloParagrafoLink;
            buffer.append(wikiUtility.fixTitolo(VUOTA, titoloParagrafoLink, numVoci));

            if (WPref.usaSottoCognomi.is() && numVoci > max) {
                parente = String.format("%s%s%s%s", titoloLinkVediAnche, textService.primaMaiuscola(nomeLista), SLASH, keyParagrafo);
                vedi = String.format("{{Vedi anche|%s}}", parente);
                buffer.append(vedi + CAPO);
                uploadSottoPagine(parente, nomeLista, keyParagrafo, lista);
            }
            else {
                usaDiv = usaDivBase ? lista.size() > maxDiv : false;
                buffer.append(usaDiv ? "{{Div col}}" + CAPO : VUOTA);
                for (WrapLista wrap : lista) {
                    buffer.append(ASTERISCO);
                    buffer.append(wrap.didascaliaBreve);
                    buffer.append(CAPO);
                }
                buffer.append(usaDiv ? "{{Div col end}}" + CAPO : VUOTA);
            }
        }

        return buffer.toString().trim();
    }


    public String testoSottoBody(List<WrapLista> listaWrap) {
        StringBuffer buffer = new StringBuffer();
        String paragrafo;
        LinkedHashMap<String, List<WrapLista>> mappaWrapSotto = new LinkedHashMap<>();
        List<WrapLista> listaTmp;

        for (WrapLista wrap : listaWrap) {
            paragrafo = wrap.titoloSottoParagrafo;

            if (mappaWrapSotto.containsKey(paragrafo)) {
                listaTmp = mappaWrapSotto.get(paragrafo);
            }
            else {
                listaTmp = new ArrayList();
            }
            listaTmp.add(wrap);
            mappaWrapSotto.put(paragrafo, listaTmp);
        }

        for (String keyParagrafoSotto : mappaWrapSotto.keySet()) {
            buffer.append(wikiUtility.fixTitolo(keyParagrafoSotto, mappaWrapSotto.get(keyParagrafoSotto).size()));
            for (WrapLista wrap : mappaWrapSotto.get(keyParagrafoSotto)) {
                buffer.append(ASTERISCO);
                buffer.append(wrap.didascaliaBreve);
                buffer.append(CAPO);
            }
        }

        return buffer.toString().trim();
    }

    public void uploadSottoPagine(String wikiTitle, String nomeLista, String keyParagrafo, List<WrapLista> lista) {
        if (uploadTest) {
            appContext.getBean(UploadCognomi.class).test().uploadSottoPagina(wikiTitle, nomeLista,keyParagrafo,lista);
        }
        else {
            appContext.getBean(UploadCognomi.class).uploadSottoPagina(wikiTitle, nomeLista,keyParagrafo,lista);
        }
    }

    protected String categorie() {
        if (uploadTest) {
            return VUOTA;
        }

        StringBuffer buffer = new StringBuffer();
        String cat = textService.primaMaiuscola(nomeLista);

        if (textService.isValid(nomeSottoPagina)) {
            cat += SLASH + nomeSottoPagina;
        }

        buffer.append(CAPO);
        buffer.append(String.format("*[[Categoria:Liste di persone per cognome|%s]]", cat));

        return buffer.toString();
    }

    /**
     * Esegue la scrittura di tutte le pagine di nazionalità <br>
     */
    public WResult uploadAll() {
        WResult result = WResult.errato();
        long inizio = System.currentTimeMillis();

        List<String> listaPlurali = cognomeBackend.findCognomiStampabiliSortNumBio();
        for (String plurale : listaPlurali) {
            upload(plurale);
        }

        fixUploadMinuti(inizio);
        return result;
    }

}
