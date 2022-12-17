package it.algos.wiki23.backend.liste;

import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.enumeration.*;
import it.algos.vaad24.backend.packages.crono.anno.*;
import it.algos.vaad24.backend.packages.crono.giorno.*;
import it.algos.vaad24.backend.service.*;
import it.algos.vaad24.backend.wrapper.*;
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

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Fri, 03-Jun-2022
 * Time: 16:08
 * <p>
 * Classe specializzata nella creazione di liste. <br>
 * La lista è una mappa di WrapLista suddivisa in paragrafi, che contiene tutte le informazioni per scrivere le righe della pagina <br>
 * <p>
 * Liste cronologiche (in namespace principale) di nati e morti nel giorno o nell'anno <br>
 * Liste di nomi e cognomi (in namespace principale) <br>
 * Liste di attività e nazionalità (in Progetto:Biografie) <br>
 * Sovrascritta nelle sottoclassi concrete <br>
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

    protected String nomeLista;

    protected String titoloParagrafo;

    protected AETypeLista typeLista;

    protected String paragrafoAltre = VUOTA;


    /**
     * Lista ordinata delle biografie (Bio) che hanno una valore valido per la pagina specifica <br>
     */
    public List<Bio> listaBio() {
        if (textService.isEmpty(nomeLista)) {
            logger.info(new WrapLog().message("Manca il nomeLista"));
            return null;
        }

        try {
            listaBio = bioService.fetchListe(typeLista, nomeLista);
        } catch (Exception unErrore) {
            return null;
        }

        return listaBio;
    }


    /**
     * Lista ordinata di tutti i wrapLista che hanno una valore valido per la pagina specifica <br>
     */
    public List<WrapLista> listaWrap() {
        listaWrap = new ArrayList<>();
        WrapLista wrap;

        if (listaBio == null || listaBio.size() == 0) {
            listaBio();
        }

        if (listaBio != null && listaBio.size() > 0) {
            for (Bio bio : listaBio) {
                wrap = didascaliaService.getWrap(typeLista, bio);
                if (wrap != null) {
                    listaWrap.add(wrap);
                }
            }
        }

        return listaWrap;
    }


    /**
     * Mappa ordinata di tutti i wrapLista che hanno una valore valido per la pagina specifica <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public LinkedHashMap<String, List<WrapLista>> mappaWrap() {
        mappaWrap = new LinkedHashMap<>();
        String paragrafo;
        List<WrapLista> lista;
        List<WrapLista> listaAltre;

        if (listaWrap == null || listaWrap.size() == 0) {
            this.listaWrap();
        }

        if (listaWrap != null && listaWrap.size() > 0) {
            for (WrapLista wrap : listaWrap) {
                paragrafo = wrap.titoloParagrafo;

                if (mappaWrap.containsKey(paragrafo)) {
                    lista = mappaWrap.get(paragrafo);
                }
                else {
                    lista = new ArrayList();
                }
                lista.add(wrap);
                mappaWrap.put(paragrafo, lista);
            }
        }

        mappaWrap = sortMap(mappaWrap);
        mappaWrap = fixAltreInCoda(mappaWrap);
        return mappaWrap;
    }


    /**
     * Ordina la mappa <br>
     * Può essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    public LinkedHashMap<String, List<WrapLista>> sortMap(LinkedHashMap<String, List<WrapLista>> mappa) {
        return mappa;
    }

    /**
     * Sposta in coda alla mappa il paragrafo 'Altre...' (eventuale) <br>
     */
    public LinkedHashMap<String, List<WrapLista>> fixAltreInCoda(LinkedHashMap<String, List<WrapLista>> mappa) {
        List<WrapLista> listaAltre;

        if (mappa.containsKey(paragrafoAltre)) {
            listaAltre = mappa.get(paragrafoAltre);
            mappa.remove(paragrafoAltre);
            mappa.put(paragrafoAltre, listaAltre);
        }

        return mappa;
    }


    //    /**
    //     * Mappa ordinata di tutti le didascalie che hanno una valore valido per la pagina specifica <br>
    //     * Le didascalie usano SPAZIO_NON_BREAKING al posto di SPAZIO (se previsto) <br>
    //     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
    //     */
    //    public LinkedHashMap<String, List<String>> mappaDidascalia() {
    //        mappaDidascalia = new LinkedHashMap<>();
    //
    //        if (mappaWrap == null || mappaWrap.size() > 0) {
    //            this.mappaWrap();
    //        }
    //
    //        return mappaDidascalia;
    //    }

    /**
     * Mappa ordinata di tutti le didascalie che hanno una valore valido per la pagina specifica <br>
     * Le didascalie usano SPAZIO_NON_BREAKING al posto di SPAZIO (se previsto) <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Deprecated
    public LinkedHashMap<String, List<String>> mappaDidascalia() {
        mappaDidascalia = new LinkedHashMap<>();
        String didascalia;
        List<WrapLista> listaWrap;
        List<String> listaDidascalie;

        if (mappaWrap == null || mappaWrap.size() == 0) {
            this.mappaWrap();
        }

        if (mappaWrap != null && mappaWrap.size() > 0) {
            for (String paragrafo : mappaWrap.keySet()) {
                listaWrap = mappaWrap.get(paragrafo);
                if (listaWrap != null) {
                    listaDidascalie = new ArrayList<>();
                    for (WrapLista wrap : listaWrap) {
                        didascalia = switch (typeLista) {
                            case giornoNascita, giornoMorte, annoNascita, annoMorte -> wrap.didascaliaLunga;
                            case nazionalitaSingolare, nazionalitaPlurale -> wrap.didascaliaBreve;
                            default -> VUOTA;
                        };
                        if (Pref.usaNonBreaking.is()) {
                            didascalia = didascalia.replaceAll(SPAZIO, SPAZIO_NON_BREAKING);
                        }
                        listaDidascalie.add(didascalia);
                    }
                    mappaDidascalia.put(paragrafo, listaDidascalie);
                }
            }
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
        return WResult.errato();
    }

//    /**
//     * Costruisce una lista dei wrapper per gestire i dati necessari ad una didascalia <br>
//     * La sottoclasse specifica esegue l'ordinamento <br>
//     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
//     */
//    public List<WrapDidascalia> listaWrapDidascalie() {
//        this.listaBio();
//
//        if (listaBio != null) {
//            listaWrapDidascalie = new ArrayList<>();
//            for (Bio bio : listaBio) {
//                listaWrapDidascalie.add(creaWrapDidascalia(bio));
//            }
//        }
//
//        return listaWrapDidascalie;
//    }


