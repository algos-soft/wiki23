package it.algos.wiki23.backend.upload;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.service.*;
import it.algos.vaad23.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.liste.*;
import it.algos.wiki23.backend.packages.anno.*;
import it.algos.wiki23.backend.packages.attivita.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.packages.giorno.*;
import it.algos.wiki23.backend.service.*;
import it.algos.wiki23.backend.wrapper.*;
import it.algos.wiki23.wiki.query.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;

import java.time.*;
import java.time.format.*;
import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Wed, 08-Jun-2022
 * Time: 06:54
 * <p>
 * Classe specializzata per caricare (upload) le liste biografiche sul server wiki. <br>
 * <p>
 * Liste cronologiche (in namespace principale) di nati e morti nel giorno o nell'anno <br>
 * Liste di nomi e cognomi (in namespace principale). <br>
 * Liste di attività e nazionalità (in Progetto:Biografie). <br>
 * <p>
 * Necessita del login come bot <br>
 * Sovrascritta nelle sottoclassi concrete <br>
 */
public abstract class Upload {

    protected static final String INFO_PAGINA_ATTIVITA = "Questa pagina di una singola '''attività''' viene creata solo se le relative voci biografiche superano le '''" + WPref.sogliaAttNazWiki.getInt() + "''' unità.";

    protected static final String INFO_PAGINA_NAZIONALITA = "Questa pagina di una singola '''nazionalità''' viene creata solo se le relative voci biografiche superano le '''" + WPref.sogliaAttNazWiki.getInt() + "''' unità.";

    protected static final String INFO_PARAGRAFI_ATTIVITA = "La lista è suddivisa in paragrafi per ogni '''attività''' individuata. Se il numero di voci biografiche nel" +
            " paragrafo supera le '''" + WPref.sogliaSottoPagina.getInt() + "''' unità, viene creata una '''sottopagina'''.";

    protected static final String INFO_PARAGRAFI_NAZIONALITA = "La lista è suddivisa in paragrafi per ogni '''nazionalità''' individuata. Se il numero di voci biografiche nel" +
            " paragrafo supera le '''" + WPref.sogliaSottoPagina.getInt() + "''' unità, viene creata una '''sottopagina'''.";

    protected static final String TITOLO_LINK_PARAGRAFO_ATTIVITA = UPLOAD_TITLE_PROJECT + ATT;

    protected static final String TITOLO_LINK_PARAGRAFO_NAZIONALITA = UPLOAD_TITLE_PROJECT + NAZ;

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
    public TextService textService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public DateService dateService;

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
    public LogService logger;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AttivitaBackend attivitaBackend;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public GiornoWikiBackend giornoWikiBackend;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AnnoWikiBackend annoWikiBackend;

    protected AETypeLista typeCrono;

    protected Lista lista;

    /**
     * Mappa delle didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) che corrisponde al titolo del paragrafo <br>
     * Ogni valore della mappa è costituito da una lista di didascalie per ogni paragrafo <br>
     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
     * La mappa viene creata nel @PostConstruct dell'istanza <br>
     */
    protected LinkedHashMap<String, List<WrapLista>> mappaWrap;

    protected String titoloLinkParagrafo;

    protected String titoloLinkVediAnche;

    protected String nomeLista;

    protected String attNazUpper;

    protected String attNaz;

    protected String attNazRevert;

    protected String attNazRevertUpper;

    //    protected String nomeAttivitaNazionalitaPlurale;

    protected String subAttivitaNazionalita;

    protected boolean uploadTest = false;

    protected String summary;

    protected WPref lastUpload;

    protected WPref durataUpload;

    protected WPref nextUpload;

    protected boolean usaParagrafi;

    protected String wikiTitle;

    protected AETypeToc typeToc;

    protected WResult esegueUpload(String wikiTitle, LinkedHashMap<String, List<WrapLista>> mappa) {
        return null;
    }


    protected String avviso() {
        return "<!-- NON MODIFICATE DIRETTAMENTE QUESTA PAGINA - GRAZIE -->";
    }


    protected String includeIni() {
        return "<noinclude>";
    }

    protected String fixToc() {
        return typeToc.get();
    }

    protected String torna(String wikiTitle) {
        wikiTitle = textService.levaCodaDa(wikiTitle, SLASH);
        return textService.isValid(wikiTitle) ? String.format("{{Torna a|%s}}", wikiTitle) : VUOTA;
    }

    protected String tmpListaBio(int numVoci) {
        String data = LocalDate.now().format(DateTimeFormatter.ofPattern("d MMM yyy")); ;
        String progetto = "biografie";
        String txtVoci = textService.format(numVoci);

        return String.format("{{ListaBio|bio=%s|data=%s|progetto=%s}}", txtVoci, data, progetto);
    }

    protected String tmpListaStat() {
        String data = LocalDate.now().format(DateTimeFormatter.ofPattern("d MMM yyy")); ;
        return String.format("{{StatBio|data=%s}}", data);
    }

    protected String includeEnd() {
        return "</noinclude>";
    }

    protected String incipit() {
        return VUOTA;
    }


    protected String note() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(CAPO);
        buffer.append(wikiUtility.setParagrafo("Note"));
        buffer.append("<references/>");

        return buffer.toString();
    }

    protected String correlate() {
        return VUOTA;
    }

    protected String portale() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(CAPO);
        buffer.append("{{Portale|biografie}}");

        return buffer.toString();
    }


    protected String categorie() {
        return VUOTA;
    }


    protected WResult registra(String wikiTitle, String newText) {
        String newTextSignificativo = newText.substring(newText.indexOf("</noinclude>"));
        return appContext.getBean(QueryWrite.class).urlRequestCheck(wikiTitle, newText, newTextSignificativo, summary);
        //                return appContext.getBean(QueryWrite.class).urlRequest(wikiTitle, newText, summary);
    }


    public void fixUploadMinuti(final long inizio) {
        long fine = System.currentTimeMillis();
        String message;
        Long delta = fine - inizio;

        if (lastUpload != null) {
            lastUpload.setValue(LocalDateTime.now());
        }
        else {
            logger.warn(new WrapLog().exception(new AlgosException("lastUpload è nullo")));
            return;
        }

        if (durataUpload != null) {
            delta = delta / 1000 / 60;
            durataUpload.setValue(delta.intValue());
        }
        else {
            logger.warn(new WrapLog().exception(new AlgosException("durataUpload è nullo")));
            return;
        }
    }
}
