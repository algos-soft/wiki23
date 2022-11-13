package it.algos.wiki23.backend.packages.bio;

import com.mongodb.client.*;
import com.mongodb.client.model.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.packages.crono.anno.*;
import it.algos.vaad23.backend.packages.crono.giorno.*;
import it.algos.vaad23.backend.packages.crono.mese.*;
import it.algos.vaad23.backend.packages.crono.secolo.*;
import it.algos.vaad23.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.wiki.*;
import it.algos.wiki23.backend.wrapper.*;
import org.bson.*;
import org.bson.conversions.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;
import java.util.stream.*;

/**
 * Project wiki
 * Created by Algos
 * User: gac
 * Date: gio, 28-apr-2022
 * Time: 11:57
 * <p>
 * Service di una entityClazz specifica e di un package <br>
 * Garantisce i metodi di collegamento per accedere al database <br>
 * Non mantiene lo stato di una istanza entityBean <br>
 * Mantiene lo stato della entityClazz <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, esiste già @Service) <br>
 */
@Service
public class BioBackend extends WikiBackend {


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

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public MeseBackend meseBackend;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public SecoloBackend secoloBackend;

    public BioRepository repository;

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
    public BioBackend(@Autowired @Qualifier(TAG_BIO) final MongoRepository crudRepository) {
        super(crudRepository, Bio.class); this.repository = (BioRepository) crudRepository;
    }


    public Bio creaIfNotExist(final WrapBio wrap) {
        return checkAndSave(newEntity(wrap));
    }


    public Bio checkAndSave(Bio bio) {
        return isExist(bio.pageId) ? null : repository.insert(elaboraService.esegue(bio));
    }

    public boolean isExist(final long pageId) {
        return repository.findFirstByPageId(pageId) != null;
    }

    public Bio fixWrap(final String keyID, final WrapBio wrap) {
        Bio bio = newEntity(wrap); bio.setId(keyID); return bio;
    }

    public Bio newEntity() {
        return newEntity(0, VUOTA, VUOTA, null);
    }

    public Bio newEntity(final WrapBio wrap) {
        String message; if (wrap.isValida()) {
            return newEntity(wrap.getPageid(), wrap.getTitle(), wrap.getTemplBio());
        }
        else {
            message = "wrap non valido"; if (wrap.getPageid() < 1) {
                message = "Manca il pageid";
            } if (textService.isEmpty(wrap.getTitle())) {
                message = "Manca il wikiTitle";
            } logger.info(new WrapLog().exception(new AlgosException(message)).usaDb()); return null;
        }
    }

    public Bio newEntity(final long pageId, final String wikiTitle, final String tmplBio) {
        return newEntity(pageId, wikiTitle, tmplBio, null);
    }

    public Bio newEntity(final Document doc) {
        return Bio.builder()
                .pageId(doc.getLong("pageId"))
                .wikiTitle(doc.getString("wikiTitle"))
                .elaborato(doc.getBoolean("elaborato"))
                .nome(doc.getString("nome"))
                .cognome(doc.getString("cognome"))
                .ordinamento(doc.getString("ordinamento"))
                .sesso(doc.getString("sesso"))
                .giornoNato(doc.getString("giornoNato"))
                .giornoNatoOrd(doc.getInteger("giornoNatoOrd"))
                .annoNato(doc.getString("annoNato"))
                .annoNatoOrd(doc.getInteger("annoNatoOrd"))
                .luogoNato(doc.getString("luogoNato"))
                .luogoNatoLink(doc.getString("luogoNatoLink"))
                .giornoMorto(doc.getString("giornoMorto"))
                .giornoMortoOrd(doc.getInteger("giornoMortoOrd"))
                .annoMorto(doc.getString("annoMorto"))
                .annoMortoOrd(doc.getInteger("annoMortoOrd"))
                .luogoMorto(doc.getString("luogoMorto"))
                .luogoMortoLink(doc.getString("luogoMortoLink"))
                .attivita(doc.getString("attivita"))
                .attivita2(doc.getString("attivita2"))
                .attivita3(doc.getString("attivita3"))
                .nazionalita(doc.getString("nazionalita"))
                .build();
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param pageId     di riferimento (obbligatorio, unico)
     * @param wikiTitle  di riferimento (obbligatorio, unico)
     * @param tmplBio    (obbligatorio, unico)
     * @param lastServer sul server wiki (obbligatorio)
     *
     * @return la nuova entity appena creata (non salvata e senza keyID)
     */
    public Bio newEntity(final long pageId, final String wikiTitle, final String tmplBio, final LocalDateTime lastServer) {
        LocalDateTime now = LocalDateTime.now();
        return Bio.builder()
                .pageId(pageId)
                .wikiTitle(textService.isValid(wikiTitle) ? wikiTitle : null)
                .tmplBio(textService.isValid(tmplBio) ? tmplBio : null)
                .elaborato(false)
                .lastServer(lastServer != null ? lastServer : now)
                .lastMongo(now)
                .valido(true)
                .build();
    }

    public Bio insert(final Bio bio) {
        return repository.insert(bio);
    }

    @Override
    public Bio save(final Object entity) {
        Bio bioTemp;

        if (entity instanceof Bio bio) {

            if (textService.isEmpty(bio.id) && bio.pageId > 0) {
                bioTemp = findByKey(bio.pageId); if (bioTemp != null) {
                    bio.id = bioTemp.id;
                }
            }

            if (!bio.errato) {
                bio.errore = null;
            } if (bio.errore == null) {
                bio.errato = false;
            }

            if (isExist(bio.pageId)) {
                try {
                    repository.save(bio);
                } catch (Exception unErrore) {
                    logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb()); return null;
                }
            }
            else {
                try {
                    repository.insert(bio);
                } catch (Exception unErrore) {
                    logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb()); return null;
                }
            } return bio;
        }

