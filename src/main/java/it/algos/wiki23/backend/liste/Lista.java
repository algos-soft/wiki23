package it.algos.wiki23.backend.liste;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.service.*;
import it.algos.wiki23.backend.packages.attivita.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.packages.nazionalita.*;
import it.algos.wiki23.backend.service.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.beans.factory.annotation.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Fri, 03-Jun-2022
 * Time: 16:08
 * <p>
 * Classe specializzata nella creazione di liste. <br>
 * La lista è un semplice testo (formattato secondo i possibili tipi di raggruppamento) <br>
 * <p>
 * Liste cronologiche (in namespace principale) di nati e morti nel giorno o nell'anno <br>
 * Liste di nomi e cognomi (in namespace principale) <br>
 * Liste di attività e nazionalità (in Progetto:Biografie) <br>
 * <p>
 * // * Sovrascritta nelle sottoclassi concrete <br>
 * // * Not annotated with @SpringComponent (sbagliato) perché è una classe astratta <br>
 * // * Punto d'inizio @PostConstruct inizia() nella superclasse <br>
 * <p>
 * // * La (List<Bio>) listaBio, la (List<String>) listaDidascalie, la (Map<String, List<String>>) mappa e (String) testoConParagrafi
 * // * vengono tutte regolate alla creazione dell'istanza in @PostConstruct e sono disponibili da subito <br>
 * // * Si può quindi usare la chiamata appContext.getBean(ListaXxx.class, yyy).getTestoConParagrafi() senza esplicitare l'istanza <br>
 * <p>
 * In uscita sono disponibili i metodi:
 * listaBio() -> Lista ordinata (per cognome) delle biografie (Bio) che hanno una valore valido per la pagina specifica <br>
 * listaWrapDidascalia() -> Lista ordinata dei wrapper (WrapDidascalia) per gestire i dati necessari ad una didascalia <br>
 * mappaWrapDidascalie() -> Mappa ordinata dei wrapper (WrapDidascalia) per gestire i dati necessari ad una didascalia <br>
 * mappaDidascalie() -> Mappa ordinata delle didascalie che hanno una valore valido per la pagina specifica <br>
 * mappaParagrafi() <br>
 */
public abstract class Lista {

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
    public ArrayService arrayService;

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
    public ElaboraService elaboraService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public DidascaliaService didascaliaService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public NazionalitaBackend nazionalitaBackend;


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
    public BioService bioService;

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
    public WikiUtility wikiUtility;

    /**
     * Lista ordinata (per cognome) delle biografie (Bio) che hanno una valore valido per la pagina specifica <br>
     * La lista è ordinata per cognome <br>
     */
    protected List<Bio> listaBio;

    /**
     * Lista ordinata dei wrapper (WrapDidascalia) per gestire i dati necessari ad una didascalia <br>
     */
    protected List<WrapDidascalia> listaWrapDidascalie;

    /**
     * Mappa ordinata dei wrapper (WrapDidascalia) per gestire i dati necessari ad una didascalia <br>
     */
    protected LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaWrapDidascalie;

    /**
     * Mappa ordinata delle didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) che corrisponde al futuro titolo del paragrafo <br>
     * Ogni valore della mappa è costituito da una lista di didascalie per ogni paragrafo <br>
     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
     */
    protected LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaDidascalie;

    /**
     * Mappa dei paragrafi delle didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) che è il titolo visibile del paragrafo <br>
     * Ogni valore della mappa è costituito da una lista di didascalie per ogni paragrafo <br>
     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
     */
    protected LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaParagrafi;

    /**
     * Mappa dei paragrafi delle didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) che è il titolo visibile del paragrafo <br>
     * Nel titolo visibile del paragrafo viene riportato il numero di voci biografiche presenti <br>
     * Ogni valore della mappa è costituito da una lista di didascalie per ogni paragrafo <br>
     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
     */
    protected LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaParagrafiDimensionati;

    public static Function<WrapDidascalia, String> cognome = wrap -> wrap.getCognome() != null ? wrap.getCognome() : VUOTA;

    public static Function<WrapDidascalia, String> wikiTitle = wrap -> wrap.getWikiTitle() != null ? wrap.getWikiTitle() : VUOTA;

    protected String titoloParagrafo;

