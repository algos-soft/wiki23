package it.algos.wiki23.backend.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki23.backend.liste.*;
import it.algos.wiki23.backend.packages.attivita.*;
import it.algos.wiki23.wiki.query.*;
import static it.algos.wiki23.wiki.query.QueryWrite.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Wed, 08-Jun-2022
 * Time: 06:55
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UploadAttivita extends Upload {

    protected String nomeAttivitaPlurale;

    protected Lista lista;

    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(UploadAttivita.class, attivita) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     *
     * @param nomeAttivitaPlurale in funzione del flag
     */
    public UploadAttivita(final String nomeAttivitaPlurale) {
        this.nomeAttivitaPlurale = nomeAttivitaPlurale;
    }// end of constructor

    /**
     * Esegue la scrittura della pagina <br>
     */
    public void upload() {
        appContext.getBean(ListaAttivita.class).plurale(nomeAttivitaPlurale).mappaDidascalie();
    }

    /**
     * Esegue la scrittura della pagina <br>
     */
    public void uploadTest() {
        mappaDidascalie = appContext.getBean(ListaAttivita.class).plurale(nomeAttivitaPlurale).mappaParagrafiDimensionati();
        newText = mappaToText(mappaDidascalie);
        appContext.getBean(QueryWrite.class).urlRequest(WIKI_TITLE_DEBUG, newText);
    }

}
