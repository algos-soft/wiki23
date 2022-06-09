package it.algos.wiki23.backend.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.wiki23.backend.packages.attivita.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.service.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

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
 * Lista delle biografie per attività <br>
 * <p>
 * La lista è un semplice testo (formattato secondo i possibili tipi di raggruppamento) <br>
 * Usata fondamentalmente da UploadAttivita con appContext.getBean(ListaAttivita.class, attivita) <br>
 * <p>
 * Creata con appContext.getBean(ListaAttivita.class, attivita) per usare tutte le attività che hanno la stessa attivita.plurale <br>
 * Creata con appContext.getBean(ListaAttivita.class, attivita, AETypeAttivita.singolare) per usare solo la singola attività <br>
 * Creata con appContext.getBean(ListaAttivita.class, nomeAttivitaPlurale, AETypeAttivita.plurale) per usare tutte le singole attività <br>
 * Creata con appContext.getBean(ListaAttivita.class, nomeAttivitaSingolare, AETypeAttivita.singolare) per usare solo una singola attività <br>
 * Punto d'inizio @PostConstruct inizia() nella superclasse <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListaAttivita extends Lista {


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


    //--property
    private String nomeAttivita;

    //--property
    private Attivita attivita;

    //--property
    private List<String> listaNomiAttivitaSingole;

    //--property
    private AETypeAttivita typeAttivita;

    protected LinkedHashMap<String, List<WrapDidascalia>> mappaWrap;

    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(ListaAttivita.class, attivita) per usare tutte le attività che hanno la stessa attivita.plurale <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune properties. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     */
    public ListaAttivita() {
    }// end of constructor


    /**
     * Regolazioni iniziali per gestire (nella sottoclasse Attivita) i due costruttori:attività plurali o attività singola <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void regolazioniIniziali() {
        super.regolazioniIniziali();

        if (typeAttivita == null) {
            logger.info(new WrapLog().exception(new AlgosException("Manca la specifica singolare/plurale")).usaDb());
            return;
        }

        if (attivita != null) {
            switch (typeAttivita) {
                case singolare -> {
                    listaNomiAttivitaSingole = arrayService.creaArraySingolo(attivita.singolare);
                }
                case plurale -> {
                    try {
                        listaNomiAttivitaSingole = attivitaBackend.findSingolariByPlurale(attivita.plurale);
                    } catch (Exception unErrore) {
                        logger.error(new WrapLog().exception((unErrore)).usaDb());
                    }
                }
                default -> {
                }
            }
        }
        else {
            switch (typeAttivita) {
                case singolare -> {
                    try {
                        attivita = attivitaBackend.findFirstBySingolare(nomeAttivita);
                    } catch (Exception unErrore) {
                        logger.error(new WrapLog().exception((unErrore)).usaDb());
                    }
                    listaNomiAttivitaSingole = arrayService.creaArraySingolo(nomeAttivita);
                }
                case plurale -> {
                    try {
                        attivita = attivitaBackend.findFirstByPlurale(nomeAttivita);
                        listaNomiAttivitaSingole = attivitaBackend.findSingolariByPlurale(nomeAttivita);
                    } catch (Exception unErrore) {
                        logger.error(new WrapLog().exception((unErrore)).usaDb());
                    }
                }
                default -> {
                }
            }
        }
    }

    public ListaAttivita attivita(final Attivita attivita) {
        this.nomeAttivita = attivita.plurale;
        this.typeAttivita = AETypeAttivita.plurale;
        this.regolazioniIniziali();
        this.fixListaBio();
        return this;
    }
    public ListaAttivita singolare(final String attivitaSingolare) {
        this.nomeAttivita = attivitaSingolare;
        this.typeAttivita = AETypeAttivita.singolare;
        this.regolazioniIniziali();
        this.fixListaBio();
        return this;
    }
    public ListaAttivita plurale(final String attivitaPlurale) {
        this.nomeAttivita = attivitaPlurale;
        this.typeAttivita = AETypeAttivita.plurale;
        this.regolazioniIniziali();
        this.fixListaBio();
        return this;
    }

    /**
     * Costruisce una lista di biografie (Bio) che hanno una valore valido per la pagina specifica <br>
     * DEVE essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    @Override
    protected void fixListaBio() {
        super.fixListaBio();

        try {
            listaBio = bioService.fetchAttivita(listaNomiAttivitaSingole);
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb());
            return;
        }

        listaWrap = creaWrap(listaBio);
        TreeMap<String, List> mappa = creaMappaNazionalita(listaWrap);
        mappaDue = new TreeMap<>();
        TreeMap<String, List> lista;
        if (mappa != null) {
            for (String key : mappa.keySet()) {
                lista = creaMappaCarattere(mappa.get(key));
                mappaDue.put(key, lista);
            }
        }
        mappaDue = arrayService.sort(mappaDue);
        creaMappaTre(mappaDue);
    }

    public TreeMap<String, List> creaMappaNazionalita(List<WrapDidascalia> listaWrapNonOrdinata) {
        TreeMap<String, List> mappa = new TreeMap<>();
        List lista;
        String nazionalita = VUOTA;

        if (listaWrapNonOrdinata != null) {
            for (WrapDidascalia wrap : listaWrapNonOrdinata) {
                nazionalita = wrap.getNazionalitaParagrafo();
                nazionalita = nazionalita != null ? nazionalita : VUOTA;

                if (mappa.containsKey(nazionalita)) {
                    lista = mappa.get(nazionalita);
                }
                else {
                    lista = new ArrayList();
                }
                lista.add(wrap);
                mappa.put(nazionalita, lista);
            }
        }

        return arrayService.sort(mappa);
    }


    public TreeMap<String, List> creaMappaCarattere(List<WrapDidascalia> listaWrapNonOrdinata) {
        TreeMap<String, List> mappa = new TreeMap<>();
        List lista;
        String primoCarattere = VUOTA;

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

        return arrayService.sort(mappa);
    }

    public static Function<WrapDidascalia, String> cognome = wrap -> wrap.getCognome() != null ? wrap.getCognome() : VUOTA;

    public static Function<WrapDidascalia, String> wikiTitle = wrap -> wrap.getWikiTitle() != null ? wrap.getWikiTitle() : VUOTA;

    public TreeMap<String, TreeMap<String, List<String>>> creaMappaTre(TreeMap<String, TreeMap<String, List>> mappaDue) {
        TreeMap<String, TreeMap<String, List<String>>> mappa = new TreeMap<>();
        TreeMap<String, List> mappaSub;
        List<WrapDidascalia> listaWrap;
        List<String> listaDidascalia;
        String didascalia;

        if (mappaDue != null) {
            for (String key1 : mappaDue.keySet()) {
                mappaSub = mappaDue.get(key1);
                mappa.put(key1, new TreeMap<>());

                for (String key2 : mappaSub.keySet()) {
                    listaWrap = mappaSub.get(key2);
                    listaDidascalia = new ArrayList<>();
                    for (WrapDidascalia wrap : listaWrap) {
                        didascalia = didascaliaService.getLista(wrap.getBio());
                        listaDidascalia.add(didascalia);
                    }
                    mappa.get(key1).put(key2, listaDidascalia);
                }
            }
        }
        this.mappa = mappa;
        return mappa;
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

    public List<WrapDidascalia> sortByNazionalita(List<WrapDidascalia> listaWrapNonOrdinata) {
        List<WrapDidascalia> sortedList = new ArrayList<>();
        List<WrapDidascalia> listaConNazionalitaOrdinata = new ArrayList<>(); ;
        List<WrapDidascalia> listaSenzaNazionalitaOrdinata = new ArrayList<>(); ;

        listaConNazionalitaOrdinata = listaWrapNonOrdinata
                .stream()
                .filter(wrap -> textService.isValid(wrap.getNazionalitaSingola()))
                .sorted(Comparator.comparing(nazionalita))
                .collect(Collectors.toList());

        listaSenzaNazionalitaOrdinata = listaWrapNonOrdinata
                .stream()
                .filter(wrap -> textService.isEmpty(wrap.getNazionalitaSingola()))
                .collect(Collectors.toList());

        sortedList.addAll(listaConNazionalitaOrdinata);
        sortedList.addAll(listaSenzaNazionalitaOrdinata);
        return sortedList;
    }

    public static Function<WrapDidascalia, String> nazionalita = wrap -> wrap.getNazionalitaSingola() != null ? wrap.getNazionalitaSingola() : VUOTA;

    public List<String> getListaNomiAttivitaSingole() {
        return listaNomiAttivitaSingole;
    }

    public enum AETypeAttivita {
        singolare, plurale
    }

    public LinkedHashMap<String, List<WrapDidascalia>> getMappaWrap() {
        return mappaWrap;
    }

}
