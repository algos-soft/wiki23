package it.algos.wiki23.backend.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.wiki23.backend.packages.attivita.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.*;

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

    private String titoloGrezzo;


    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(ListaAttivita.class, attivita) per usare tutte le attività che hanno la stessa attivita.plurale <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune properties. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     *
     * @param attivita di cui recuperare le liste di tutte le singole attività di biografie
     */
    public ListaAttivita(final Attivita attivita) {
        this(attivita, AETypeAttivita.plurale);
    }// end of constructor


    /**
     * Costruttore alternativo con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(ListaAttivita.class, attivita, AETypeAttivita.singolare) per usare solo una singola attività <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune properties. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     *
     * @param attivita     di cui recuperare la lista di biografie
     * @param typeAttivita singolare o plurale
     */
    public ListaAttivita(final Attivita attivita, final AETypeAttivita typeAttivita) {
        this.attivita = attivita;
        this.typeAttivita = typeAttivita;
    }// end of constructor

    /**
     * Costruttore alternativo con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(ListaAttivita.class, nomeAttivitaPlurale, AETypeAttivita.plurale) per usare tutte le singole attività <br>
     * Uso: appContext.getBean(ListaAttivita.class, nomeAttivitaSingolare, AETypeAttivita.singolare) per usare solo una singola attività <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune properties. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     *
     * @param nomeAttivita singolare o plurale in funzione del flag
     * @param typeAttivita singola o plurale
     */
    public ListaAttivita(final String nomeAttivita, final AETypeAttivita typeAttivita) {
        this.nomeAttivita = nomeAttivita;
        this.typeAttivita = typeAttivita;
    }// end of constructor


    /**
     * Regolazioni iniziali per gestire (nella sottoclasse Attivita) i due costruttori:attività plurali o attività singola <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void regolazioniIniziali() {
        super.regolazioniIniziali();

        if (attivita != null) {
            switch (typeAttivita) {
                case singolare -> {
                    this.titoloGrezzo = attivita.singolare;
                    listaNomiAttivitaSingole = arrayService.creaArraySingolo(attivita.singolare);
                }
                case plurale -> {
                    try {
                        this.titoloGrezzo = attivita.plurale;
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
                        this.titoloGrezzo = attivita.singolare;
                    } catch (Exception unErrore) {
                        logger.error(new WrapLog().exception((unErrore)).usaDb());
                    }
                    listaNomiAttivitaSingole = arrayService.creaArraySingolo(nomeAttivita);
                }
                case plurale -> {
                    try {
                        attivita = attivitaBackend.findFirstByPlurale(nomeAttivita);
                        this.titoloGrezzo = attivita.plurale;
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
        }
    }


    public List<String> getListaNomiAttivitaSingole() {
        return listaNomiAttivitaSingole;
    }

    public enum AETypeAttivita {
        singolare, plurale
    }

}
