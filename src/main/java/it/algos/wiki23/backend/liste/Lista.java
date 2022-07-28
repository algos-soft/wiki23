package it.algos.wiki23.backend.liste;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.packages.crono.anno.*;
import it.algos.vaad23.backend.packages.crono.giorno.*;
import it.algos.vaad23.backend.service.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.anno.*;
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
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AnnoWikiBackend annoWikiBackend;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public GiornoBackend giornoBackend;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AnnoBackend annoBackend;


    //--property
    protected List<String> listaNomiSingoli;

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
    protected LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaDidascalieOld;

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

    /**
     * Lista ordinata dei wrapper (WrapLista) per gestire i dati necessari ad una didascalia <br>
     */
    protected List<WrapLista> listaWrap;

    protected LinkedHashMap<String, List<WrapLista>> mappaWrap;

    protected LinkedHashMap<String, List<String>> mappaDidascalia;

    public static Function<WrapDidascalia, String> funCognome = wrap -> wrap.getCognome() != null ? wrap.getCognome() : VUOTA;

    public static Function<WrapDidascalia, String> funWikiTitle = wrap -> wrap.getWikiTitle() != null ? wrap.getWikiTitle() : VUOTA;

    public static Function<WrapDidascalia, String> funNazionalita = wrap -> wrap.getNazionalitaSingola() != null ? wrap.getNazionalitaSingola() : VUOTA;

    public static Function<WrapDidascalia, String> funAttivita = wrap -> wrap.getAttivitaSingola() != null ? wrap.getAttivitaSingola() : VUOTA;

    public static Function<WrapDidascalia, String> funAnnoNato = wrap -> wrap.getAnnoNato() != null ? wrap.getAnnoNato() : VUOTA;

    public static Function<WrapDidascalia, String> funAnnoMorto = wrap -> wrap.getAnnoMorto() != null ? wrap.getAnnoMorto() : VUOTA;

    public static Function<WrapDidascalia, String> funGiornoNato = wrap -> wrap.getGiornoNato() != null ? wrap.getGiornoNato() : VUOTA;

    public static Function<WrapDidascalia, String> funGiornoMorto = wrap -> wrap.getGiornoMorto() != null ? wrap.getGiornoMorto() : VUOTA;

    protected String titoloParagrafo;

    protected AETypeCrono typeCrono;

    protected AETypeDidascalia typeDidascalia = AETypeDidascalia.listaBreve;

    /**
     * Lista ordinata (per cognome) delle biografie (Bio) che hanno una valore valido per la pagina specifica <br>
     */
    public List<Bio> listaBio() {
        listaBio = new ArrayList<>();
        return listaBio;
    }


    /**
     * Lista ordinata di tutti i wrapLista che hanno una valore valido per la pagina specifica <br>
     */
    public List<WrapLista> listaWrap() {
        listaWrap = new ArrayList<>();

        if (listaBio == null || listaBio.size() > 0) {
            this.listaBio();
        }

        return listaWrap;
    }


    /**
     * Mappa ordinata di tutti i wrapLista che hanno una valore valido per la pagina specifica <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public LinkedHashMap<String, List<WrapLista>> mappaWrap() {
        mappaWrap = new LinkedHashMap<>();

        if (listaWrap == null || listaWrap.size() > 0) {
            this.listaWrap();
        }

        return mappaWrap;
    }

    /**
     * Mappa ordinata di tutti le didascalie che hanno una valore valido per la pagina specifica <br>
     * Le didascalie usano SPAZIO_NON_BREAKING al posto di SPAZIO (se previsto) <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public LinkedHashMap<String, List<String>> mappaDidascalia() {
        mappaDidascalia = new LinkedHashMap<>();

        if (mappaWrap == null || mappaWrap.size() > 0) {
            this.mappaWrap();
        }

        return mappaDidascalia;
    }

    /**
     * Testo del body di upload con paragrafi e righe <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public WResult testoBody() {
        String testoBody = VUOTA;

        if (mappaDidascalia == null || mappaDidascalia.size() > 0) {
            this.mappaDidascalia();
        }
        return null;
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
        mappaDidascalieOld = new LinkedHashMap<>();
        LinkedHashMap<String, List<WrapDidascalia>> mappaWrap;
        List<WrapDidascalia> listaWrap;
        List<String> listaDidascalia;
        String didascalia;

        for (String key1 : mappaWrapDidascalie.keySet()) {
            mappaWrap = mappaWrapDidascalie.get(key1);
            mappaDidascalieOld.put(key1, new LinkedHashMap<>());

            for (String key2 : mappaWrap.keySet()) {
                listaWrap = mappaWrap.get(key2);
                listaDidascalia = new ArrayList<>();
                for (WrapDidascalia wrap : listaWrap) {
                    didascalia = switch (typeDidascalia) {
                        case listaBreve -> didascaliaService.getDidascaliaLista(wrap.getBio());
                        case giornoNascita -> didascaliaService.getDidascaliaAnnoNato(wrap.getBio());
                        case giornoMorte -> didascaliaService.getDidascaliaAnnoMorto(wrap.getBio());
                        case annoNascita -> didascaliaService.getDidascaliaGiornoNato(wrap.getBio());
                        case annoMorte -> didascaliaService.getDidascaliaGiornoMorto(wrap.getBio());
                        default -> VUOTA;
                    };
                    listaDidascalia.add(didascalia);
                }
                mappaDidascalieOld.get(key1).put(key2, listaDidascalia);
            }
        }

        return mappaDidascalieOld;
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
        LinkedHashMap<String, List<String>> mappaSub;
        String paragrafo;

        for (String key : mappaDidascalieOld.keySet()) {
            paragrafo = key;
            mappaSub = mappaDidascalieOld.get(key);
            paragrafo = fixTitolo(titoloParagrafo, paragrafo);

            mappaParagrafi.put(paragrafo, mappaSub);
        }

        return mappaParagrafi;
    }

    public String fixTitolo(String wikiTitleBase, String paragrafo) {
        return wikiUtility.fixTitolo(titoloParagrafo, paragrafo);
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
        LinkedHashMap<String, List<String>> mappaSub;
        String paragrafoDimensionato;
        int size;

        for (String key : mappaDidascalieOld.keySet()) {
            paragrafoDimensionato = key;
            mappaSub = mappaDidascalieOld.get(key);
            size = wikiUtility.getSize(mappaSub);
            paragrafoDimensionato = wikiUtility.fixTitolo(titoloParagrafo, paragrafoDimensionato, size);

            mappaParagrafiDimensionati.put(paragrafoDimensionato, mappaSub);
        }

        return mappaParagrafiDimensionati;
    }


    protected WrapDidascalia creaWrapDidascalia(Bio bio) {
        WrapDidascalia wrap = new WrapDidascalia();
        AnnoWiki anno;

        wrap.setAttivitaSingola(bio.attivita);
        if (textService.isValid(bio.attivita)) {
            wrap.setAttivitaParagrafo(attivitaBackend.findFirstBySingolare(bio.attivita).paragrafo);
        }

        wrap.setNazionalitaSingola(bio.nazionalita);
        if (textService.isValid(bio.nazionalita)) {
            wrap.setNazionalitaParagrafo(nazionalitaBackend.findFirstBySingolare(bio.nazionalita).plurale);
        }

        wrap.setGiornoNato(bio.giornoNato);
        if (textService.isValid(bio.giornoNato)) {
            wrap.setMeseParagrafoNato(fixMese(bio.giornoNato));
        }
        wrap.setGiornoMorto(bio.giornoMorto);
        if (textService.isValid(bio.giornoMorto)) {
            wrap.setMeseParagrafoMorto(fixMese(bio.giornoMorto));
        }

        wrap.setAnnoNato(bio.annoNato);
        if (textService.isValid(bio.annoNato)) {
            wrap.setSecoloParagrafoNato(fixSecolo(bio.annoNato));
        }
        wrap.setAnnoMorto(bio.annoMorto);
        if (textService.isValid(bio.annoMorto)) {
            wrap.setSecoloParagrafoMorto(fixSecolo(bio.annoMorto));
        }

        wrap.setWikiTitle(bio.wikiTitle);
        wrap.setNome(bio.nome);
        wrap.setCognome(bio.cognome);
        wrap.setPrimoCarattere(bio.ordinamento.substring(0, 1));

        wrap.setBio(bio); //@todo meglio eliminarlo
        return wrap;
    }


    public LinkedHashMap<String, List<WrapDidascalia>> creaMappaCarattere(List<WrapDidascalia> listaWrapNonOrdinata) {
        LinkedHashMap<String, List<WrapDidascalia>> mappa = new LinkedHashMap<>();
        List lista;
        String primoCarattere;

        if (listaWrapNonOrdinata != null) {
            for (WrapDidascalia wrap : listaWrapNonOrdinata) {
                primoCarattere = wrap.getPrimoCarattere();
                if (mappa.containsKey(primoCarattere)) {
                    lista = mappa.get(primoCarattere);
                }
                else {
                    lista = new ArrayList();
                }
                lista.add(wrap);
                mappa.put(primoCarattere, lista);
            }
        }

        for (String key : mappa.keySet()) {
            lista = mappa.get(key);
            lista = sortByCognome(lista);
            mappa.put(key, lista);
        }

        return mappa;
    }

    public List<WrapDidascalia> sortByCognome(List<WrapDidascalia> listaWrapNonOrdinata) {
        List<WrapDidascalia> sortedList = new ArrayList<>();
        List<WrapDidascalia> listaConCognomeOrdinata = new ArrayList<>(); ;
        List<WrapDidascalia> listaSenzaCognomeOrdinata = new ArrayList<>(); ;

        listaConCognomeOrdinata = listaWrapNonOrdinata
                .stream()
                .filter(wrap -> wrap.getCognome() != null)
                .sorted(Comparator.comparing(funCognome))
                .collect(Collectors.toList());

        listaSenzaCognomeOrdinata = listaWrapNonOrdinata
                .stream()
                .filter(wrap -> wrap.getCognome() == null)
                .sorted(Comparator.comparing(funWikiTitle))
                .collect(Collectors.toList());

        sortedList.addAll(listaConCognomeOrdinata);
        sortedList.addAll(listaSenzaCognomeOrdinata);
        return sortedList;
    }

    public String fixMese(final String giornoWiki) {
        Giorno giorno = giornoBackend.findByNome(giornoWiki);
        return giorno != null ? giorno.getMese().nome : VUOTA;
    }

    public String fixSecolo(final String annoWiki) {
        Anno anno = annoBackend.findByNome(annoWiki);
        return anno != null ? anno.getSecolo().nome : VUOTA;
    }


}
