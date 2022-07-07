package it.algos.wiki23.backend.packages.nazionalita;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.wiki.*;
import it.algos.wiki23.backend.upload.*;
import it.algos.wiki23.backend.wrapper.*;
import it.algos.wiki23.wiki.query.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

/**
 * Project wiki
 * Created by Algos
 * User: gac
 * Date: lun, 25-apr-2022
 * Time: 18:21
 * <p>
 * Service di una entityClazz specifica e di un package <br>
 * Garantisce i metodi di collegamento per accedere al database <br>
 * Non mantiene lo stato di una istanza entityBean <br>
 * Mantiene lo stato della entityClazz <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, esiste già @Service) <br>
 */
@Service
public class NazionalitaBackend extends WikiBackend {


    public NazionalitaRepository repository;

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
    public NazionalitaBackend(@Autowired @Qualifier(TAG_NAZIONALITA) final MongoRepository crudRepository) {
        super(crudRepository, Nazionalita.class);
        this.repository = (NazionalitaRepository) crudRepository;
//        super.lastDownload = WPref.downloadNazionalita;
    }

    public Nazionalita creaIfNotExist(final String singolare, final String plurale) {
        return checkAndSave(newEntity(singolare, plurale));
    }

    public Nazionalita checkAndSave(final Nazionalita nazionalita) {
        return isExist(nazionalita.singolare) ? null : repository.insert(nazionalita);
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
    public Nazionalita newEntity() {
        return newEntity(VUOTA, VUOTA);
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param singolare di riferimento (obbligatorio, unico)
     * @param plurale   (facoltativo, non unico)
     *
     * @return la nuova entity appena creata (non salvata e senza keyID)
     */
    public Nazionalita newEntity(final String singolare, final String plurale) {
        return Nazionalita.builder()
                .singolare(textService.isValid(singolare) ? singolare : null)
                .plurale(textService.isValid(plurale) ? plurale : null)
                .build();
    }

    public List<Nazionalita> findAll() {
        return repository.findAll();
    }

    public List<String> findAllPlurali() {
        List<String> lista = new ArrayList<>();
        List<Nazionalita> listaAll = findNazionalitaDistinctByPlurali();

        for (Nazionalita nazionalita : listaAll) {
            lista.add(nazionalita.plurale);
        }

        return lista;
    }

    /**
     * Retrieves the first entity by a 'singular' property.
     * Cerca una singola entity con una query. <br>
     * Restituisce un valore valido ANCHE se esistono diverse entities <br>
     *
     * @param nazionalitaSingolare per costruire la query
     *
     * @return the FIRST founded entity
     */
    public Nazionalita findFirstBySingolare(final String nazionalitaSingolare) {
        return repository.findFirstBySingolare(nazionalitaSingolare);
    }

    /**
     * Retrieves the first entity by a 'plural' property.
     * Cerca una singola entity con una query. <br>
     * Restituisce un valore valido ANCHE se esistono diverse entities <br>
     *
     * @param nazionalitaPlurale per costruire la query
     *
     * @return the FIRST founded entity
     */
    public Nazionalita findFirstByPlurale(final String nazionalitaPlurale) {
        return repository.findFirstByPlurale(nazionalitaPlurale);
    }

    public List<Nazionalita> findNazionalitaDistinctByPlurali() {
        List<Nazionalita> lista = new ArrayList<>();
        Set<String> set = new HashSet();
        List<Nazionalita> listaAll = repository.findAll();

        for (Nazionalita nazionalita : listaAll) {
            if (set.add(nazionalita.plurale)) {
                lista.add(nazionalita);
            }
        }

        return lista;
    }

    /**
     * Pagine che esistono sul server wikipedia e che non superano la soglia prevista per le liste <br>
     * flag esistePagina=true <br>
     * flag superaSoglia=false <br>
     *
     * @return attività con liste da cancellare
     */
    public List<Nazionalita> findPagineDaCancellare() {
        List<Nazionalita> listaDaCancellare = new ArrayList<>();
        List<Nazionalita> listaPlurali = findNazionalitaDistinctByPlurali();

        for (Nazionalita nazionalita : listaPlurali) {
            if (nazionalita.esistePagina && !nazionalita.superaSoglia) {
                listaDaCancellare.add(nazionalita);
            }
        }

        return listaDaCancellare;
    }

    public List<Nazionalita> findAllByPlurale(final String plurale) {
        return repository.findAllByPluraleOrderBySingolareAsc(plurale);
    }

    public List<String> findSingolariByPlurale(final String plurale) {
        List<String> listaNomi = new ArrayList<>();
        List<Nazionalita> listaNazionalita = findAllByPlurale(plurale);

        for (Nazionalita nazionalita : listaNazionalita) {
            listaNomi.add(nazionalita.singolare);
        }

        return listaNomi;
    }


    public LinkedHashMap<String, List<String>> findMappaSingolariByPlurale() {
        LinkedHashMap<String, List<String>> mappa = new LinkedHashMap<>();
        List<String> lista;
        List<Nazionalita> listaAll = repository.findAll();
        String plurale;
        String singolare;

        for (Nazionalita nazionalita : listaAll) {
            plurale = nazionalita.plurale;
            singolare = nazionalita.singolare;

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
     * Legge la mappa di valori dal modulo di wiki <br>
     * Cancella la (eventuale) precedente lista di attività <br>
     * Elabora la mappa per creare le singole attività <br>
     * Integra le attività con quelle di genere <br>
     *
     * @param wikiTitle della pagina su wikipedia
     *
     * @return true se l'azione è stata eseguita
     */
    public void download(String wikiTitle) {
        String message;
        int size = 0;
        long inizio = System.currentTimeMillis();

        Map<String, String> mappa = wikiApiService.leggeMappaModulo(wikiTitle);

        if (mappa != null && mappa.size() > 0) {
            deleteAll();
            for (Map.Entry<String, String> entry : mappa.entrySet()) {
                if (creaIfNotExist(entry.getKey(), entry.getValue()) != null) {
                    size++;
                }
            }
        }
        else {
            message = String.format("Non sono riuscito a leggere da wiki il modulo %s", wikiTitle);
            logger.warn(new WrapLog().exception(new AlgosException(message)).usaDb());
        }

        super.fixDownload(inizio, wikiTitle, mappa.size(), size);
    }


    /**
     * Esegue un azione di elaborazione, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void elabora() {
        long inizio = System.currentTimeMillis();
        int numBio;
        int numSingolari;

        for (Nazionalita nazionalita : findAll()) {
            nazionalita.numBio = 0;
            update(nazionalita);
        }

        //--Spazzola tutte le nazionalità distinte plurali (circa 284)
        //--Per ognuna recupera le nazionalità singolari
        //--Per ognuna nazionalità singolare calcola quante biografie la usano
        //--Memorizza e registra il dato nella entityBean
        for (Nazionalita nazionalita : findNazionalitaDistinctByPlurali()) {
            numBio = 0;
            numSingolari = 0;

            for (String singolare : findSingolariByPlurale(nazionalita.plurale)) {
                numBio += bioBackend.countNazionalita(singolare);
                numSingolari++;
            }

            for (Nazionalita nazionalitaOK : findAllByPlurale(nazionalita.plurale)) {
                nazionalitaOK.numBio = numBio;
                nazionalitaOK.numSingolari = numSingolari;
                update(nazionalitaOK);
            }
        }

        super.fixElaboraSecondi(inizio, "nazionalità");
    }


    /**
     * Esegue un azione di upload, specifica del programma/package in corso <br>
     */
    public void uploadAll() {
        WResult result;
        long inizio = System.currentTimeMillis();
        int sottoSoglia = 0;
        int daCancellare = 0;
        int modificate = 0;
        int nonModificate = 0;
        List<String> listaPluraliUnici = findAllPlurali();
        this.fixNext();

        for (String pluraleNazionalita : listaPluraliUnici) {
            result = uploadPagina(pluraleNazionalita);
            if (result.isValido()) {
                if (result.isModificata()) {
                    modificate++;
                }
                else {
                    nonModificate++;
                }
            }
            else {
                sottoSoglia++;
                if (result.getErrorCode().equals(KEY_ERROR_CANCELLANDA)) {
                    daCancellare++;
                }
            }
        }
        super.fixUploadMinuti(inizio, sottoSoglia, daCancellare, nonModificate, modificate, "nazionalità");

        LocalDateTime adesso = LocalDateTime.now();
        adesso = adesso.plusDays(7);
        WPref.uploadNazionalitaPrevisto.setValue(adesso);
    }

    /**
     * Controlla l'esistenza della pagina wiki relativa a questa nazionalità (lista) <br>
     */
    public boolean esistePagina(String pluraleNazionalita) {
        String wikiTitle = "Progetto:Biografie/Nazionalità/" + textService.primaMaiuscola(pluraleNazionalita);
        return appContext.getBean(QueryExist.class).isEsiste(wikiTitle);
    }

    /**
     * Scrive una pagina definitiva sul server wiki <br>
     */
    public WResult uploadPagina(String pluraleNazionalitaMinuscola) {
        WResult result = WResult.errato();
        String message;
        int numVoci = bioBackend.countNazionalitaPlurale(pluraleNazionalitaMinuscola);
        String voci = textService.format(numVoci);
        String pluraleNazionalitaMaiuscola = textService.primaMaiuscola(pluraleNazionalitaMinuscola);
        int soglia = WPref.sogliaAttNazWiki.getInt();
        String wikiTitle = "Progetto:Biografie/Nazionalità/" + pluraleNazionalitaMaiuscola;

        if (numVoci > soglia) {
            result = appContext.getBean(UploadNazionalita.class).upload(pluraleNazionalitaMinuscola);
            if (result.isValido()) {
                if (result.isModificata()) {
                    message = String.format("Lista %s utilizzati in %s voci biografiche", pluraleNazionalitaMinuscola, voci);
                }
                else {
                    message = String.format("Nazionalità %s utilizzata in %s voci biografiche. %s", pluraleNazionalitaMinuscola, voci, result.getValidMessage());
                }
                if (Pref.debug.is()) {
                    logger.info(new WrapLog().message(message).type(AETypeLog.upload));
                }
            }
            else {
                logger.warn(new WrapLog().message(result.getErrorMessage()).type(AETypeLog.upload));
            }
        }
        else {
            message = String.format("La nazionalità %s ha solo %s voci biografiche e non raggiunge il numero necessario per avere una pagina dedicata", pluraleNazionalitaMinuscola, voci);
            if (Pref.debug.is()) {
                result.setErrorMessage(message).setValido(false);
                logger.info(new WrapLog().message(message).type(AETypeLog.upload));
            }
            if (esistePagina(pluraleNazionalitaMinuscola)) {
                result.setErrorCode(KEY_ERROR_CANCELLANDA);
                message = String.format("Esiste la pagina %s che andrebbe cancellata", wikiTitle);
                logger.warn(new WrapLog().message(message).type(AETypeLog.upload).usaDb());
            }
        }

        return result;
    }

    public void fixNext() {
        LocalDateTime adesso = LocalDateTime.now();
        LocalDateTime prossimo = adesso.plusDays(7);
        WPref.uploadNazionalitaPrevisto.setValue(prossimo);
    }

}// end of crud backend class
