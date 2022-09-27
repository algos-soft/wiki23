package it.algos.wiki23.backend.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.liste.*;
import it.algos.wiki23.backend.packages.nazionalita.*;
import static it.algos.wiki23.backend.upload.UploadAttivita.*;
import it.algos.wiki23.backend.wrapper.*;
import static it.algos.wiki23.wiki.query.QueryWrite.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Tue, 14-Jun-2022
 * Time: 18:48
 * Classe specializzata per caricare (upload) le liste di nazionalità sul server wiki. <br>
 * Usata fondamentalmente da NazionalitaWikiView con appContext.getBean(UploadNazionalita.class).upload(nomeNazionalitaPlurale) <br>
 * <p>
 * Necessita del login come bot <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UploadNazionalita extends UploadAttivitaNazionalita {

    public static final String UPLOAD_TITLE_PROJECT_NAZIONALITA = UPLOAD_TITLE_PROJECT + "Nazionalità/";


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public NazionalitaBackend nazionalitaBackend;

    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(UploadNazionalita.class).upload(nomeNazionalitaPlurale) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     */
    public UploadNazionalita() {
        super.summary = "[[Utente:Biobot/nazionalitàBio|nazionalitàBio]]";
        super.titoloLinkParagrafo = TITOLO_LINK_PARAGRAFO_ATTIVITA;
        super.titoloLinkVediAnche = TITOLO_LINK_PARAGRAFO_NAZIONALITA;
        super.typeCrono = AETypeLista.nazionalitaPlurale;
        super.lastUpload = WPref.uploadNazionalita;
        super.durataUpload = WPref.uploadNazionalitaTime;
        super.nextUpload = WPref.uploadNazionalitaPrevisto;
    }// end of constructor


    public UploadNazionalita singolare() {
        this.typeCrono = AETypeLista.nazionalitaSingolare;
        return this;
    }

    public UploadNazionalita plurale() {
        this.typeCrono = AETypeLista.nazionalitaPlurale;
        return this;
    }

    protected String incipit() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("Questa");
        buffer.append(textService.setRef(INFO_PAGINA_NAZIONALITA));
        buffer.append(" è una lista");
        buffer.append(textService.setRef(INFO_DIDASCALIE));
        buffer.append(textService.setRef(INFO_ORDINE));
        buffer.append(" di persone");
        buffer.append(textService.setRef(INFO_PERSONA_NAZIONALITA));
        buffer.append(" presenti");
        buffer.append(textService.setRef(INFO_LISTA));
        buffer.append(" nell'enciclopedia che hanno come nazionalità");
        buffer.append(textService.setRef(INFO_NAZIONALITA_PREVISTE));
        buffer.append(String.format(" quella di '''%s'''.", nomeLista));
        buffer.append(" Le persone sono suddivise");
        buffer.append(textService.setRef(INFO_PARAGRAFI_ATTIVITA));
        buffer.append(" per attività.");
        buffer.append(textService.setRef(INFO_ATTIVITA_PREVISTE));
        if (mappaWrap.containsKey(TAG_LISTA_ALTRE)) {
            buffer.append(textService.setRef(INFO_ALTRE_ATTIVITA));
        }

        return buffer.toString();
    }


    protected String incipitSottoPagina(String nazionalita, String attivita, int numVoci) {
        StringBuffer buffer = new StringBuffer();
        this.nomeLista = nazionalita;
        this.nomeSottoPagina = attivita;
        String naz = textService.primaMaiuscola(nazionalita);
        String att = textService.primaMaiuscola(attivita);
        String message;

        buffer.append("Questa");
        message = String.format(INFO_SOTTOPAGINA_DI_NAZIONALITA, naz, att, numVoci, att, naz);
        buffer.append(textService.setRef(message));
        buffer.append(" è una lista");
        buffer.append(textService.setRef(INFO_DIDASCALIE));
        buffer.append(textService.setRef(INFO_ORDINE));
        buffer.append(" di persone");
        buffer.append(textService.setRef(INFO_PERSONA_NAZIONALITA));
        buffer.append(" presenti");
        buffer.append(textService.setRef(INFO_LISTA));
        buffer.append(" nell'enciclopedia che hanno come nazionalità");
        buffer.append(textService.setRef(INFO_NAZIONALITA_PREVISTE));
        buffer.append(String.format(" quella di '''%s'''", nazionalita.toLowerCase()));
        if (attivita.equals(TAG_LISTA_ALTRE)) {
            buffer.append(" e non usano il parametro ''attività'' oppure hanno un'attività di difficile elaborazione da parte del '''[[Utente:Biobot|<span style=\"color:green;\">bot</span>]]'''");
        }
        else {
            buffer.append(String.format(" e sono '''%s'''.", attivita.toLowerCase()));
        }
        buffer.append(textService.setRef(INFO_ATTIVITA_PREVISTE));

        return buffer.toString();
    }


    protected String incipitSottoSottoPagina(String nazionalita, String attivita, String keyParagrafo, int numVoci) {
        StringBuffer buffer = new StringBuffer();
        String att = textService.primaMaiuscola(attivita) + SLASH + textService.primaMaiuscola(nazionalita); ;
        String naz = textService.primaMaiuscola(keyParagrafo);
        String message;

        buffer.append("Questa");
        message = String.format(INFO_SOTTOPAGINA_DI_NAZIONALITA, att, naz, numVoci, naz, att);
        buffer.append(textService.setRef(message));
        buffer.append(" è una lista");
        buffer.append(textService.setRef(INFO_DIDASCALIE));
        buffer.append(" di persone");
        buffer.append(textService.setRef(INFO_PERSONA_NAZIONALITA));
        buffer.append(" presenti");
        buffer.append(textService.setRef(INFO_LISTA));
        buffer.append(" nell'enciclopedia che hanno come nazionalità");
        buffer.append(textService.setRef(INFO_NAZIONALITA_PREVISTE));
        buffer.append(String.format(" quella di '''%s'''", nazionalita.toLowerCase()));
        if (naz.equals(TAG_LISTA_ALTRE)) {
            buffer.append(" e non usano il parametro ''nazionalità'' oppure hanno un'nazionalità di difficile elaborazione da parte del '''[[Utente:Biobot|<span style=\"color:green;\">bot</span>]]");
        }
        else {
            buffer.append(String.format(", sono '''%s'''", attivita.toLowerCase()));
        }
        buffer.append(textService.setRef(INFO_ATTIVITA_PREVISTE));
        buffer.append(" ed il cognome");
        buffer.append(textService.setRef(INFO_ORDINE));
        buffer.append(String.format(" inizia con la lettera '''%s'''.", keyParagrafo.toUpperCase()));

        return buffer.toString();
    }

    public void uploadSottoPagine(String wikiTitle, String attNazPrincipale, String attNazSottoPagina, List<WrapLista> lista) {
        if (uploadTest) {
            appContext.getBean(UploadNazionalita.class).test().uploadSottoPagina(wikiTitle, nomeLista, attNazSottoPagina, lista);
        }
        else {
            appContext.getBean(UploadNazionalita.class).uploadSottoPagina(wikiTitle, nomeLista, attNazSottoPagina, lista);
        }
    }


    public void uploadSottoSottoPagine(String wikiTitle, String attNazPrincipale, String attNazSottoPagina, String keyParagrafo, List<WrapLista> lista) {
        if (uploadTest) {
            appContext.getBean(UploadNazionalita.class).test().uploadSottoSottoPagina(wikiTitle, attNazPrincipale, attNazSottoPagina, keyParagrafo, lista);
        }
        else {
            appContext.getBean(UploadNazionalita.class).uploadSottoSottoPagina(wikiTitle, attNazPrincipale, attNazSottoPagina, keyParagrafo, lista);
        }
    }

    protected String correlate() {
        StringBuffer buffer = new StringBuffer();
        String cat = textService.primaMaiuscola(nomeLista);

        buffer.append(wikiUtility.setParagrafo("Voci correlate"));
        buffer.append(String.format("*[[:Categoria:%s]]", cat));
        buffer.append(CAPO);
        buffer.append("*[[Progetto:Biografie/Nazionalità]]");

        return buffer.toString();
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
        buffer.append(String.format("*[[Categoria:Bio nazionalità|%s]]", cat));

        return buffer.toString();
    }


    /**
     * Esegue la scrittura di tutte le pagine di nazionalità <br>
     */
    public WResult uploadAll() {
        WResult result = WResult.errato();
        long inizio = System.currentTimeMillis();

        List<String> listaPlurali = nazionalitaBackend.findAllPlurali();
        for (String plurale : listaPlurali) {
            upload(plurale);
        }

        fixUploadMinuti(inizio);
        return result;
    }

}
