package it.algos.wiki23.backend.statistiche;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.service.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.attivita.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.service.*;
import it.algos.wiki23.backend.wrapper.*;
import it.algos.wiki23.wiki.query.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import javax.annotation.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Fri, 01-Jul-2022
 * Time: 10:34
 */
public abstract class Statistiche {

    /**
     * Istanza di una interfaccia <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ApplicationContext appContext;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public WikiUtility wikiUtility;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public LogService logger;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public TextService textService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public BioBackend bioBackend;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AttivitaBackend attivitaBackend;

    protected AETypeToc typeToc;

    protected List lista;

    protected LinkedHashMap<String, MappaStatistiche> mappa;

    /**
     * Performing the initialization in a constructor is not suggested as the state of the UI is not properly set up when the constructor is invoked. <br>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     * Se viene implementata una sottoclasse, passa di qui per ogni sottoclasse oltre che per questa istanza <br>
     * Se esistono delle sottoclassi, passa di qui per ognuna di esse (oltre a questa classe madre) <br>
     */
    @PostConstruct
    private void postConstruct() {
        this.fixPreferenze();
        this.creaLista();
        this.creaMappa();
    }

    /**
     * Preferenze usate da questa 'view' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Puo essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        this.typeToc = AETypeToc.forceToc;
    }

    protected WResult upload(String wikiTitle) {
        StringBuffer buffer = new StringBuffer();

        buffer.append(avviso());
        buffer.append(noToc());
        buffer.append(tmpStatBio());
        buffer.append(body());
        buffer.append(note());
        buffer.append(correlate());
        buffer.append(categorie());

        return registra(wikiTitle, buffer.toString());
    }

    protected String avviso() {
        return "<!-- NON MODIFICATE DIRETTAMENTE QUESTA PAGINA - GRAZIE -->";
    }

    protected String noToc() {
        return typeToc.get();
    }

    protected String tmpStatBio() {
        StringBuffer buffer = new StringBuffer();
        String data = LocalDate.now().format(DateTimeFormatter.ofPattern("d MMM yyy")); ;

        buffer.append(CAPO);
        buffer.append(String.format("{{StatBio|data=%s}}", data));

        return buffer.toString();
    }

    protected String body() {
        return CAPO;
    }

    protected String note() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(wikiUtility.setParagrafo("Note"));
        buffer.append("<references/>");
        buffer.append(CAPO);

        return buffer.toString();
    }

    protected String correlate() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(CAPO);
        buffer.append("{{BioCorrelate}}");
        buffer.append(CAPO);

        return buffer.toString();
    }

    protected String categorie() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(CAPO);
        buffer.append("[[Categoria:Progetto Biografie|{{PAGENAME}}]]");
        buffer.append(CAPO);

        return buffer.toString();
    }


    protected String inizioTabella() {
        String testo = VUOTA;

        testo += CAPO;
        testo += "{|class=\"wikitable sortable\" style=\"background-color:#EFEFEF; text-align: right;\"";
        testo += CAPO;

        return testo;
    }

    protected String fineTabella() {
        String testo = VUOTA;

        testo += "|}";
        testo += CAPO;

        return testo;
    }

    /**
     * Recupera la lista
     */
    protected void creaLista() {
    }


    /**
     * Costruisce la mappa <br>
     */
    protected void creaMappa() {
        mappa = new LinkedHashMap<>();
    }

    protected WResult registra(String wikiTitle, String newText) {
        if (Pref.debug.is()) {
            wikiTitle = WIKI_TITLE_DEBUG;
        }
        return appContext.getBean(QueryWrite.class).urlRequest(wikiTitle, newText);
    }

}
