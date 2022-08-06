package it.algos.wiki23.backend.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.liste.*;
import static it.algos.wiki23.backend.upload.UploadAttivita.*;
import it.algos.wiki23.backend.wrapper.*;
import static it.algos.wiki23.wiki.query.QueryWrite.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Tue, 14-Jun-2022
 * Time: 18:48
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UploadNazionalita extends UploadAttivitaNazionalita {

    public static final String UPLOAD_TITLE_PROJECT_NAZIONALITA = UPLOAD_TITLE_PROJECT + "Nazionalità/";

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
//        super.attNazUpper = "Nazionalità";
//        super.attNaz = "nazionalità";
//        super.attNazRevert = "attività";
//        super.attNazRevertUpper = "Attività";
        super.typeCrono = AETypeLista.nazionalitaPlurale;
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
        buffer.append(String.format(" per %s.", attNazRevert));
        buffer.append(textService.setRef(INFO_ATTIVITA_PREVISTE));
        buffer.append(textService.setRef(INFO_ALTRE_ATTIVITA));

        return buffer.toString();
    }

    protected String sottoPaginaAttNaz() {
        return String.format(" e sono '''%s'''", textService.primaMinuscola(subAttivitaNazionalita));
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

        buffer.append(CAPO);
        buffer.append(String.format("*[[Categoria:Bio nazionalità|%s]]", cat));

        return buffer.toString();
    }

//    /**
//     * Esegue la scrittura della pagina <br>
//     */
//    public void uploadTest(String nazionalitaPlurale) {
//        WResult result;
//        this.typeCrono = AETypeLista.nazionalitaPlurale;
//        this.nomeLista = nazionalitaPlurale;
//        this.uploadTest = true;
//        String wikiTitle = UPLOAD_TITLE_DEBUG + textService.primaMaiuscola(nazionalitaPlurale);
//        //        result = appContext.getBean(ListaNazionalita.class).plurale(nazionalitaPlurale).testoBody();
//        //        this.esegue(wikiTitle, result.getContent(), result.getIntValue());
//
//        Map<String, List<String>> mappa = appContext.getBean(ListaNazionalita.class).plurale(nazionalitaPlurale).mappaDidascalia();
//    }

//    /**
//     * Esegue la scrittura della sotto-pagina <br>
//     */
//    public void uploadSottoPagina(String wikiTitleParente, int numVoci, LinkedHashMap<String, List<String>> mappaSub) {
//        UploadNazionalita sottoPagina = appContext.getBean(UploadNazionalita.class);
//        sottoPagina.esegueSub(wikiTitleParente, nomeAttivitaNazionalitaPlurale, mappaSub);
//    }
//
//    /**
//     * Esegue la scrittura della sotto-sotto-pagina <br>
//     */
//    public void uploadSottoSottoPagina(String wikiTitleParente, List<String> listaSub) {
//        UploadNazionalita sottoSottoPagina = appContext.getBean(UploadNazionalita.class);
//        sottoSottoPagina.esegueSubSub(wikiTitleParente, nomeAttivitaNazionalitaPlurale, listaSub);
//    }

}
