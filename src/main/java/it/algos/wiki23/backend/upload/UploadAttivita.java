package it.algos.wiki23.backend.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaad23.backend.boot.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.boot.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.liste.*;
import it.algos.wiki23.backend.packages.attivita.*;
import it.algos.wiki23.backend.wrapper.*;
import it.algos.wiki23.wiki.query.*;
import static it.algos.wiki23.wiki.query.QueryWrite.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Wed, 08-Jun-2022
 * Time: 06:55
 * Classe specializzata per caricare (upload) le liste di attività sul server wiki. <br>
 * Usata fondamentalmente da AttivitaWikiView con appContext.getBean(UploadAttivita.class).upload(nomeAttivitaPlurale) <br>
 * <p>
 * Necessita del login come bot <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UploadAttivita extends UploadAttivitaNazionalita {

    public static final String UPLOAD_TITLE_PROJECT_ATTIVITA = UPLOAD_TITLE_PROJECT + "Attività/";

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AttivitaBackend attivitaBackend;


    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(UploadAttivita.class).upload(nomeAttivitaPlurale) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     */
    public UploadAttivita() {
        super.summary = "[[Utente:Biobot/attivitàBio|attivitàBio]]";
        super.titoloLinkParagrafo = TITOLO_LINK_PARAGRAFO_NAZIONALITA;
        super.titoloLinkVediAnche = TITOLO_LINK_PARAGRAFO_ATTIVITA;
        super.typeCrono = AETypeLista.attivitaPlurale;
        super.lastUpload = WPref.uploadAttivita;
        super.durataUpload = WPref.uploadAttivitaTime;
        super.nextUpload = WPref.uploadAttivitaPrevisto;
    }// end of constructor


    public UploadAttivita singolare() {
        this.typeCrono = AETypeLista.attivitaSingolare;
        return this;
    }

    public UploadAttivita plurale() {
        this.typeCrono = AETypeLista.attivitaPlurale;
        return this;
    }

    protected String incipit() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("Questa");
        buffer.append(textService.setRef(INFO_PAGINA_ATTIVITA));
        buffer.append(" è una lista");
        buffer.append(textService.setRef(INFO_DIDASCALIE));
        buffer.append(textService.setRef(INFO_ORDINE));
        buffer.append(" di persone");
        buffer.append(textService.setRef(INFO_PERSONA_ATTIVITA));
        buffer.append(" presenti");
        buffer.append(textService.setRef(INFO_LISTA));
        buffer.append(" nell'enciclopedia che hanno come attività");
        buffer.append(textService.setRef(INFO_ATTIVITA_PREVISTE));
        buffer.append(String.format(" quella di '''%s'''.", nomeLista));
        buffer.append(" Le persone sono suddivise");
        buffer.append(textService.setRef(INFO_PARAGRAFI_NAZIONALITA));
        buffer.append(" per nazionalità.");
        buffer.append(textService.setRef(INFO_NAZIONALITA_PREVISTE));
        buffer.append(textService.setRef(INFO_ALTRE_NAZIONALITA));

        return buffer.toString();
    }


    protected String incipitSottoPagina(String attivita, String nazionalita, int numVoci) {
        StringBuffer buffer = new StringBuffer();
        this.nomeLista = attivita;
        this.nomeNazionalitaSottoPagina = nazionalita;
        String att = textService.primaMaiuscola(attivita);
        String naz = textService.primaMaiuscola(nazionalita);
        String message;

        buffer.append("Questa");
        message = String.format(INFO_SOTTOPAGINA_DI_ATTIVITA, att, naz, numVoci, naz, att);
        buffer.append(textService.setRef(message));
        buffer.append(" è una lista");
        buffer.append(textService.setRef(INFO_DIDASCALIE));
        buffer.append(textService.setRef(INFO_ORDINE));
        buffer.append(" di persone");
        buffer.append(textService.setRef(INFO_PERSONA_ATTIVITA));
        buffer.append(" presenti");
        buffer.append(textService.setRef(INFO_LISTA));
        buffer.append(" nell'enciclopedia che hanno come attività");
        buffer.append(textService.setRef(INFO_ATTIVITA_PREVISTE));
        buffer.append(String.format(" quella di '''%s'''", attivita.toLowerCase()));
        if (nazionalita.equals(TAG_LISTA_ALTRE)) {
            buffer.append(" e non usano il parametro ''nazionalità'' oppure hanno un'nazionalità di difficile elaborazione da parte del '''[[Utente:Biobot|<span style=\"color:green;\">bot</span>]]");
        }
        else {
            buffer.append(String.format(" e sono '''%s'''.", nazionalita.toLowerCase()));
        }
        buffer.append(textService.setRef(INFO_NAZIONALITA_PREVISTE));

        return buffer.toString();
    }

    //    @Deprecated
    //    protected String incipitAttNaz() {
    //        StringBuffer buffer = new StringBuffer();
    //        String message;
    //        String mod = "Bio/Plurale attività";
    //
    //        if (WPref.usaTreAttivita.is()) {
    //            buffer.append(String.format(" tra le %s", attNaz));
    //        }
    //        else {
    //            buffer.append(String.format(" come %s", attNaz));
    //        }
    //        message = String.format("Le %s sono quelle [[Discussioni progetto:Biografie/%s|'''convenzionalmente''' previste]] dalla " +
    //                "comunità ed [[Modulo:%s|inserite nell' '''elenco''']] utilizzato dal [[template:Bio|template Bio]]", attNaz, attNazUpper, mod);
    //        //--ref 5
    //        buffer.append(textService.setRef(message));
    //
    //        if (WPref.usaTreAttivita.is()) {
    //            message = LISTA_ATTIVITA_TRE;
    //        }
    //        else {
    //            buffer.append(" principale");
    //            message = LISTA_ATTIVITA_UNICA;
    //        }
    //        //--ref 6
    //        buffer.append(textService.setRef(message));
    //
    //        return buffer.toString();
    //    }

    public void uploadSottoPagine(String wikiTitle, String attNazPrincipale, String attNazSottoPagina, List<WrapLista> lista) {
        if (uploadTest) {
            appContext.getBean(UploadAttivita.class).test().uploadSottoPagina(wikiTitle, attNazPrincipale, attNazSottoPagina, lista);
        }
        else {
            appContext.getBean(UploadAttivita.class).uploadSottoPagina(wikiTitle, attNazPrincipale, attNazSottoPagina, lista);
        }
    }

    @Deprecated
    protected String sottoPaginaAttNaz() {
        StringBuffer buffer = new StringBuffer();
        String message;

        if (subAttivitaNazionalita.equals(TAG_ALTRE)) {
            subAttivitaNazionalita = "senza nazionalità";
            buffer.append(String.format(" e sono '''%s'''", textService.primaMinuscola(subAttivitaNazionalita)));
            message = "Qui vengono raggruppate quelle voci biografiche che '''non''' usano il parametro ''nazionalità'' oppure che usano una nazionalità di difficile elaborazione da parte del '''[[Utente:Biobot|<span style=\"color:green;\">bot</span>]]'''";
            buffer.append(textService.setRef(message));
        }
        else {
            buffer.append(String.format(" e sono '''%s'''", textService.primaMinuscola(subAttivitaNazionalita)));
        }

        return buffer.toString();
    }


    protected String correlate() {
        StringBuffer buffer = new StringBuffer();
        String cat = textService.primaMaiuscola(nomeLista);

        buffer.append(wikiUtility.setParagrafo("Voci correlate"));
        buffer.append(String.format("*[[:Categoria:%s]]", cat));
        buffer.append(CAPO);
        buffer.append("*[[Progetto:Biografie/Attività]]");

        return buffer.toString();
    }

    protected String categorie() {
        StringBuffer buffer = new StringBuffer();
        String cat = textService.primaMaiuscola(nomeLista);

        if (textService.isValid(nomeNazionalitaSottoPagina)) {
            cat += SLASH + nomeNazionalitaSottoPagina;
        }

        buffer.append(CAPO);
        buffer.append(String.format("*[[Categoria:Bio attività|%s]]", cat));

        return buffer.toString();
    }

    /**
     * Esegue la scrittura di tutte le pagine di nazionalità <br>
     */
    public WResult uploadAll() {
        WResult result = WResult.errato();
        long inizio = System.currentTimeMillis();

        List<String> listaPlurali = attivitaBackend.findAllPlurali();
        for (String plurale : listaPlurali) {
            upload(plurale);
        }

        fixUploadMinuti(inizio);
        return result;
    }

}