        return null;
    }

    @Override
    public Bio update(Object entityBean) {
        if (entityBean instanceof Bio bio) {
            if (isExist(bio.pageId)) {
                repository.save(bio);
            }
            else {
                //                repository.insert(bio);
            } return null;
        }

        return null;
    }


    /**
     * Conta tutte le biografie con una serie di attività plurali. <br>
     *
     * @param attivitaPlurale
     *
     * @return conteggio di biografie che la usano
     */
    public int countAttivitaPlurale(final String attivitaPlurale) {
        int numBio = 0; List<String> listaNomi = attivitaBackend.findAllSingolariByPlurale(attivitaPlurale);

        for (String singolare : listaNomi) {
            numBio += countAttivitaSingola(singolare);
        }

        return numBio;
    }


    /**
     * Conta tutte le biografie con una serie di attività singolari. <br>
     *
     * @param attivitaSingola
     *
     * @return conteggio di biografie che la usano
     */
    public int countAttivitaAll(final String attivitaSingola) {
        int numBio = 0;
        Long attivitaUno = repository.countBioByAttivita(attivitaSingola);
        Long attivitaDue;
        Long attivitaTre;

        numBio = attivitaUno.intValue();

        if (WPref.usaTreAttivita.is()) {
            attivitaDue = repository.countBioByAttivita2(attivitaSingola);
            attivitaTre = repository.countBioByAttivita3(attivitaSingola);
            numBio += attivitaDue.intValue() + attivitaTre.intValue();
        }

        return numBio;
    }

    public int countAttivitaSingola(final String attivitaSingola) {
        Long numBio = textService.isValid(attivitaSingola) ? repository.countBioByAttivita(attivitaSingola) : 0;
        return numBio > 0 ? numBio.intValue() : 0;
    }


    public int countAttivitaDue(final String attivitaSingola) {
        Long numBio = repository.countBioByAttivita2(attivitaSingola);
        return numBio > 0 ? numBio.intValue() : 0;
    }

    public int countAttivitaTre(final String attivitaSingola) {
        Long numBio = repository.countBioByAttivita3(attivitaSingola);
        return numBio > 0 ? numBio.intValue() : 0;
    }

    public int countAttivitaNazionalitaBase(final String attivitaSingola, final String nazionalitaSingola) {
        Long numBio = repository.countBioByAttivitaAndNazionalita(attivitaSingola, nazionalitaSingola);
        return numBio > 0 ? numBio.intValue() : 0;
    }

    public int countAttivitaNazionalita(final String attivitaSingola, final String nazionalitaSingolarePlurale) {
        int numBio = 0; List<String> listaNazionalita = nazionalitaBackend.findAllSingolari(nazionalitaSingolarePlurale);

        for (String nazionalitaSingola : listaNazionalita) {
            numBio += countAttivitaNazionalitaBase(attivitaSingola, nazionalitaSingola);
        }

        return numBio;
    }

    /**
     * Conta tutte le biografie incrociate di un'attività plurale con una nazionalità plurale. <br>
     * L'attività può essere espressa direttamente come plurale oppure come singolare e ne viene ricavato il plurale <br>
     * La nazionalità può essere espressa direttamente come plurale oppure come singolare e ne viene ricavato il plurale <br>
     *
     * @param attivitaSingolarePlurale    da controllare/convertire in plurale
     * @param nazionalitaSingolarePlurale da controllare/convertire in plurale
     *
     * @return conteggio di biografie che usano l'attività e la nazionalità
     */
    public int countAttivitaNazionalitaAll(final String attivitaSingolarePlurale, final String nazionalitaSingolarePlurale) {
        return countAttivitaNazionalitaAll(attivitaSingolarePlurale, nazionalitaSingolarePlurale, VUOTA);
    }

    /**
     * Conta tutte le biografie incrociate di un'attività plurale con una nazionalità plurale. <br>
     * L'attività può essere espressa direttamente come plurale oppure come singolare e ne viene ricavato il plurale <br>
     * La nazionalità può essere espressa direttamente come plurale oppure come singolare e ne viene ricavato il plurale <br>
     *
     * @param attivitaSingolarePlurale    da controllare/convertire in plurale
     * @param nazionalitaSingolarePlurale da controllare/convertire in plurale
     * @param letteraIniziale             della (eventuale) sottoSottoPagina
     *
     * @return conteggio di biografie che usano l'attività e la nazionalità
     */
    public int countAttivitaNazionalitaAll(String attivitaSingolarePlurale, String nazionalitaSingolarePlurale, String letteraIniziale) {
        int numBio = 0;
        List<String> listaAttivita;
        List<String> listaNazionalita;
        String attivitaPlurale = attivitaBackend.pluraleBySingolarePlurale(attivitaSingolarePlurale);
        String nazionalitaPlurale = nazionalitaBackend.pluraleBySingolarePlurale(nazionalitaSingolarePlurale);

        listaAttivita = attivitaBackend.findAllSingolariByPlurale(attivitaPlurale);
        listaNazionalita = nazionalitaBackend.findSingolariByPlurale(nazionalitaPlurale);

        for (String nazionalitaSingola : listaNazionalita) {
            for (String attivitaSingola : listaAttivita) {
                numBio += countAttivitaNazionalitaBase(attivitaSingola, nazionalitaSingola);
            }
        }

        if (listaNazionalita.size() == 0 && nazionalitaSingolarePlurale.equalsIgnoreCase(TAG_LISTA_NO_NAZIONALITA)) {
            numBio = 0; for (String attivitaSingola : listaAttivita) {
                numBio += countAttivitaNazionalitaBase(attivitaSingola, null);
                List listone = bioService.fetchAttivita(attivitaSingola);
            }
        }

        if (textService.isValid(letteraIniziale)) {
            numBio = findAllAttivitaNazionalita(attivitaSingolarePlurale, nazionalitaSingolarePlurale, letteraIniziale).size();
        }

        return numBio;
    }


    public int countNazionalitaAttivita(final String nazionalita, final String attivitaSingolare) {
        Long numBio = repository.countBioByNazionalitaAndAttivita(nazionalita, attivitaSingolare); return numBio > 0 ? numBio.intValue() : 0;
    }


    /**
     * Conta tutte le biografie incrociate di una nazionalità plurale con un'attività plurale. <br>
     * L'attività può essere espressa direttamente come plurale oppure come singolare e ne viene ricavato il plurale <br>
     * La nazionalità può essere espressa direttamente come plurale oppure come singolare e ne viene ricavato il plurale <br>
     *
     * @param nazionalitaSingolarePlurale da controllare/convertire in plurale
     * @param attivitaSingolarePlurale    da controllare/convertire in plurale
     *
     * @return conteggio di biografie che usano l'attività e la nazionalità
     */
    public int countNazionalitaAttivitaAll(final String nazionalitaSingolarePlurale, final String attivitaSingolarePlurale) {
        return countNazionalitaAttivitaAll(nazionalitaSingolarePlurale, attivitaSingolarePlurale, VUOTA);
    }

    /**
     * Conta tutte le biografie incrociate di una nazionalità plurale con un'attività plurale. <br>
     * L'attività può essere espressa direttamente come plurale oppure come singolare e ne viene ricavato il plurale <br>
     * La nazionalità può essere espressa direttamente come plurale oppure come singolare e ne viene ricavato il plurale <br>
     *
     * @param nazionalitaSingolarePlurale da controllare/convertire in plurale
     * @param attivitaSingolarePlurale    da controllare/convertire in plurale
     * @param letteraIniziale             della (eventuale) sottoSottoPagina
     *
     * @return conteggio di biografie che usano l'attività e la nazionalità
     */
    public int countNazionalitaAttivitaAll(String nazionalitaSingolarePlurale, final String attivitaSingolarePlurale, String letteraIniziale) {
        int numBio = 0; List<String> listaNazionalita; List<String> listaAttivita;
        String nazionalitaPlurale = nazionalitaBackend.pluraleBySingolarePlurale(nazionalitaSingolarePlurale);
        String attivitaPlurale = attivitaBackend.pluraleBySingolarePlurale(attivitaSingolarePlurale);

        listaNazionalita = nazionalitaBackend.findSingolariByPlurale(nazionalitaPlurale);
        listaAttivita = attivitaBackend.findAllSingolariByPlurale(attivitaPlurale);

        for (String nazionalitaSingola : listaNazionalita) {
            for (String attivitaSingola : listaAttivita) {
                numBio += countNazionalitaAttivita(nazionalitaSingola, attivitaSingola);
            }
        }

        if (textService.isValid(letteraIniziale)) {
            numBio = findAllAttivitaNazionalita(attivitaSingolarePlurale, nazionalitaSingolarePlurale, letteraIniziale).size();
        }

        return numBio;
    }


    /**
     * Conta tutte le biografie con una serie di nazionalità plurali. <br>
     *
     * @param nazionalitaPlurale
     *
     * @return conteggio di biografie che la usano
     */
    public int countNazionalitaPlurale(final String nazionalitaPlurale) {
        int numBio = 0; List<String> listaNomi = nazionalitaBackend.findSingolariByPlurale(nazionalitaPlurale);

        for (String singolare : listaNomi) {
            numBio += countNazionalita(singolare);
        }

        return numBio;
    }

    /**
     * Conta tutte le biografie con una serie di nazionalita. <br>
     *
     * @param nazionalitaSingola singola
     *
     * @return conteggio di biografie che la usano
     */
    public int countNazionalita(final String nazionalitaSingola) {
        Long nazLong = repository.countBioByNazionalita(nazionalitaSingola); return nazLong.intValue();
    }


    public Bio findByKey(final long pageId) {
        return repository.findFirstByPageId(pageId);
    }

    public Bio findByTitle(final String wikiTitle) {
        return repository.findFirstByWikiTitle(wikiTitle);
    }

    public List<Long> findOnlyPageId() {
        return mongoService.projectionLong(Bio.class, "pageId");
    }

    public List<Bio> findAllAll() {
        return super.findAll();
    }

    public List<Bio> findAll() {
        return findSenzaTmpl();
    }

    public List<Bio> fetchErrori() {
        return repository.findAllByErrato(true);
    }

    public List<Bio> findAttivitaNazionalita(final String attivitaSingola, final String nazionalitaSingolarePlurale) {
        List<Bio> lista = new ArrayList<>(); List<String> listaNazionalita = nazionalitaBackend.findAllSingolari(nazionalitaSingolarePlurale);

        if (listaNazionalita != null) {
            for (String nazionalitaSingola : listaNazionalita) {
                lista.addAll(repository.findAllByAttivitaAndNazionalitaOrderByOrdinamento(attivitaSingola, nazionalitaSingola));
            }
        }
        else {
            if (nazionalitaSingolarePlurale.equals(TAG_LISTA_NO_NAZIONALITA)) {
                lista = repository.findAllByAttivitaOrderByOrdinamento(attivitaSingola);
                lista = lista.stream().filter(bio -> (textService.isEmpty(bio.nazionalita))).collect(Collectors.toList());
            }
        }

        return lista;
    }

    public List<Bio> findNazionalitaAttivita(final String nazionalitaSingolare, final String attivitaSingolare) {
        return repository.findAllByNazionalitaAndAttivitaOrderByOrdinamento(nazionalitaSingolare, attivitaSingolare);
    }


    /**
     * Recupera tutte le biografie incrociate di un'attività plurale con una nazionalità plurale. <br>
     * L'attività può essere espressa direttamente come plurale oppure come singolare e ne viene ricavato il plurale <br>
     * La nazionalità può essere espressa direttamente come plurale oppure come singolare e ne viene ricavato il plurale <br>
     *
     * @param attivitaSingolarePlurale    da controllare/convertire in plurale
     * @param nazionalitaSingolarePlurale da controllare/convertire in plurale
     *
     * @return Lista di biografie che usano l'attività e la nazionalità
     */
    public List<Bio> findAllAttivitaNazionalita(String attivitaSingolarePlurale, String nazionalitaSingolarePlurale) {
        return findAllAttivitaNazionalita(attivitaSingolarePlurale, nazionalitaSingolarePlurale, VUOTA);
    }

    /**
     * Recupera tutte le biografie incrociate di un'attività plurale con una nazionalità plurale. <br>
     * L'attività può essere espressa direttamente come plurale oppure come singolare e ne viene ricavato il plurale <br>
     * La nazionalità può essere espressa direttamente come plurale oppure come singolare e ne viene ricavato il plurale <br>
     *
     * @param attivitaSingolarePlurale    da controllare/convertire in plurale
     * @param nazionalitaSingolarePlurale da controllare/convertire in plurale
     * @param letteraIniziale             della (eventuale) sottoSottoPagina
     *
     * @return Lista di biografie che usano l'attività e la nazionalità
     */
    public List<Bio> findAllAttivitaNazionalita(String attivitaSingolarePlurale, String nazionalitaSingolarePlurale, String letteraIniziale) {
        List<Bio> lista = new ArrayList<>();
        List<String> listaAttivita;
        String attivitaPlurale = attivitaBackend.pluraleBySingolarePlurale(attivitaSingolarePlurale);

        listaAttivita = attivitaBackend.findAllSingolariByPlurale(attivitaPlurale);

        for (String attivitaSingola : listaAttivita) {
            lista.addAll(findAttivitaNazionalita(attivitaSingola, nazionalitaSingolarePlurale));
        }

        if (textService.isValid(letteraIniziale)) {
            lista = lista.stream().filter(bio -> (textService.isValid(bio.ordinamento) && bio.ordinamento.startsWith(letteraIniziale))).collect(Collectors.toList());
        }

        return bioService.sortByForzaOrdinamento(lista);
    }


    /**
     * Conta tutte le biografie incrociate di una nazionalità plurale con un'attività plurale. <br>
     * La nazionalità può essere espressa direttamente come plurale oppure come singolare e ne viene ricavato il plurale <br>
     * L'attività può essere espressa direttamente come plurale oppure come singolare e ne viene ricavato il plurale <br>
     *
     * @param nazionalitaSingolarePlurale da controllare/convertire in plurale
     * @param attivitaSingolarePlurale    da controllare/convertire in plurale
     * @param letteraIniziale             della (eventuale) sottoSottoPagina
     *
     * @return Lista di biografie che usano l'attività e la nazionalità
     */
    public List<Bio> findAllNazionalitaAttivita(String nazionalitaSingolarePlurale, String attivitaSingolarePlurale, String letteraIniziale) {
        List<Bio> lista = new ArrayList<>(); List<String> listaNazionalita; List<String> listaAttivita;
        String nazionalitaPlurale = nazionalitaBackend.pluraleBySingolarePlurale(nazionalitaSingolarePlurale);
        String attivitaPlurale = attivitaBackend.pluraleBySingolarePlurale(attivitaSingolarePlurale);

        listaNazionalita = nazionalitaBackend.findSingolariByPlurale(nazionalitaPlurale);
        listaAttivita = attivitaBackend.findAllSingolariByPlurale(attivitaPlurale);

        for (String nazionalitaSingola : listaNazionalita) {
            for (String attivitaSingola : listaAttivita) {
                lista.addAll(findNazionalitaAttivita(nazionalitaSingola, attivitaSingola));
            }
        }

        if (textService.isValid(letteraIniziale)) {
            lista = lista.stream().filter(bio -> (textService.isValid(bio.cognome) && bio.cognome.startsWith(letteraIniziale))).collect(Collectors.toList());
        }

        return bioService.sortByForzaOrdinamento(lista);
    }


    //
    //
    // GIORNO  NATO
    //
    //
    public int countGiornoNato(final String giornoNato) {
        Long giornoLong = textService.isValid(giornoNato) ? repository.countBioByGiornoNato(giornoNato) : 0;
        return giornoLong.intValue();
    }

    public int countGiornoNato(final int giornoNatoOrd) {
        Long giornoLong = giornoNatoOrd > 0 ? repository.countBioByGiornoNatoOrd(giornoNatoOrd) : 0;
        return giornoLong.intValue();
    }

    public List<Bio> findGiornoNato(String giornoNato) {
        Query query = queryGiornoNato(giornoNato);
        return query != null ? mongoService.mongoOp.find(query, Bio.class) : new ArrayList<>();
    }

    /**
     * Se il secolo è valido ->query base <br>
     * Se il secolo è null -> 0 <br>
     * Se il secolo è vuoto ->0 <br>
     * Se il secolo non corrisponde ad un Secolo ->0 <br>
     * Se il secolo è uguale a 'Senza anno specificato' -> query diversa <br>
     */
    public int countGiornoNatoSecolo(String giornoNato, String nomeSecolo) {
        Query query = queryGiornoNatoSecolo(giornoNato, nomeSecolo);
        return query != null ? ((Long) mongoService.mongoOp.count(query, Bio.class)).intValue() : 0;
    }

    public List<Bio> findGiornoNatoSecolo(String giornoNato, String nomeSecolo) {
        Query query = queryGiornoNatoSecolo(giornoNato, nomeSecolo);
        return query != null ? mongoService.mongoOp.find(query, Bio.class) : new ArrayList<>();
    }

    public Query queryGiornoNato(String giornoNato) {
        Query query = new Query();
        Sort sort;
        giornoNato = wikiUtility.fixPrimoMese(giornoNato);

        if (textService.isEmpty(giornoNato)) {
            return null;
        }
        if (giornoBackend.findByNome(giornoNato) == null) {
            return null;
        }

        query.addCriteria(Criteria.where("giornoNato").is(giornoNato));
        sort = Sort.by(Sort.Direction.ASC, "annoNatoOrd", "ordinamento");
        query.with(sort);

        return query;
    }

    /**
     * Se il giorno è null -> query null <br>
     * Se il giorno è vuoto -> query null <br>
     * Se il secolo è valido ->query base <br>
     * Se il secolo è null -> query null <br>
     * Se il secolo è vuoto -> query null <br>
     * Se il secolo non corrisponde ad un Secolo -> query null <br>
     * Se il secolo è uguale a 'Senza anno specificato' -> query diversa <br>
     */
    public Query queryGiornoNatoSecolo(String giornoNato, String nomeSecolo) {
        Query query;
        Secolo secolo;
        int annoIniziale = DELTA_ANNI;
        annoIniziale = 1000;
        int delta = DELTA_ORDINE_ANNI;
        int inizio = annoIniziale;
        int fine = annoIniziale;

        if (textService.isEmpty(giornoNato)) {
            return null;
        }
        if (textService.isEmpty(nomeSecolo)) {
            return null;
        }
        secolo = secoloBackend.findByNome(nomeSecolo);
        if (secolo == null && !nomeSecolo.equalsIgnoreCase(TAG_LISTA_NO_ANNO)) {
            return null;
        }

        query = queryGiornoNato(giornoNato);
        if (query == null) {
            return null;
        }

        if (secolo != null) {
            inizio += secolo.inizio;
            inizio *= delta;
            fine += secolo.fine;
            fine *= delta;
            query.addCriteria(Criteria.where("annoNatoOrd").gte(inizio).lte(fine));
        }
        else {
            query.addCriteria(Criteria.where("annoNatoOrd").is(0));
        }

        return query;
    }
    //
    // giorno nato end
    //


    //
    //
    // GIORNO  MORTO
    //
    //
    public int countGiornoMorto(final String giornoMorto) {
        Long giornoLong = textService.isValid(giornoMorto) ? repository.countBioByGiornoMorto(giornoMorto) : 0;
        return giornoLong.intValue();
    }

    public int countGiornoMorto(final int giornoMorto) {
        Long giornoLong = giornoMorto > 0 ? repository.countBioByGiornoMortoOrd(giornoMorto) : 0;
        return giornoLong.intValue();
    }


    public List<Bio> findGiornoMorto(String giornoMorto) {
        Query query = queryGiornoMorto(giornoMorto);
        return query != null ? mongoService.mongoOp.find(query, Bio.class) : new ArrayList<>();
    }


    public int countGiornoMortoSecolo(String giornoMorto, String nomeSecolo) {
        Query query = queryGiornoMortoSecolo(giornoMorto, nomeSecolo);
        return query != null ? ((Long) mongoService.mongoOp.count(query, Bio.class)).intValue() : 0;
    }

    public List<Bio> findGiornoMortoSecolo(String giornoMorto, String nomeSecolo) {
        Query query = queryGiornoMortoSecolo(giornoMorto, nomeSecolo);
        return query != null ? mongoService.mongoOp.find(query, Bio.class) : new ArrayList<>();
    }

    public Query queryGiornoMorto(String giornoMorto) {
        Query query = new Query(); Sort sort;
        giornoMorto = wikiUtility.fixPrimoMese(giornoMorto);

        if (textService.isEmpty(giornoMorto)) {
            return null;
        }
        if (giornoBackend.findByNome(giornoMorto) == null) {
            return null;
        }

        query.addCriteria(Criteria.where("giornoMorto").is(giornoMorto));
        sort = Sort.by(Sort.Direction.ASC, "annoMortoOrd", "ordinamento");
        query.with(sort);

        return query;
    }

    public Query queryGiornoMortoSecolo(String giornoMorto, String nomeSecolo) {
        Query query = new Query();
        Secolo secolo;
        int annoIniziale = DELTA_ANNI;
        annoIniziale = 1000;
        int delta = DELTA_ORDINE_ANNI;
        int inizio = annoIniziale;
        int fine = annoIniziale;

        if (textService.isEmpty(giornoMorto)) {
            return null;
        }
        if (textService.isEmpty(nomeSecolo)) {
            return null;
        }
        secolo = secoloBackend.findByNome(nomeSecolo);
        if (secolo == null && !nomeSecolo.equalsIgnoreCase(TAG_LISTA_NO_ANNO)) {
            return null;
        }

        query = queryGiornoMorto(giornoMorto);
        if (query == null) {
            return null;
        }

        if (secolo != null) {
            inizio += secolo.inizio;
            inizio *= delta;
            fine += secolo.fine;
            fine *= delta;
            query.addCriteria(Criteria.where("annoMortoOrd").gte(inizio).lte(fine));
        }
        else {
            query.addCriteria(Criteria.where("annoMortoOrd").is(0));
        }

        return query;
    }
    //
    // giorno morto end
    //


    //
    //
    // ANNO  NATO
    //
    //
    public int countAnnoNato(final String annoNato) {
        Long annoLong = textService.isValid(annoNato) ? repository.countBioByAnnoNato(annoNato) : 0;
        return annoLong.intValue();
    }


    public List<Bio> findAnnoNato(String annoNato) {
        Query query = queryAnnoNato(annoNato);
        return query != null ? mongoService.mongoOp.find(query, Bio.class) : new ArrayList<>();
    }

    public int countAnnoNatoMese(String annoNato, String nomeMese) {
        Query query = queryAnnoNatoMese(annoNato, nomeMese);
        return query != null ? ((Long) mongoService.mongoOp.count(query, Bio.class)).intValue() : 0;
    }

    public List<Bio> findAnnoNatoMese(String annoNato, String nomeMese) {
        Query query = queryAnnoNatoMese(annoNato, nomeMese);
        return query != null ? mongoService.mongoOp.find(query, Bio.class) : new ArrayList<>();
    }

    public Query queryAnnoNato(String annoNato) {
        Query query = new Query();
        Sort sort;

        if (textService.isEmpty(annoNato)) {
            return null;
        }
        if (annoBackend.findByNome(annoNato) == null) {
            return null;
        }

        query.addCriteria(Criteria.where("annoNato").is(annoNato));
        sort = Sort.by(Sort.Direction.ASC, "giornoNatoOrd", "ordinamento");
        query.with(sort);

        return query;
    }

    public Query queryAnnoNatoMese(String annoNato, String nomeMese) {
        Query query;
        Mese mese;

        if (textService.isEmpty(annoNato)) {
            return null;
        }
        if (textService.isEmpty(nomeMese)) {
            return null;
        }
        nomeMese = textService.primaMinuscola(nomeMese);
        mese = meseBackend.findByNome(nomeMese);
        if (mese == null && !nomeMese.equalsIgnoreCase(TAG_LISTA_NO_GIORNO)) {
            return null;
        }

        query = queryAnnoNato(annoNato);
        if (query == null) {
            return null;
        }

        if (mese != null) {
            nomeMese = textService.primaMinuscola(nomeMese);
            query.addCriteria(Criteria.where("giornoNato").regex(nomeMese + "$"));
        }
        else {
            query.addCriteria(Criteria.where("giornoNatoOrd").is(0));
        }

        return query;
    }
    //
    // anno nato end
    //

    //
    //
    // ANNO  MORTO
    //
    //

    public int countAnnoMorto(final String annoMorto) {
        Long annoLong = textService.isValid(annoMorto) ? repository.countBioByAnnoMorto(annoMorto) : 0;
        return annoLong.intValue();
    }

    public List<Bio> findAnnoMorto(String annoMorto) {
        Query query = queryAnnoMorto(annoMorto);
        return query != null ? mongoService.mongoOp.find(query, Bio.class) : new ArrayList<>();
    }


    public int countAnnoMortoMese(String annoMorto, String nomeMese) {
        Query query = queryAnnoMortoMese(annoMorto, nomeMese);
        return query != null ? ((Long) mongoService.mongoOp.count(query, Bio.class)).intValue() : 0;
    }

    public List<Bio> findAnnoMortoMese(String annoMorto, String nomeMese) {
        Query query = queryAnnoMortoMese(annoMorto, nomeMese);
        return query != null ? mongoService.mongoOp.find(query, Bio.class) : new ArrayList<>();
    }


    public Query queryAnnoMorto(String annoMorto) {
        Query query = new Query(); Sort sort;

        if (textService.isEmpty(annoMorto)) {
            return null;
        }
        if (annoBackend.findByNome(annoMorto) == null) {
            return null;
        }

        query.addCriteria(Criteria.where("annoMorto").is(annoMorto));
        sort = Sort.by(Sort.Direction.ASC, "giornoMortoOrd", "ordinamento");
        query.with(sort);

        return query;
    }

    public Query queryAnnoMortoMese(String annoMorto, String nomeMese) {
        Query query;
        Mese mese;

        if (textService.isEmpty(annoMorto)) {
            return null;
        }
        if (textService.isEmpty(nomeMese)) {
            return null;
        }
        nomeMese = textService.primaMinuscola(nomeMese);
        mese = meseBackend.findByNome(nomeMese);
        if (mese == null && !nomeMese.equalsIgnoreCase(TAG_LISTA_NO_GIORNO)) {
            return null;
        }

        query = queryAnnoMorto(annoMorto);
        if (query == null) {
            return null;
        }

        if (mese != null) {
            nomeMese = textService.primaMinuscola(nomeMese);
            query.addCriteria(Criteria.where("giornoMorto").regex(nomeMese + "$"));
        }
        else {
            query.addCriteria(Criteria.where("giornoMortoOrd").is(0));
        }

        return query;
    }
    //
    // anno morto end
    //


    //
    //
    // COGNOME
    //
    //
    public int countCognome(final String cognome) {
        Long numBio = textService.isValid(cognome) ? repository.countBioByCognome(cognome) : 0;
        return numBio.intValue();
    }

    public List<Bio> findCognome(String cognome) {
        Query query = queryCognome(cognome);
        return query != null ? mongoService.mongoOp.find(query, Bio.class) : new ArrayList<>();
    }

    public Query queryCognome(String cognome) {
        Query query = new Query(); Sort sort;

        if (textService.isEmpty(cognome)) {
            return null;
        }

        query.addCriteria(Criteria.where("cognome").is(cognome));
        sort = Sort.by(Sort.Direction.ASC, "ordinamento");
        query.with(sort);

        return query;
    }
    //
    // cognome end
    //


    public List<String> findAllWikiTitle() {
        return mongoService.projectionString(Bio.class, "wikiTitle");
    }


    public List<Long> findAllPageId() {
        return mongoService.projectionLong(Bio.class, "pageId");
    }

    public List<String> findAllCognomiDistinti() {
        // Lista di tutti i valori di una property
        List<String> cognomi = mongoService.projectionString(Bio.class, "cognome");

        // Lista dei valori distinct, non nulli e ordinati di una property
        cognomi = cognomi.stream().distinct().filter(cognome -> textService.isValid(cognome)).sorted().collect(Collectors.toList());

        return cognomi;
    }

    /**
     * Controlla l'esistenza della property <br>
     * La lista funziona anche se la property del sort è errata <br>
     * Ma ovviamente il sort non viene effettuato <br>
     *
     * @param sort
     */
    @Override
    public List<Bio> findAll(Sort sort) {
        if (sort == null) {
            return findSenzaTmpl();
        }
        else {
            return findSenzaTmpl(sort);
        }

    }

    public List<Bio> findSenzaTmpl() {
        return mongoService.projectionExclude(Bio.class, this, new Document("ordinamento", 1), "tmplBio");
    }

    public List<Bio> findSenzaTmpl(Sort sort) {
        Document doc = null;

        //        if (sort == null) {
        doc = new Document("ordinamento", 1);
        //        }

        return mongoService.projectionExclude(Bio.class, this, doc, "tmplBio");
    }

    public List<Bio> findSenzaTmpl(Document doc) {
        return mongoService.projectionExclude(Bio.class, this, doc, "tmplBio");
    }

    /**
     * Esegue un azione di elaborazione, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void elabora() {
        long inizio = System.currentTimeMillis(); List<Bio> lista = findAll(); int dim = 50000; int blocco = lista.size() / dim; int ini; int end; String size;
        String time; String message; int cont = 0; Bio bioTemp; Bio bioSaved;

        for (int k = 0; k <= blocco; k++) {
            ini = k * dim; end = Math.min(ini + dim, lista.size()); if (Pref.debug.is()) {
                logger.info(new WrapLog().message(String.format("Ini %s - End %s", ini, end)).type(AETypeLog.elabora));
            }

            for (Bio bio : lista.subList(ini, end)) {
                bioTemp = this.findByTitle(bio.wikiTitle);

                bioSaved = elaboraService.esegueSave(bioTemp); cont++;
            }

            if (Pref.debug.is()) {
                size = textService.format(cont); time = dateService.deltaText(inizio);
                message = String.format("Elaborate finora %s voci biografiche, in %s", size, time);
                logger.info(new WrapLog().message(message).type(AETypeLog.elabora));
            }
        } super.fixElaboraMinuti(inizio, "biografie");
    }

    /**
     * Esegue un azione di elaborazione, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void errori() {
        resetErrori(); fixErroriSesso(); fixMancaOrdinamento();
    }

    public void resetErrori() {
        mongoService.mongoOp.updateMulti(new Query(), Update.update("errato", false), Bio.class);
    }

    public int countErrori() {
        return ((Long) repository.countBioByErratoIsTrue()).intValue();
    }

    public int countSessoMancante() {
        return ((Long) repository.countBioBySessoIsNull()).intValue();
    }

    public int countSessoLungo() {
        Long lungo = null; Query query = new Query();

        query.addCriteria(Criteria.where("sesso").regex(".{2,}")); lungo = mongoService.mongoOp.count(query, Bio.class);

        return lungo != null ? lungo.intValue() : 0;
    }

    public int countSessoErrato() {
        Long lungo = null; Query query = new Query();

        query.addCriteria(Criteria.where("sesso").regex("[^M^F]{1}")); lungo = mongoService.mongoOp.count(query, Bio.class);

        return lungo != null ? lungo.intValue() : 0;
    }

    public int countOrdinamento() {
        return ((Long) repository.countBioByOrdinamentoIsNull()).intValue();
    }

    public List<Bio> getSessoLungo() {
        List<Bio> listaLunghe = null; Query query = new Query();

        query.addCriteria(Criteria.where("sesso").regex(".{2,}")); listaLunghe = mongoService.mongoOp.find(query, Bio.class);

        return listaLunghe;
    }

    public List<Bio> getSessoErrato() {
        List<Bio> listaLunghe = null; Query query = new Query();

        query.addCriteria(Criteria.where("sesso").regex("[^M^F]{1}")); listaLunghe = mongoService.mongoOp.find(query, Bio.class);

        return listaLunghe;
    }

    public int countNazionalitaGenere() {
        return ((Long) repository.countBioByErroreIs(AETypeBioError.nazionalitaGenere)).intValue();
    }

    public void fixErroriSesso() {
        int totali; int nulli; int maschi; int femmine; List<Bio> lista;

        totali = ((Long) repository.count()).intValue(); nulli = ((Long) repository.countBioBySessoIsNull()).intValue();
        maschi = ((Long) repository.countBioBySessoEquals("M")).intValue(); femmine = ((Long) repository.countBioBySessoEquals("F")).intValue();
        totali = nulli + maschi + femmine;

        lista = repository.findBySessoIsNull(); for (Bio bio : lista) {
            bio.errato = true; bio.errore = AETypeBioError.sessoMancante; save(bio);
        }

        lista = getSessoLungo(); for (Bio bio : lista) {
            bio.errato = true; bio.errore = AETypeBioError.sessoLungo; save(bio);
        }

        lista = getSessoErrato(); for (Bio bio : lista) {
            bio.errato = true; bio.errore = AETypeBioError.sessoErrato; save(bio);
        }
    }

    public void fixMancaOrdinamento() {
        List<Bio> lista;

        lista = repository.findByOrdinamentoIsNull(); for (Bio bio : lista) {
            bio.errato = true; bio.errore = AETypeBioError.mancaOrdinamento; save(bio);
        }
    }


    public List<Bio> findAllWikiTitlePageId() {
        List<Bio> listaBio = new ArrayList(); Bio bio; String wikiTitle = "wikiTitle"; String pageId = "pageId"; String message;
        MongoCollection collection = mongoService.getCollection("bio");

        if (collection == null) {
            message = String.format("Non esiste la collection", entityClazz.getSimpleName());
            logger.warn(new WrapLog().exception(new AlgosException(message)).usaDb()); return null;
        }

        Bson bsonSort = Sorts.ascending(wikiTitle); Bson projection = Projections.fields(Projections.include(wikiTitle, pageId), Projections.excludeId());
        var documents = collection.find().sort(bsonSort).projection(projection);

        for (var doc : documents) {
            bio = new Bio(); bio.wikiTitle = ((Document) doc).get(wikiTitle, String.class); bio.pageId = ((Document) doc).get(pageId, Long.class);
            listaBio.add(bio);
        } return listaBio;
    }


    public List<WrapTime> findAllWrapTime() {
        List<WrapTime> listaWrap = new ArrayList(); WrapTime wrap; String pageIdField = "pageId"; String wikiTitleField = "wikiTitle";
        String lastServerField = "lastServer"; long pageId; String wikiTitle; LocalDateTime lastServer = null; Date timeStamp; String message;
        MongoCollection collection = mongoService.getCollection("bio");

        if (collection == null) {
            message = String.format("Non esiste la collection", entityClazz.getSimpleName());
            logger.warn(new WrapLog().exception(new AlgosException(message)).usaDb()); return null;
        }

        Bson bsonSort = Sorts.ascending(wikiTitleField);
        Bson projection = Projections.fields(Projections.include(pageIdField, wikiTitleField, lastServerField), Projections.excludeId());
        var documents = collection.find().sort(bsonSort).projection(projection);

        for (var doc : documents) {
            pageId = ((Document) doc).get(pageIdField, Long.class); wikiTitle = ((Document) doc).get(wikiTitleField, String.class);
            timeStamp = ((Document) doc).get(lastServerField, Date.class); lastServer = dateService.dateToLocalDateTime(timeStamp);

            wrap = new WrapTime(pageId, wikiTitle, lastServer); listaWrap.add(wrap);
        } return listaWrap;
    }

}// end of crud backend class
