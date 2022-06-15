package it.algos.wiki23.backend.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki23.backend.liste.*;
import static it.algos.wiki23.wiki.query.QueryWrite.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

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
    }// end of constructor

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
        this.nomeAttivitaNazionalitaPlurale = nomeAttivitaNazionalitaPlurale;
        mappaDidascalie = appContext.getBean(ListaNazionalita.class).plurale(nomeAttivitaNazionalitaPlurale).mappaDidascalie();
        super.esegue(WIKI_TITLE_DEBUG, mappaDidascalie);
    }

}
