package it.algos.wiki23.backend.packages.attivita;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.wiki23.backend.boot.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.packages.genere.*;
import it.algos.wiki23.backend.packages.wiki.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 18-apr-2022
 * Time: 21:25
 * <p>
 * Service di una entityClazz specifica e di un package <br>
 * Garantisce i metodi di collegamento per accedere al database <br>
 * Non mantiene lo stato di una istanza entityBean <br>
 * Mantiene lo stato della entityClazz <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, esiste già @Service) <br>
 */
@Service
public class AttivitaBackend extends WikiBackend {


    private AttivitaRepository repository;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public BioBackend bioBackend;

    /**
     * Costruttore @Autowired (facoltativo) @Qualifier (obbligatorio) <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * Si usa un @Qualifier(), per specificare la classe che incrementa l'interfaccia repository <br>
     * Si usa una costante statica, per essere sicuri di scriverla uguale a quella di xxxRepository <br>
     * Regola la classe di persistenza dei dati specifica e la passa al costruttore della superclasse <br>
     * Regola la entityClazz (final nella superclasse) associata a questo service <br>
     *
     * @param crudRepository per la persistenza dei dati
     */
    public AttivitaBackend(@Autowired @Qualifier(TAG_ATTIVITA) final MongoRepository crudRepository) {
        super(crudRepository, Attivita.class);
        this.repository = (AttivitaRepository) crudRepository;
    }

    public Attivita creaIfNotExist(final String singolare, final String plurale, final boolean aggiunta) {
        return checkAndSave(newEntity(singolare, plurale, aggiunta));
    }

    public Attivita checkAndSave(final Attivita attivita) {
        return isExist(attivita.singolare) ? null : repository.insert(attivita);
    }

    public boolean isExist(final String singolare) {
        return repository.findFirstBySingolare(singolare) != null;
    }


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Attivita newEntity() {
        return newEntity(VUOTA, VUOTA, true);
    }


    /**
     * Creazione in memoria di una nuova entityBean che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @param singolare di riferimento (obbligatorio, unico)
     * @param plurale   (facoltativo, non unico)
     * @param aggiunta  flag (facoltativo, di default false)
     *
     * @return la nuova entityBean appena creata (non salvata)
     */
    public Attivita newEntity(final String singolare, final String plurale, final boolean aggiunta) {
        return Attivita.builder()
                .singolare(textService.isValid(singolare) ? singolare : null)
                .plurale(textService.isValid(plurale) ? plurale : null)
                .aggiunta(aggiunta)
                .build();
    }

    public List<Attivita> findAll() {
        return repository.findAll();
    }

    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria e unica) <br>
     *
     * @param singolare (obbligatorio, unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Attivita findBySingolare(final String singolare) {
        return repository.findFirstBySingolare(singolare);
    }


    /**
     * Legge la mappa di valori dal modulo di wiki <br>
     * Cancella la (eventuale) precedente lista di attività <br>
     * Elabora la mappa per creare le singole attività <br>
     * Integra le attività con quelle di genere <br>
     *
     * @param wikiTitle della pagina su wikipedia
     *
     * @return true se l'azione è stata eseguita
     */
    public void download(final String wikiTitle) {
        long inizio = System.currentTimeMillis();
        int size = 0;

        Map<String, String> mappa = wikiApiService.leggeMappaModulo(wikiTitle);

        if (mappa != null && mappa.size() > 0) {
            deleteAll();
            for (Map.Entry<String, String> entry : mappa.entrySet()) {
                if (creaIfNotExist(entry.getKey(), entry.getValue(), false) != null) {
                    size++;
                }
            }
        }
        else {
            message = String.format("Non sono riuscito a leggere da wiki il modulo %s", wikiTitle);
            logger.warn(new WrapLog().exception(new AlgosException(message)).usaDb());
            return;
        }

        super.fixDownload(inizio, wikiTitle, mappa.size(), size);
        aggiunge();
    }

    /**
     * Esegue un azione di elaborazione, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void elabora() {
        long inizio = System.currentTimeMillis();
        int numBio = 0;

        //--Spazzola tutte le attività
        //--Per ognuna calcola quante biografie usano l'attività
        //--Memorizza e registra il dato nella entityBean
        for (Attivita attivita : findAll()) {
            numBio = bioBackend.countAttivita(attivita.singolare);
            attivita.bio = numBio;
            update(attivita);
        }

        super.fixElabora(inizio, "attività");
    }


    /**
     * Aggiunge le ex-attività NON presenti nel modulo 'Modulo:Bio/Plurale attività' <br>
     * Le recupera dal modulo 'Modulo:Bio/Plurale attività genere' <br>
     * Le aggiunge se trova la corrispondenza tra il nome con e senza EX <br>
     */
    private void aggiunge() {
        List<Genere> listaEx = genereBackend.findStartingEx();
        String attivitaSingolare;
        String genereSingolare;
        Attivita entity;
        String message;
        int size = 0;

        if (listaEx == null || listaEx.size() == 0) {
            message = "Il modulo genere deve essere scaricato PRIMA di quello di attività";
            logger.warn(new WrapLog().exception(new AlgosException(message)).usaDb());
            return;
        }

        if (listaEx != null) {
            for (Genere genere : listaEx) {
                entity = null;
                attivitaSingolare = VUOTA;
                genereSingolare = genere.singolare;

                if (genereSingolare.startsWith(TAG_EX)) {
                    attivitaSingolare = genereSingolare.substring(TAG_EX.length());
                }
                if (genereSingolare.startsWith(TAG_EX2)) {
                    attivitaSingolare = genereSingolare.substring(TAG_EX2.length());
                }

                if (textService.isValid(attivitaSingolare)) {
                    entity = findBySingolare(attivitaSingolare);
                }

                if (entity != null) {
                    if (creaIfNotExist(genereSingolare, entity.plurale, true) != null) {
                        size++;
                    }
                    else {
                        logger.info(new WrapLog().message(genereSingolare));
                    }
                }
            }
        }
        message = String.format("Aggiunte %s ex-attività dalla collection genere", textService.format(size));
        logger.info(new WrapLog().message(message));
    }


}// end of crud backend class
