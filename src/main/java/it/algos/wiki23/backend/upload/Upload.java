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

    protected static final String INFO_DIDASCALIE = "Le didascalie delle voci sono quelle previste nel [[Progetto:Biografie/Didascalie|progetto biografie]]";

    protected static final String INFO_ORDINE = "Le voci, all'interno di ogni paragrafo, sono in ordine alfabetico per '''forzaOrdinamento''' oppure per '''cognome''' oppure per '''titolo''' della pagina su wikipedia.";

    protected static final String INFO_LISTA = "La lista non è esaustiva e contiene '''solo''' le persone che sono citate nell'enciclopedia e per le quali è stato implementato correttamente il '''[[template:Bio|template Bio]]'''";

    protected static final String INFO_PAGINA_NAZIONALITA = "La pagina di una singola '''nazionalità''' viene creata solo se le relative voci biografiche superano le '''" + WPref.sogliaAttNazWiki.getInt() + "''' unità.";

    protected static final String INFO_PARAGRAFI_ATTIVITA = "La lista è suddivisa in paragrafi per ogni '''attività''' individuata. Se il numero di voci biografiche nel" +
            " paragrafo supera le '''" + WPref.sogliaSottoPagina.getInt() + "''' unità, viene creata una '''sottopagina'''.";

    protected static final String INFO_ATTIVITA_PREVISTE = "Le '''attività''' sono quelle [[Discussioni progetto:Biografie/Attività|'''convenzionalmente''' previste]] dalla comunità ed [[Modulo:Bio/Plurale attività|inserite nell' '''elenco''']] utilizzato dal [[template:Bio|template Bio]]";

    protected static final String INFO_NAZIONALITA_PREVISTE = "Le '''nazionalità''' sono quelle [[Discussioni progetto:Biografie/Nazionalità|'''convenzionalmente''' previste]] dalla comunità ed [[Modulo:Bio/Plurale nazionalità|inserite nell' '''elenco''']] utilizzato dal [[template:Bio|template Bio]]";

    protected static final String INFO_PERSONA = "Ogni persona è presente in una sola [[Discussioni progetto:Biografie/Nazionalità|lista]], in base a quanto riportato nel parametro ''nazionalità'' utilizzato dal [[template:Bio|template Bio]]";

    protected static final String INFO_ALTRE_ATTIVITA = "Nel paragrafo Altre... (eventuale) vengono raggruppate quelle voci biografiche che '''non''' usano il " +
            "parametro ''attività'' oppure che usano una attività di difficile elaborazione da parte del '''[[Utente:Biobot|<span style=\"color:green;\">bot</span>]]'''";

    protected static final String INFO_SOTTOPAGINA_ATTIVITA = "La sottopagina di una singola '''attività''' viene creata solo se il numero di voci biografiche supera le '''" + WPref.sogliaSottoPagina.getInt() + "''' unità.";


    public static final String UPLOAD_TITLE_DEBUG = "Utente:Biobot/";

    public static final String UPLOAD_TITLE_PROJECT = "Progetto:Biografie/";

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
    protected TreeMap<String, TreeMap<String, List<String>>> mappa;

    protected LinkedHashMap<String, List<WrapLista>> mappaWrap;

    protected LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaDidascalie;

    protected String titoloLinkParagrafo;

    protected String titoloLinkVediAnche;

    protected String nomeLista;

    protected String attNazUpper;

    protected String attNaz;

    protected String attNazRevert;

    protected String attNazRevertUpper;

    protected String nomeAttivitaNazionalitaPlurale;

    protected String subAttivitaNazionalita;

    protected boolean sottoPagina = false;

    protected boolean uploadTest = false;

    protected String summary;

    protected WPref lastUpload;

    protected WPref durataUpload;

    protected WPref nextUpload;

    protected boolean usaParagrafi;

    protected String wikiTitle;

    protected AETypeUpload typeUpload;


    protected WResult esegueUpload(String wikiTitle, LinkedHashMap<String, List<WrapLista>> mappa) {
        return null;
    }

    //    protected WResult esegueMappa(String wikiTitle, Map<String, List<String>> mappa) {
    //        StringBuffer buffer = new StringBuffer();
    //        int numVoci = wikiUtility.getSizeAll(mappa);
    //
    //        buffer.append(avviso());
    //        buffer.append(CAPO);
    //        buffer.append(includeIni());
    //        buffer.append(fixToc());
    //        buffer.append(tmpListaBio(numVoci));
    //        buffer.append(includeEnd());
    //        buffer.append(CAPO);
    //        buffer.append(incipit());
    //        buffer.append(CAPO);
    //        buffer.append(testoBody(mappa));
    //        buffer.append(uploadTest ? VUOTA : DOPPIE_GRAFFE_END);
    //        buffer.append(note());
    //        buffer.append(CAPO);
    //        buffer.append(correlate());
    //        buffer.append(CAPO);
    //        buffer.append(portale());
    //        buffer.append(categorie());
    //
    //        return registra(wikiTitle, buffer.toString().trim());
    //    }

    /**
     * Testo del body di upload con paragrafi e righe <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public String testoBody(Map<String, List<WrapLista>> mappa) {
        StringBuffer buffer = new StringBuffer();
        List<WrapLista> lista;
        int max = WPref.sogliaSottoPagina.getInt();
        int numVoci;
        String nomeListaUpper = textService.primaMaiuscola(nomeLista);
        String keyParagrafoUpper;
        String parente;
        boolean usaDiv;

        for (String keyParagrafo : mappa.keySet()) {
            lista = mappa.get(keyParagrafo);
            numVoci = lista.size();
            keyParagrafoUpper = textService.primaMaiuscola(keyParagrafo);
            buffer.append(wikiUtility.fixTitolo(titoloLinkParagrafo, keyParagrafo, numVoci));

            if (numVoci > max && !keyParagrafo.equals(TAG_LISTA_ALTRE)) {
                parente = String.format("%s%s%s%s%s", titoloLinkVediAnche, SLASH, nomeListaUpper, SLASH, keyParagrafoUpper);
                String vedi = String.format("{{Vedi anche|%s}}", parente);
                buffer.append(vedi + CAPO);
                //                this.uploadSottoPagina(parente, numVoci, mappaSub);
            }
            else {
                usaDiv = lista.size() > 3;
                buffer.append(usaDiv ? "{{Div col}}" + CAPO : VUOTA);
                //                for (String didascalia : lista) {
                //                    buffer.append(ASTERISCO + didascalia);
                //                    buffer.append(CAPO);
                //                }
                buffer.append(usaDiv ? "{{Div col end}}" + CAPO : VUOTA);
            }

        }

        return buffer.toString().trim();
    }

    protected WResult esegue(String wikiTitle, String testoBody, int numVoci) {
        StringBuffer buffer = new StringBuffer();

        buffer.append(avviso());
        buffer.append(CAPO);
        buffer.append(includeIni());
        buffer.append(fixToc());
        buffer.append(torna(wikiTitle));
        buffer.append(tmpListaBio(numVoci));
        buffer.append(includeEnd());
        buffer.append(CAPO);
        //        buffer.append(tmpListaPersoneIni(numVoci));
        buffer.append(testoBody);
        buffer.append(uploadTest ? VUOTA : DOPPIE_GRAFFE_END);
        buffer.append(includeIni());
        buffer.append(note());
        buffer.append(CAPO);
        buffer.append(correlate());
        buffer.append(CAPO);
        buffer.append(portale());
        buffer.append(categorie());
        buffer.append(includeEnd());

        return registra(wikiTitle, buffer.toString().trim());
    }

    @Deprecated
    protected WResult esegueOld(String wikiTitle, LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaDidascalie) {
        StringBuffer buffer = new StringBuffer();
        int numVoci = wikiUtility.getSizeAll(mappaDidascalie);

        buffer.append(avviso());
        buffer.append(CAPO);
        buffer.append(includeIni());
        buffer.append(fixToc());
        buffer.append(torna(VUOTA));
        buffer.append(tmpListaBio(numVoci));
        buffer.append(includeEnd());
        buffer.append(CAPO);
        buffer.append(incipit());
        buffer.append(CAPO);
        buffer.append(body(wikiTitle, mappaDidascalie));
        buffer.append(note());
        buffer.append(CAPO);
        buffer.append(correlate());
        buffer.append(CAPO);
        buffer.append(portale());
        buffer.append(categorie());

        return registra(wikiTitle, buffer.toString());
    }


    protected WResult esegueSub(String wikiTitle, String nomeAttivitaNazionalitaPlurale, LinkedHashMap<String, List<String>> mappaSub) {
        StringBuffer buffer = new StringBuffer();
        int numVoci = wikiUtility.getSize(mappaSub);
        this.sottoPagina = true;
        this.nomeAttivitaNazionalitaPlurale = nomeAttivitaNazionalitaPlurale;
        this.subAttivitaNazionalita = wikiTitle.substring(wikiTitle.lastIndexOf(SLASH) + SLASH.length());

        buffer.append(avviso());
        buffer.append(CAPO);
        buffer.append(includeIni());
        buffer.append(fixToc());
        buffer.append(torna(wikiTitle));
        buffer.append(tmpListaBio(numVoci));
        buffer.append(includeEnd());
        buffer.append(CAPO);
        buffer.append(incipit());
        buffer.append(CAPO);
        buffer.append(bodySub(wikiTitle, mappaSub));
        buffer.append(note());
        buffer.append(CAPO);
        buffer.append(correlate());
        buffer.append(CAPO);
        buffer.append(portale());
        buffer.append(categorie());

        return registra(wikiTitle, buffer.toString());
    }


    protected WResult esegueSubSub(String wikiTitle, String nomeAttivitaNazionalitaPlurale, List<String> listaSub) {
        StringBuffer buffer = new StringBuffer();
        int numVoci = listaSub.size();
        this.sottoPagina = true;
        this.nomeAttivitaNazionalitaPlurale = nomeAttivitaNazionalitaPlurale;
        this.subAttivitaNazionalita = wikiTitle.substring(wikiTitle.lastIndexOf(SLASH) + SLASH.length());

        buffer.append(avviso());
        buffer.append(CAPO);
        buffer.append(includeIni());
        buffer.append(fixToc());
        buffer.append(torna(wikiTitle));
        buffer.append(tmpListaBio(numVoci));
        buffer.append(includeEnd());
        buffer.append(CAPO);
        buffer.append(bodySubSub(wikiTitle, listaSub));
        buffer.append(correlate());
        buffer.append(CAPO);
        buffer.append(portale());
        buffer.append(categorie());

        return registra(wikiTitle, buffer.toString());
    }

    protected String avviso() {
        return "<!-- NON MODIFICATE DIRETTAMENTE QUESTA PAGINA - GRAZIE -->";
    }


    protected String includeIni() {
        return "<noinclude>";
    }

    protected String fixToc() {
        return ((AETypeToc) WPref.typeTocAttNaz.getEnumCurrentObj()).get();
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
        StringBuffer buffer = new StringBuffer();
        String message;
        int maxPagina = WPref.sogliaAttNazWiki.getInt();
        int maxSottoPagina = WPref.sogliaSottoPagina.getInt();
        String mod = "Bio/Plurale attività";
        //        String pack = nomeAttivitaNazionalitaPlurale;

        buffer.append("Questa è una lista");
        message = "Le didascalie delle voci sono quelle previste nel [[Progetto:Biografie/Didascalie|progetto biografie]]";
        //--ref 1
        buffer.append(textService.setRef(message));

        message = "Le voci, all'interno di ogni paragrafo, sono in ordine alfabetico per '''cognome'''; se questo manca si utilizza il '''titolo''' della pagina.";
        //--ref 2
        buffer.append(textService.setRef(message));

        buffer.append(" di persone");
        message = "La lista non è esaustiva e contiene solo le persone che sono citate nell'enciclopedia e per le quali è stato " +
                "implementato correttamente il [[template:Bio|template Bio]]";
        //--ref 3
        buffer.append(textService.setRef(message));

        buffer.append(" presenti");
        message = String.format("La pagina di una singola %s viene creata se le relative voci biografiche superano le '''%d''' unità.", attNaz, maxPagina);
        //--ref 4
        buffer.append(textService.setRef(message));

        buffer.append(" nell'enciclopedia che hanno");
        buffer.append(incipitAttNaz());
        message = String.format(" quella di '''%s'''", nomeLista);
        buffer.append(message);
        if (sottoPagina) {
            buffer.append(sottoPaginaAttNaz());
            message = String.format("Le %s sono quelle [[Discussioni progetto:Biografie/%s|'''convenzionalmente''' previste]] dalla " +
                            "comunità ed [[Modulo:Bio/Plurale %s|inserite nell' '''elenco''']] utilizzato dal [[template:Bio|template Bio]]",
                    attNazRevert, attNazRevertUpper, attNazRevert
            );
            //--ref 6/7/8
            buffer.append(textService.setRef(message));
        }
        buffer.append(PUNTO);
        if (!sottoPagina) {
            buffer.append(" Le persone sono suddivise");
            message = String.format("La lista è suddivisa in paragrafi per ogni %s individuata. Se il numero di voci biografiche nel" +
                    " paragrafo supera le '''%s''' unità, viene creata una sottopagina.", attNazRevert, maxSottoPagina);
            //--ref 5/7
            buffer.append(textService.setRef(message));

            buffer.append(String.format(" per %s.", attNazRevert));
            message = String.format("Le %s sono quelle [[Discussioni progetto:Biografie/%s|'''convenzionalmente''' previste]] dalla " +
                            "comunità ed [[Modulo:Bio/Plurale %s|inserite nell' '''elenco''']] utilizzato dal [[template:Bio|template Bio]]",
                    attNazRevert, attNazRevertUpper, attNazRevert
            );
            //--ref 6/8
            buffer.append(textService.setRef(message));

            message = String.format("Nel paragrafo Altre... (eventuale) vengono raggruppate quelle voci biografiche che '''non''' usano il " +
                    "parametro ''%s'' oppure che usano una %s di difficile elaborazione da parte del '''[[Utente:Biobot|<span " +
                    "style=\"color:green;\">bot</span>]]'''", attNazRevert, attNazRevert);
            //--ref 9
            buffer.append(textService.setRef(message));
        }

        return buffer.toString();
    }

    protected String incipitAttNaz() {
        return String.format(" come %s", attNaz);
    }

    protected String sottoPaginaAttNaz() {
        return VUOTA;
    }

    protected String body(String wikiTitle, LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaDidascalie) {
        return mappaToText(wikiTitle, mappaDidascalie);
    }


    protected String bodySub(String wikiTitle, LinkedHashMap<String, List<String>> mappaSub) {
        return mappaToTextSub(wikiTitle, mappaSub);
    }

    protected String bodySubSub(String wikiTitle, List<String> listaSub) {
        return mappaToTextSubSub(wikiTitle, listaSub);
    }

    protected String note() {
        StringBuffer buffer = new StringBuffer();

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
        //        return appContext.getBean(QueryWrite.class).urlRequestCheck(wikiTitle, newText, newTextSignificativo, summary);
        return appContext.getBean(QueryWrite.class).urlRequest(wikiTitle, newText, summary);
        //        return null;
    }

    protected String mappaToText(String wikiTitle, LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaTxt) {
        StringBuffer buffer = new StringBuffer();
        LinkedHashMap<String, List<String>> mappaSub;
        int numVoci;
        String testoTemp;

        if (mappaTxt != null) {
            for (String key : mappaTxt.keySet()) {
                mappaSub = mappaTxt.get(key);
                numVoci = wikiUtility.getSize(mappaSub);

                buffer.append(fixTitoloParagrafo(key, numVoci));
                testoTemp = fixCorpoParagrafo(wikiTitle, key, numVoci, mappaSub);
                if (Pref.usaNonBreaking.is()) {
                    testoTemp = testoTemp.replaceAll(SPAZIO, Pref.nonBreaking.getStr());
                }
                buffer.append(testoTemp);
            }
        }

        return buffer.toString().trim();
    }

    protected String mappaToTextSub(String wikiTitle, LinkedHashMap<String, List<String>> mappaSub) {
        StringBuffer buffer = new StringBuffer();
        int numVoci;

        if (mappaSub != null) {
            for (String key : mappaSub.keySet()) {
                numVoci = mappaSub.get(key).size();

                buffer.append(fixTitoloParagrafoSub(key, numVoci));
                buffer.append(fixCorpoParagrafoSub(wikiTitle, key, numVoci, mappaSub.get(key)));
            }
        }

        return buffer.toString();
    }

    protected String mappaToTextSubSub(String wikiTitle, List<String> listaSub) {
        StringBuffer buffer = new StringBuffer();

        if (listaSub != null) {
            for (String didascalia : listaSub) {
                buffer.append(didascalia + CAPO);
            }
        }

        return buffer.toString();
    }

    protected String fixTitoloParagrafo(String titoloParagrafo, int numVoci) {
        if (WPref.usaParagrafiDimensionati.is()) {
            return wikiUtility.fixTitolo(titoloLinkParagrafo, titoloParagrafo, numVoci);
        }
        else {
            return wikiUtility.fixTitolo(titoloLinkParagrafo, titoloParagrafo);
        }
    }

    protected String fixTitoloParagrafoSub(String titoloParagrafo, int numVoci) {
        if (WPref.usaParagrafiDimensionati.is()) {
            return wikiUtility.fixTitolo(titoloParagrafo, numVoci);
        }
        else {
            return wikiUtility.fixTitolo(titoloParagrafo);
        }
    }


    protected String fixCorpoParagrafo(String wikiTitle, String titoloParagrafo, int numVoci, LinkedHashMap<String, List<String>> mappaSub) {
        StringBuffer buffer = new StringBuffer();
        List<String> lista;
        int max = WPref.sogliaSottoPagina.getInt();
        String parente;

        if (numVoci > max) {
            titoloParagrafo = textService.isValid(titoloParagrafo) ? titoloParagrafo : TAG_ALTRE;
            parente = String.format("%s%s%s", wikiTitle, SLASH, textService.primaMaiuscola(titoloParagrafo));
            String vedi = String.format("{{Vedi anche|%s}}", parente);
            buffer.append(vedi + CAPO);
            this.uploadSottoPagina(parente, numVoci, mappaSub);
        }
        else {
            for (String key : mappaSub.keySet()) {
                lista = mappaSub.get(key);
                for (String didascalia : lista) {
                    buffer.append(didascalia + CAPO);
                }
            }
        }

        return buffer.toString();
    }

    protected String fixCorpoParagrafoSub(String wikiTitle, String titoloParagrafo, int numVoci, List<String> listaSub) {
        StringBuffer buffer = new StringBuffer();
        int max = WPref.sogliaSottoPagina.getInt();
        String parente;

        if (numVoci > max) {
            titoloParagrafo = textService.isValid(titoloParagrafo) ? titoloParagrafo : TAG_ALTRE;
            parente = String.format("%s%s%s", wikiTitle, SLASH, textService.primaMaiuscola(titoloParagrafo));
            String vedi = String.format("{{Vedi anche|%s}}", parente);
            buffer.append(vedi + CAPO);
            this.uploadSottoSottoPagina(parente, listaSub);
        }
        else {
            for (String didascalia : listaSub) {
                buffer.append(didascalia + CAPO);
            }
        }

        return buffer.toString();
    }

    /**
     * Esegue la scrittura della sotto-pagina <br>
     */
    public void uploadSottoPagina(String wikiTitleParente, int numVoci, LinkedHashMap<String, List<String>> mappaSub) {
    }

    /**
     * Esegue la scrittura della sotto-sotto-pagina <br>
     */
    public void uploadSottoSottoPagina(String wikiTitleParente, List<String> lista) {
    }

    protected String subMappaToText(String wikiTitle, LinkedHashMap<String, List<String>> mappaSub) {
        StringBuffer buffer = new StringBuffer();
        List<String> listaSub;

        if (mappaSub != null) {
            System.out.println(VUOTA);

            for (String key : mappaSub.keySet()) {
                listaSub = mappaSub.get(key);

                buffer.append(fixTitoloParagrafo(key, listaSub.size()));
                buffer.append(CAPO);
                for (String didascalia : listaSub) {
                    buffer.append(didascalia + CAPO);
                }
                buffer.append(CAPO);
            }
        }

        return buffer.toString();
    }

    public String setParagrafo(final String titolo) {
        return setParagrafo(titolo, 0);
    }


    /**
     * Inserisce un numero in caratteri ridotti <br>
     *
     * @param titolo da inglobare nei tag wiki (paragrafo)
     * @param numero da visualizzare (maggiore di zero)
     *
     * @return testo coi tag html
     */
    public String setParagrafo(final String titolo, final int numero) {
        String paragrafo = VUOTA;

        paragrafo += PARAGRAFO;
        paragrafo += SPAZIO;
        paragrafo += titolo;
        if (numero > 0) {
            paragrafo += SPAZIO;
            paragrafo += smallNumero(numero);
        }
        paragrafo += SPAZIO;
        paragrafo += PARAGRAFO;
        paragrafo += CAPO;

        return paragrafo;
    }


    /**
     * Inserisce un numero in caratteri ridotti <br>
     *
     * @param numero da visualizzare
     *
     * @return testo coi tag html
     */
    public String smallNumero(final int numero) {
        String testo = VUOTA;

        testo += "<span style=\"font-size:70%\">(";
        testo += numero;
        testo += ")</span>";

        return testo;
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

        message = String.format("Check");
        logger.info(new WrapLog().message(message).type(AETypeLog.upload).usaDb());
    }

}
