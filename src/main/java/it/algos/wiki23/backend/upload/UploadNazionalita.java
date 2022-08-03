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
public class UploadNazionalita extends Upload {

    public static final String UPLOAD_TITLE_PROJECT_NAZIONALITA = UPLOAD_TITLE_PROJECT + "Nazionalità/";

    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(UploadAttivita.class, attivita) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     */
    public UploadNazionalita() {
        super.titoloLinkParagrafo = "Progetto:Biografie/Attività/";
        super.attNazUpper = "Nazionalità";
        super.attNaz = "nazionalità";
        super.attNazRevert = "attività";
        super.attNazRevertUpper = "Attività";
    }// end of constructor


    protected WResult esegue(String wikiTitle, String testoBody, int numVoci) {
        StringBuffer buffer = new StringBuffer();

        buffer.append(avviso());
        buffer.append(CAPO);
        buffer.append(includeIni());
        buffer.append(fixToc());
        buffer.append(tmpListaBio(numVoci));
        buffer.append(includeEnd());
        buffer.append(CAPO);
        buffer.append(incipit());
        buffer.append(CAPO);
        buffer.append(testoBody);
        buffer.append(uploadTest ? VUOTA : DOPPIE_GRAFFE_END);
        buffer.append(note());
        buffer.append(CAPO);
        buffer.append(correlate());
        buffer.append(CAPO);
        buffer.append(portale());
        buffer.append(categorie());

        return registra(wikiTitle, buffer.toString().trim());
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

    /**
     * Esegue la scrittura della pagina <br>
     */
    public WResult upload(String nomeAttivitaNazionalitaPlurale) {
        this.nomeAttivitaNazionalitaPlurale = nomeAttivitaNazionalitaPlurale;
        String wikiTitle = UPLOAD_TITLE_PROJECT_NAZIONALITA + textService.primaMaiuscola(nomeAttivitaNazionalitaPlurale);
        mappaDidascalie = appContext.getBean(ListaNazionalita.class).plurale(nomeAttivitaNazionalitaPlurale).mappaDidascalie();
        return super.esegue(wikiTitle, mappaDidascalie);
    }


    /**
     * Esegue la scrittura della pagina <br>
     */
    public void uploadTest(String nazionalitaPlurale) {
        WResult result;
        this.typeCrono = AETypeLista.nazionalitaPlurale;
        this.nomeLista = nazionalitaPlurale;
        this.uploadTest = true;
        String wikiTitle = UPLOAD_TITLE_DEBUG + textService.primaMaiuscola(nazionalitaPlurale);
        result = appContext.getBean(ListaNazionalita.class).plurale(nazionalitaPlurale).testoBody();
        this.esegue(wikiTitle, result.getContent(), result.getIntValue());
    }

    /**
     * Esegue la scrittura della sotto-pagina <br>
     */
    public void uploadSottoPagina(String wikiTitleParente, int numVoci, LinkedHashMap<String, List<String>> mappaSub) {
        UploadNazionalita sottoPagina = appContext.getBean(UploadNazionalita.class);
        sottoPagina.esegueSub(wikiTitleParente, nomeAttivitaNazionalitaPlurale, mappaSub);
    }

    /**
     * Esegue la scrittura della sotto-sotto-pagina <br>
     */
    public void uploadSottoSottoPagina(String wikiTitleParente, List<String> listaSub) {
        UploadNazionalita sottoSottoPagina = appContext.getBean(UploadNazionalita.class);
        sottoSottoPagina.esegueSubSub(wikiTitleParente, nomeAttivitaNazionalitaPlurale, listaSub);
    }

}
