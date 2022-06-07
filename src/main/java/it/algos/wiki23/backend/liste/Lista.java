package it.algos.wiki23.backend.liste;

import it.algos.vaad23.backend.service.*;
import it.algos.wiki23.backend.packages.attivita.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.packages.nazionalita.*;
import it.algos.wiki23.backend.service.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.beans.factory.annotation.*;

import javax.annotation.*;
import java.util.*;

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
 * Sovrascritta nelle sottoclassi concrete <br>
 * Not annotated with @SpringComponent (sbagliato) perché è una classe astratta <br>
 * Punto d'inizio @PostConstruct inizia() nella superclasse <br>
 * <p>
 * La (List<Bio>) listaBio, la (List<String>) listaDidascalie, la (Map<String, List<String>>) mappa e (String) testoConParagrafi
 * vengono tutte regolate alla creazione dell'istanza in @PostConstruct e sono disponibili da subito <br>
 * Si può quindi usare la chiamata appContext.getBean(ListaXxx.class, yyy).getTestoConParagrafi() senza esplicitare l'istanza <br>
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
    public AttivitaBackend attivitaBackend;
    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public NazionalitaBackend nazionalitaBackend;

    /**
     * Lista delle biografie che hanno una valore valido per la pagina specifica <br>
     * La lista viene creata nel @PostConstruct dell'istanza <br>
     * La lista è ordinata per cognome <br>
     */
    protected List<Bio> listaBio;

    /**
     * Lista dei wrapper per gestire i dati necessari ad una didascalia <br>
     */
    protected List<WrapDidascalia> listaWrap;


    /**
     * Lista delle listaDidascalie che hanno una valore valido per la pagina specifica <br>
     * La lista viene creata nel @PostConstruct dell'istanza <br>
     * La lista è ordinata per cognome <br>
     */
    protected List<String> listaDidascalie;

    /**
     * Mappa delle didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) che corrisponde al titolo del paragrafo <br>
     * Ogni valore della mappa è costituito da una lista di didascalie per ogni paragrafo <br>
     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
     * La mappa viene creata nel @PostConstruct dell'istanza <br>
     */
    protected LinkedHashMap<String, List<String>> mappaUno;

    protected TreeMap<String, TreeMap<String, List>> mappaDue;
    protected TreeMap<String, TreeMap<String, List<String>>> mappa;

//    protected LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<String>>>> mappaTre;


    /**
     * Metodo invocato subito DOPO il costruttore
     * <p>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     * Se hanno la stessa firma, chiama prima @PostConstruct della sottoclasse <br>
     * Se hanno firme diverse, chiama prima @PostConstruct della superclasse <br>
     */
    @PostConstruct
    public void inizia() {
        this.fixPreferenze();
        this.regolazioniIniziali();
        this.fixListaBio();
        this.fixListaDidascalie();
        this.fixMappaParagrafi();
    }

    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
    }


    /**
     * Regolazioni iniziali per gestire (nella sottoclasse Attivita) i due costruttori:attività plurali o attività singola <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void regolazioniIniziali() {
    }

    /**
     * Costruisce una lista di biografie (Bio) che hanno una valore valido per la pagina specifica <br>
     * DEVE essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    protected void fixListaBio() {
    }


    /**
     * Costruisce una lista di didascalie (testo) che hanno una valore valido per la pagina specifica <br>
     * DEVE essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    protected void fixListaDidascalie() {
    }


    /**
     * Costruisce una mappa ordinata di didascalie suddivise per paragrafi <br>
     * DEVE essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    protected void fixMappaParagrafi() {
    }

    /**
     * Costruisce una lista dei wrapper per gestire i dati necessari ad una didascalia <br>
     */
    public List<WrapDidascalia> creaWrap(List<Bio> listaBio) {
        List<WrapDidascalia> listaWrap = null;

        if (listaBio != null) {
            listaWrap = new ArrayList<>();
            for (Bio bio : listaBio) {
                listaWrap.add(creaWrap(bio));
            }
        }

        return listaWrap;
    }

    public WrapDidascalia creaWrap(Bio bio) {
        WrapDidascalia wrap = null;
        wrap = new WrapDidascalia();

        wrap.setAttivitaSingola(bio.attivita);
        if (textService.isValid(bio.attivita) ) {
            wrap.setAttivitaParagrafo(attivitaBackend.findFirstBySingolare(bio.attivita).plurale);
        }
        wrap.setNazionalitaSingola(bio.nazionalita);
        if (textService.isValid(bio.nazionalita) ) {
            wrap.setNazionalitaParagrafo(nazionalitaBackend.findBySingolare(bio.nazionalita).plurale);
        }
        wrap.setWikiTitle(bio.wikiTitle);
        wrap.setNome(bio.nome);
        wrap.setCognome(bio.cognome);
        wrap.setPrimoCarattere(textService.isValid(bio.cognome)? bio.cognome.substring(0,1): bio.wikiTitle.substring(0,1));

        return wrap;
    }

    public List<Bio> getListaBio() {
        return listaBio;
    }

    public List<WrapDidascalia> getListaWrap() {
        return listaWrap;
    }

    public TreeMap<String, TreeMap<String, List>> getMappaDue() {
        return mappaDue;
    }
    public TreeMap<String, TreeMap<String, List<String>>> getMappa() {
        return mappa;
    }

}