//    /**
//     * Mappa ordinata dei wrapper (WrapDidascalia) per gestire i dati necessari ad una didascalia <br>
//     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
//     */
//    public LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaWrapDidascalie() {
//        this.listaWrapDidascalie();
//        mappaWrapDidascalie = new LinkedHashMap<>();
//        return mappaWrapDidascalie;
//    }


//    /**
//     * Mappa ordinata delle didascalie che hanno una valore valido per la pagina specifica <br>
//     * La mappa è composta da una chiave (ordinata) che corrisponde al titolo del paragrafo <br>
//     * Ogni valore della mappa è costituito da una lista di didascalie per ogni paragrafo <br>
//     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
//     */
//    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaDidascalie() {
//        this.mappaWrapDidascalie();
//        mappaDidascalieOld = new LinkedHashMap<>();
//        LinkedHashMap<String, List<WrapDidascalia>> mappaWrap;
//        List<WrapDidascalia> listaWrap;
//        List<String> listaDidascalia;
//        String didascalia;
//
//        for (String key1 : mappaWrapDidascalie.keySet()) {
//            mappaWrap = mappaWrapDidascalie.get(key1);
//            mappaDidascalieOld.put(key1, new LinkedHashMap<>());
//
//            for (String key2 : mappaWrap.keySet()) {
//                listaWrap = mappaWrap.get(key2);
//                listaDidascalia = new ArrayList<>();
//                for (WrapDidascalia wrap : listaWrap) {
//                    didascalia = switch (typeLista) {
//                        case giornoNascita -> didascaliaService.getDidascaliaAnnoNato(wrap.getBio());
//                        case giornoMorte -> didascaliaService.getDidascaliaAnnoMorto(wrap.getBio());
//                        case annoNascita -> didascaliaService.getDidascaliaGiornoNato(wrap.getBio());
//                        case annoMorte -> didascaliaService.getDidascaliaGiornoMorto(wrap.getBio());
//                        case listaBreve -> didascaliaService.getDidascaliaLista(wrap.getBio());
//                        default -> VUOTA;
//                    };
//                    listaDidascalia.add(didascalia);
//                }
//                mappaDidascalieOld.get(key1).put(key2, listaDidascalia);
//            }
//        }
//
//        return mappaDidascalieOld;
//    }

