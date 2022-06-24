package it.algos.wiki23.backend.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki23.backend.liste.*;
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

    protected String sottoPaginaAttNaz() {
        return String.format(" e sono '''%s'''", textService.primaMinuscola(subAttivitaNazionalita));
    }
    /**
     * Esegue la scrittura della pagina <br>
     */
    public void upload() {
        appContext.getBean(ListaNazionalita.class).plurale(nomeAttivitaNazionalitaPlurale).mappaDidascalie();
    }


    /**
     * Esegue la scrittura della pagina <br>
     */
    public void uploadTest(String nomeAttivitaNazionalitaPlurale) {
        String wikiTitle = UPLOAD_TITLE_DEBUG + textService.primaMaiuscola(nomeAttivitaNazionalitaPlurale);
        this.nomeAttivitaNazionalitaPlurale = nomeAttivitaNazionalitaPlurale;
        mappaDidascalie = appContext.getBean(ListaNazionalita.class).plurale(nomeAttivitaNazionalitaPlurale).mappaDidascalie();
        super.esegue(wikiTitle, mappaDidascalie);
    }

    /**
     * Esegue la scrittura della sotto-pagina <br>
     */
    public void uploadSottoPagina(String wikiTitleParente, int numVoci, LinkedHashMap<String, List<String>> mappaSub) {
        UploadNazionalita sottoPagina = appContext.getBean(UploadNazionalita.class);
        sottoPagina.esegueSub(wikiTitleParente, nomeAttivitaNazionalitaPlurale, mappaSub);
    }

}