    /**
     * Lista ordinata (per cognome) delle biografie (Bio) che hanno una valore valido per la pagina specifica <br>
     */
    public List<Bio> listaBio() {
        listaBio = new ArrayList<>();
        return listaBio;
    }

    /**
     * Costruisce una lista dei wrapper per gestire i dati necessari ad una didascalia <br>
     * La sottoclasse specifica esegue l'ordinamento <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public List<WrapDidascalia> listaWrapDidascalie() {
        this.listaBio();

        if (listaBio != null) {
            listaWrapDidascalie = new ArrayList<>();
            for (Bio bio : listaBio) {
                listaWrapDidascalie.add(creaWrapDidascalia(bio));
            }
        }

        return listaWrapDidascalie;
    }


    /**
     * Mappa ordinata dei wrapper (WrapDidascalia) per gestire i dati necessari ad una didascalia <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaWrapDidascalie() {
        this.listaWrapDidascalie();
        mappaWrapDidascalie = new LinkedHashMap<>();
        return mappaWrapDidascalie;
    }


    /**
     * Mappa ordinata delle didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) che corrisponde al titolo del paragrafo <br>
     * Ogni valore della mappa è costituito da una lista di didascalie per ogni paragrafo <br>
     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
     */
    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaDidascalie() {
        this.mappaWrapDidascalie();
        mappaDidascalie = new LinkedHashMap<>();
        return mappaDidascalie;
    }

    /**
     * Mappa dei paragrafi delle didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) che è il titolo visibile del paragrafo <br>
     * Ogni valore della mappa è costituito da una lista di didascalie per ogni paragrafo <br>
     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
     */
    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaParagrafi() {
        this.mappaDidascalie();
        mappaParagrafi = new LinkedHashMap<>();
        return mappaParagrafi;
    }


    /**
     * Mappa dei paragrafi delle didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) che è il titolo visibile del paragrafo <br>
     * Nel titolo visibile del paragrafo viene riportato il numero di voci biografiche presenti <br>
     * Ogni valore della mappa è costituito da una lista di didascalie per ogni paragrafo <br>
     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
     */
    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaParagrafiDimensionati() {
        this.mappaDidascalie();
        mappaParagrafiDimensionati = new LinkedHashMap<>();
        return mappaParagrafiDimensionati;
    }


    protected WrapDidascalia creaWrapDidascalia(Bio bio) {
        WrapDidascalia wrap = null;
        wrap = new WrapDidascalia();

        wrap.setAttivitaSingola(bio.attivita);
        if (textService.isValid(bio.attivita)) {
            wrap.setAttivitaParagrafo(attivitaBackend.findFirstBySingolare(bio.attivita).plurale);
        }
        wrap.setNazionalitaSingola(bio.nazionalita);
        if (textService.isValid(bio.nazionalita)) {
            wrap.setNazionalitaParagrafo(nazionalitaBackend.findBySingolare(bio.nazionalita).plurale);
        }
        wrap.setWikiTitle(bio.wikiTitle);
        wrap.setNome(bio.nome);
        wrap.setCognome(bio.cognome);
        wrap.setPrimoCarattere(bio.ordinamento.substring(0, 1));

        wrap.setBio(bio); //@todo meglio eliminarlo
        return wrap;
    }


    public List<WrapDidascalia> sortByCognome(List<WrapDidascalia> listaWrapNonOrdinata) {
        List<WrapDidascalia> sortedList = new ArrayList<>();
        List<WrapDidascalia> listaConCognomeOrdinata = new ArrayList<>(); ;
        List<WrapDidascalia> listaSenzaCognomeOrdinata = new ArrayList<>(); ;

        listaConCognomeOrdinata = listaWrapNonOrdinata
                .stream()
                .filter(wrap -> wrap.getCognome() != null)
                .sorted(Comparator.comparing(cognome))
                .collect(Collectors.toList());

        listaSenzaCognomeOrdinata = listaWrapNonOrdinata
                .stream()
                .filter(wrap -> wrap.getCognome() == null)
                .sorted(Comparator.comparing(wikiTitle))
                .collect(Collectors.toList());

        sortedList.addAll(listaConCognomeOrdinata);
        sortedList.addAll(listaSenzaCognomeOrdinata);
        return sortedList;
    }

    //    public TreeMap<String, TreeMap<String, List<String>>> getMappa() {
    //        return mappa;
    //    }

}
