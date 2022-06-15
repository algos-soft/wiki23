package it.algos.wiki23.backend.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki23.backend.enumeration.*;
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


    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(UploadAttivita.class, attivita) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     */
    public UploadAttivita() {
        super.attNazUpper="Attività";
        super.attNaz = "attività";
        super.attNazRevert = "nazionalità";
    }// end of constructor

    protected String incipitAttNaz() {
        StringBuffer buffer = new StringBuffer();
        String message;
        String mod = "Bio/Plurale attività";

        if (WPref.usaTreAttivita.is()) {
            buffer.append(String.format(" tra le %s", attNaz));
        }
        else {
            buffer.append(String.format(" come %s", attNaz));
        }
        message = String.format("Le %s sono quelle [[Discussioni progetto:Biografie/%s|'''convenzionalmente''' previste]] dalla " +
                "comunità ed [[Modulo:%s|inserite nell' '''elenco''']] utilizzato dal [[template:Bio|template Bio]]", attNaz, attNazUpper, mod);
        buffer.append(textService.setRef(message));

        if (WPref.usaTreAttivita.is()) {
            message = String.format("Ogni persona è presente in diverse [[Progetto:Biografie/%s|liste]], in base a quanto riportato in " +
                    "uno dei 3 parametri ''%s, %s2 e %s3'' del [[template:Bio|template Bio]] presente nella voce " +
                    "biografica specifica della persona", attNazUpper, attNaz, attNaz, attNaz);
        }
        else {
            buffer.append(" principale");
            message = String.format("Ogni persona è presente in una sola [[Progetto:Biografie/%s|lista]], in base a quanto riportato " +
                    "nel (primo) parametro ''%s'' del [[template:Bio|template Bio]] presente nella voce biografica specifica della " +
                    "persona",attNazUpper,attNaz);
        }
        buffer.append(textService.setRef(message));

        return buffer.toString();
    }

    /**
     * Esegue la scrittura della pagina <br>
     */
    public void upload() {
        appContext.getBean(ListaAttivita.class).plurale(nomeAttivitaNazionalitaPlurale).mappaDidascalie();
    }

    /**
     * Esegue la scrittura della pagina <br>
     */
    public void uploadTest(String nomeAttivitaNazionalitaPlurale) {
        this.nomeAttivitaNazionalitaPlurale = nomeAttivitaNazionalitaPlurale;
        mappaDidascalie = appContext.getBean(ListaAttivita.class).plurale(nomeAttivitaNazionalitaPlurale).mappaDidascalie();
        super.esegue(WIKI_TITLE_DEBUG, mappaDidascalie);
    }

    /**
     * Esegue la scrittura della sotto-pagina <br>
     */
    public void uploadSottoPagina(String wikiTitlePaginaPrincipale, String nomeAttivitaNazionalitaPlurale) {
        mappaDidascalie = appContext.getBean(ListaAttivita.class).plurale(nomeAttivitaNazionalitaPlurale).mappaDidascalie();
        //        newText = mappaToText(WIKI_TITLE_DEBUG,mappaDidascalie);
        //        appContext.getBean(QueryWrite.class).urlRequest(WIKI_TITLE_DEBUG, newText);
    }

}
