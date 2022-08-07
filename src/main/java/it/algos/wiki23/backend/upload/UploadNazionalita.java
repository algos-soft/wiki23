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
 * Usata fondamentalmente da NazionalitàWikiView con appContext.getBean(UploadNazionalita.class).upload(nomeGiorno).upload(nomeNazionalitaPlurale) <br>
 * <p>
 * Necessita del login come bot <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UploadNazionalita extends UploadAttivitaNazionalita {

    public static final String UPLOAD_TITLE_PROJECT_NAZIONALITA = UPLOAD_TITLE_PROJECT + "Nazionalità/";

    private String nomeAttivitaSottoPagina;

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
     * Uso: appContext.getBean(UploadAttivita.class, attivita) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     */
    public UploadNazionalita() {
        super.titoloLinkParagrafo = TITOLO_LINK_PARAGRAFO_ATTIVITA;
        super.titoloLinkVediAnche = TITOLO_LINK_PARAGRAFO_NAZIONALITA;
        super.typeCrono = AETypeLista.nazionalitaPlurale;
        super.lastUpload = WPref.uploadNazionalita;
        super.durataUpload = WPref.uploadNazionalitaTime;
        super.nextUpload = WPref.uploadNazionalitaPrevisto;
        super.usaParagrafi = WPref.usaParagrafiGiorni.is();
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
        buffer.append(textService.setRef(INFO_PERSONA));
        buffer.append(" presenti");
        buffer.append(textService.setRef(INFO_LISTA));
        buffer.append(" nell'enciclopedia che hanno come nazionalità");
        buffer.append(textService.setRef(INFO_NAZIONALITA_PREVISTE));
        buffer.append(String.format(" quella di '''%s'''.", nomeLista));
        buffer.append(" Le persone sono suddivise");
        buffer.append(textService.setRef(INFO_PARAGRAFI_ATTIVITA));
        buffer.append(" per attività.");
        buffer.append(textService.setRef(INFO_ATTIVITA_PREVISTE));
        buffer.append(textService.setRef(INFO_ALTRE_ATTIVITA));

        return buffer.toString();
    }


    protected String incipitSottoPagina(String nazionalita, String attivita) {
        StringBuffer buffer = new StringBuffer();
        this.nomeLista = nazionalita;
        this.nomeAttivitaSottoPagina = attivita;

        buffer.append("Questa");
        buffer.append(textService.setRef(INFO_SOTTOPAGINA_ATTIVITA));
        buffer.append(" è una lista");
        buffer.append(textService.setRef(INFO_DIDASCALIE));
        buffer.append(textService.setRef(INFO_ORDINE));
        buffer.append(" di persone");
        buffer.append(textService.setRef(INFO_PERSONA));
        buffer.append(" presenti");
        buffer.append(textService.setRef(INFO_LISTA));
        buffer.append(" nell'enciclopedia che hanno come nazionalità");
        buffer.append(textService.setRef(INFO_NAZIONALITA_PREVISTE));
        buffer.append(String.format(" quella di '''%s'''", nazionalita.toLowerCase()));
        if (attivita.equals(TAG_LISTA_ALTRE)) {
            buffer.append(" e non usano il parametro ''attività'' oppure hanno un'attività di difficile elaborazione da parte del '''[[Utente:Biobot|<span style=\"color:green;\">bot</span>]]");
        }
        else {
            buffer.append(String.format(" e sono '''%s'''.", attivita.toLowerCase()));
        }
        buffer.append(textService.setRef(INFO_ATTIVITA_PREVISTE));

        return buffer.toString();
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
        StringBuffer buffer = new StringBuffer();
        String cat = textService.primaMaiuscola(nomeLista);

        if (typeUpload == AETypeUpload.sottoPagina) {
            cat += SLASH + nomeAttivitaSottoPagina;
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