//    /**
//     * Mappa dei paragrafi delle didascalie che hanno una valore valido per la pagina specifica <br>
//     * La mappa è composta da una chiave (ordinata) che è il titolo visibile del paragrafo <br>
//     * Ogni valore della mappa è costituito da una lista di didascalie per ogni paragrafo <br>
//     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
//     */
//    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaParagrafi() {
//        this.mappaDidascalie();
//        mappaParagrafi = new LinkedHashMap<>();
//        LinkedHashMap<String, List<String>> mappaSub;
//        String paragrafo;
//
//        for (String key : mappaDidascalieOld.keySet()) {
//            paragrafo = key;
//            mappaSub = mappaDidascalieOld.get(key);
//            paragrafo = fixTitolo(titoloParagrafo, paragrafo);
//
//            mappaParagrafi.put(paragrafo, mappaSub);
//        }
//
//        return mappaParagrafi;
//    }
//
//    public String fixTitolo(String wikiTitleBase, String paragrafo) {
//        return wikiUtility.fixTitolo(titoloParagrafo, paragrafo);
//    }
//
//
//    /**
//     * Mappa dei paragrafi delle didascalie che hanno una valore valido per la pagina specifica <br>
//     * La mappa è composta da una chiave (ordinata) che è il titolo visibile del paragrafo <br>
//     * Nel titolo visibile del paragrafo viene riportato il numero di voci biografiche presenti <br>
//     * Ogni valore della mappa è costituito da una lista di didascalie per ogni paragrafo <br>
//     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
//     */
//    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaParagrafiDimensionati() {
//        this.mappaDidascalie();
//        mappaParagrafiDimensionati = new LinkedHashMap<>();
//        LinkedHashMap<String, List<String>> mappaSub;
//        String paragrafoDimensionato;
//        int size;
//
//        for (String key : mappaDidascalieOld.keySet()) {
//            paragrafoDimensionato = key;
//            mappaSub = mappaDidascalieOld.get(key);
//            size = wikiUtility.getSize(mappaSub);
//            paragrafoDimensionato = wikiUtility.fixTitolo(titoloParagrafo, paragrafoDimensionato, size);
//
//            mappaParagrafiDimensionati.put(paragrafoDimensionato, mappaSub);
//        }
//
//        return mappaParagrafiDimensionati;
//    }
//
//
//    protected WrapDidascalia creaWrapDidascalia(Bio bio) {
//        WrapDidascalia wrap = new WrapDidascalia();
//        AnnoWiki anno;
//
//        wrap.setAttivitaSingola(bio.attivita);
//        if (textService.isValid(bio.attivita)) {
//            wrap.setAttivitaParagrafo(attivitaBackend.findFirstBySingolare(bio.attivita).paragrafo);
//        }
//
//        wrap.setNazionalitaSingola(bio.nazionalita);
//        if (textService.isValid(bio.nazionalita)) {
//            wrap.setNazionalitaParagrafo(nazionalitaBackend.findFirstBySingolare(bio.nazionalita).plurale);
//        }
//
//        wrap.setGiornoNato(bio.giornoNato);
//        if (textService.isValid(bio.giornoNato)) {
//            wrap.setMeseParagrafoNato(fixMese(bio.giornoNato));
//        }
//        wrap.setGiornoMorto(bio.giornoMorto);
//        if (textService.isValid(bio.giornoMorto)) {
//            wrap.setMeseParagrafoMorto(fixMese(bio.giornoMorto));
//        }
//
//        wrap.setAnnoNato(bio.annoNato);
//        if (textService.isValid(bio.annoNato)) {
//            wrap.setSecoloParagrafoNato(fixSecolo(bio.annoNato));
//        }
//        wrap.setAnnoMorto(bio.annoMorto);
//        if (textService.isValid(bio.annoMorto)) {
//            wrap.setSecoloParagrafoMorto(fixSecolo(bio.annoMorto));
//        }
//
//        wrap.setWikiTitle(bio.wikiTitle);
//        wrap.setNome(bio.nome);
//        wrap.setCognome(bio.cognome);
//        wrap.setPrimoCarattere(bio.ordinamento.substring(0, 1));
//
//        wrap.setBio(bio); //@todo meglio eliminarlo
//        return wrap;
//    }
//
//
//    public LinkedHashMap<String, List<WrapDidascalia>> creaMappaCarattere(List<WrapDidascalia> listaWrapNonOrdinata) {
//        LinkedHashMap<String, List<WrapDidascalia>> mappa = new LinkedHashMap<>();
//        List lista;
//        String primoCarattere;
//
//        if (listaWrapNonOrdinata != null) {
//            for (WrapDidascalia wrap : listaWrapNonOrdinata) {
//                primoCarattere = wrap.getPrimoCarattere();
//                if (mappa.containsKey(primoCarattere)) {
//                    lista = mappa.get(primoCarattere);
//                }
//                else {
//                    lista = new ArrayList();
//                }
//                lista.add(wrap);
//                mappa.put(primoCarattere, lista);
//            }
//        }
//
//        for (String key : mappa.keySet()) {
//            lista = mappa.get(key);
//            lista = sortByCognome(lista);
//            mappa.put(key, lista);
//        }
//
//        return mappa;
//    }
//
//    public List<WrapDidascalia> sortByCognome(List<WrapDidascalia> listaWrapNonOrdinata) {
//        List<WrapDidascalia> sortedList = new ArrayList<>();
//        List<WrapDidascalia> listaConCognomeOrdinata = new ArrayList<>(); ;
//        List<WrapDidascalia> listaSenzaCognomeOrdinata = new ArrayList<>(); ;
//
//        listaConCognomeOrdinata = listaWrapNonOrdinata
//                .stream()
//                .filter(wrap -> wrap.getCognome() != null)
//                .sorted(Comparator.comparing(funCognome))
//                .collect(Collectors.toList());
//
//        listaSenzaCognomeOrdinata = listaWrapNonOrdinata
//                .stream()
//                .filter(wrap -> wrap.getCognome() == null)
//                .sorted(Comparator.comparing(funWikiTitle))
//                .collect(Collectors.toList());
//
//        sortedList.addAll(listaConCognomeOrdinata);
//        sortedList.addAll(listaSenzaCognomeOrdinata);
//        return sortedList;
//    }
//
//    public String fixMese(final String giornoWiki) {
//        Giorno giorno = giornoBackend.findByNome(giornoWiki);
//        return giorno != null ? giorno.getMese().nome : VUOTA;
//    }
//
//    public String fixSecolo(final String annoWiki) {
//        Anno anno = annoBackend.findByNome(annoWiki);
//        return anno != null ? anno.getSecolo().nome : VUOTA;
//    }
//
//    /**
//     * Ordina la mappa secondo la chiave
//     *
//     * @param mappaDisordinata in ingresso
//     *
//     * @return mappa ordinata, null se mappaDisordinata è null
//     */
//    public LinkedHashMap sort(final LinkedHashMap<String, List<WrapLista>> mappaDisordinata) {
//        LinkedHashMap mappaOrdinata = new LinkedHashMap();
//        Object[] listaChiavi;
//
//        listaChiavi = mappaDisordinata.keySet().toArray();
//
//        try {
//            Arrays.sort(listaChiavi);
//        } catch (Exception unErrore) {
//            logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb());
//        }
//
//        for (Object chiave : listaChiavi) {
//            mappaOrdinata.put(chiave, mappaDisordinata.get(chiave));
//        }
//
//        return mappaOrdinata;
//    }


}
