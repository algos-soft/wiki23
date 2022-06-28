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
import org.springframework.data.domain.*;
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


    public AttivitaRepository repository;


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


    public List<Attivita> findAttivitaDistinctByPlurali() {
        List<Attivita> lista = new ArrayList<>();
        Set<String> set = new HashSet();
        Sort sortOrder = Sort.by(Sort.Direction.ASC, "plurale");
        List<Attivita> listaAll = repository.findAll(sortOrder);

        for (Attivita attivita : listaAll) {
            if (set.add(attivita.plurale)) {
                lista.add(attivita);
            }
        }

        return lista;
    }

    public List<String> findAllPlurali() {
        List<String> lista = new ArrayList<>();
        List<Attivita> listaAll = findAttivitaDistinctByPlurali();

        for (Attivita attivita : listaAll) {
            lista.add(attivita.plurale);
        }

        return lista;
    }

    public boolean isExist(final String singolare) {
        return findFirstBySingolare(singolare) != null;
    }

    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria e unica) <br>
     *
     * @param singolare (obbligatorio, unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Attivita findFirstBySingolare(final String singolare) {
        return repository.findFirstBySingolare(singolare);
    }

    /**
     * Retrieves the first entity by a 'plural' property.
     * Cerca una singola entity con una query. <br>
     * Restituisce un valore valido ANCHE se esistono diverse entities <br>
     *
     * @param attivitaPlurale per costruire la query
     *
     * @return the FIRST founded entity
     */
    public Attivita findFirstByPlurale(final String attivitaPlurale) {
        return repository.findFirstByPlurale(attivitaPlurale);
    }


    public List<Attivita> findByPlurale(final String plurale) {
        return repository.findByPluraleOrderBySingolareAsc(plurale);
    }

    /**
     * Crea una lista di singolari che hanno lo stesso plurale. <br>
     *
     * @param attivitaPlurale da selezionare
     *
     * @return lista di singolari filtrati
     */
    public List<String> findSingolariByPlurale(final String attivitaPlurale) {
        List<String> listaNomi = new ArrayList<>();
        List<Attivita> listaAttivita = findByPlurale(attivitaPlurale);

        for (Attivita attivita : listaAttivita) {
            listaNomi.add(attivita.singolare);
        }

        return listaNomi;
    }


    public LinkedHashMap<String, List<String>> findMappaSingolariByPlurale() {
        LinkedHashMap<String, List<String>> mappa = new LinkedHashMap<>();
        List<String> lista;
        List<Attivita> listaAll = repository.findAll();
        String plurale;
        String singolare;

        for (Attivita attivita : listaAll) {
            plurale = attivita.plurale;
            singolare = attivita.singolare;

            if (mappa.get(plurale) == null) {
                lista = new ArrayList<>();
                lista.add(singolare);
                mappa.put(plurale, lista);
            }
            else {
                lista = mappa.get(plurale);
                lista.add(singolare);
                mappa.put(plurale, lista);
            }
        }

        return mappa;
    }

    /**
     * Conta il totale delle voci bio per tutte le attività associate a quella indicata. <br>
     * Recupera l'attività plurale e quindi tutte le attività singole associate <br>
     *
     * @param attivitaSingolare selezionata
     *
     * @return totale di voci biografiche interessate
     */
    public int contBio(final Attivita attivitaSingolare) {
        int numBio = 0;
        List<Attivita> lista = this.findByPlurale(attivitaSingolare.plurale);

        for (Attivita attivita : lista) {
            numBio += attivita.numBio;
        }

        return numBio;
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
                    entity = findFirstBySingolare(attivitaSingolare);
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

    /**
     * Esegue un azione di elaborazione, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void elabora() {
        long inizio = System.currentTimeMillis();
        int numBio;
        int numSingolari;

        for (Attivita attivita : findAll()) {
            attivita.numBio = 0;
            update(attivita);
        }

        //--Spazzola tutte le attività distinte plurali (circa 657)
        //--Per ognuna recupera le attività singolari
        //--Per ognuna attività singolare calcola quante biografie la usano (in 1 o 3 parametri)
        //--Memorizza e registra il dato nella entityBean
        for (Attivita attivita : findAttivitaDistinctByPlurali()) {
            numBio = 0;
            numSingolari = 0;

            for (String singolare : findSingolariByPlurale(attivita.plurale)) {
                numBio += bioBackend.countAttivita(singolare);
                numSingolari++;
            }

            for (Attivita attivitaOK : findByPlurale(attivita.plurale)) {
                attivitaOK.numBio = numBio;
                attivitaOK.numSingolari = numSingolari;
                update(attivitaOK);
            }
        }

        super.fixElaboraSecondi(inizio, "attività");
    }

}// end of crud backend class
