package it.algos.vaad24.backend.logic;

import it.algos.vaad24.backend.entity.*;
import it.algos.vaad24.backend.enumeration.*;
import it.algos.vaad24.backend.exception.*;
import it.algos.vaad24.backend.service.*;
import it.algos.vaad24.backend.wrapper.*;
import org.bson.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.*;

import java.util.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: gio, 10-mar-2022
 * Time: 21:02
 * Layer di collegamento del backend con mongoDB <br>
 * Classe astratta di servizio per la Entity di un package <br>
 * Le sottoclassi concrete sono SCOPE_SINGLETON e non mantengono dati <br>
 * L'unico dato mantenuto nelle sottoclassi concrete: la property final entityClazz <br>
 * Se la sottoclasse xxxService non esiste (non è indispensabile), usa la classe generica GenericService; i metodi esistono ma occorre un
 * cast in uscita <br>
 */
public abstract class CrudBackend extends AbstractService {

    /**
     * The Entity Class  (obbligatoria sempre e final)
     */
    public final Class<? extends AEntity> entityClazz;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ResourceService resourceService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public MongoService mongoService;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public TextService textService;

    public MongoRepository crudRepository;


    /**
     * Constructor @Autowired. <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * L' @Autowired (esplicito o implicito) funziona SOLO per UN costruttore <br>
     * Se ci sono DUE o più costruttori, va in errore <br>
     * Se ci sono DUE costruttori, di cui uno senza parametri, inietta quello senza parametri <br>
     */
    public CrudBackend(final MongoRepository crudRepository, final Class<? extends AEntity> entityClazz) {
        this.crudRepository = crudRepository;
        this.entityClazz = entityClazz;
    }// end of constructor with @Autowired


    public AEntity newEntity(Document doc) {
        return null;
    }


    /**
     * Creazione in memoria di una nuova entityBean che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Senza properties per compatibilità con la superclasse <br>
     *
     * @return la nuova entityBean appena creata (non salvata)
     */
    public AEntity newEntity() {
        AEntity newEntityBean = null;

        try {
            newEntityBean = entityClazz.getDeclaredConstructor().newInstance();
        } catch (Exception unErrore) {
            logger.warn(AETypeLog.nuovo, unErrore);
        }

        return newEntityBean;
    }


    public List findAll() {
        return crudRepository.findAll();
    }

    /**
     * Controlla l'esistenza della property <br>
     * La lista funziona anche se la property del sort è errata <br>
     * Ma ovviamente il sort non viene effettuato <br>
     */
    public List findAll(Sort sort) {
        boolean esiste;
        Sort.Order order;
        String property;
        String message;

        if (sort == null) {
            return crudRepository.findAll();
        }
        else {
            if (sort.stream().count() == 1) {
                order = sort.stream().toList().get(0);
                property = order.getProperty();
                esiste = reflectionService.isEsiste(entityClazz, property);
                if (esiste) {
                    return crudRepository.findAll(sort);
                }
                else {
                    message = String.format("Non esiste la property %s per l'ordinamento della classe %s", property, entityClazz.getSimpleName());
                    logger.warn(new WrapLog().exception(new AlgosException(message)).usaDb());
                    return crudRepository.findAll();
                }
            }
            else {
                return crudRepository.findAll(sort);
            }
        }
    }

    public AEntity add(Object objEntity) {
        AEntity entity = (AEntity) objEntity;

        return (AEntity) crudRepository.insert(entity);
    }

    public AEntity save(Object entity) {
        return (AEntity) crudRepository.save(entity);
    }

    public AEntity update(Object entity) {
        return (AEntity) crudRepository.save(entity);
    }

    public void delete(Object entity) {
        try {
            crudRepository.delete(entity);
        } catch (Exception unErrore) {
            logger.error(unErrore);
        }
    }

    public boolean deleteAll() {
        try {
            crudRepository.deleteAll();
        } catch (Exception unErrore) {
            logger.error(unErrore);
        }

        return crudRepository.count() == 0;
    }

    public int count() {
        return ((Long) crudRepository.count()).intValue();
    }

    public List findByDescrizione(final String value) {
        return null;
    }

    public List findByDescrizioneAndLivelloAndType(final String value, final AENotaLevel level, final AETypeLog type) {
        return null;
    }

    public List findByDescrizioneAndType(final String value, final AETypeLog type) {
        return null;
    }


    /**
     * Creazione di alcuni dati <br>
     * Viene invocato dal bottone Reset della lista o dalla UtilityView <br>
     * Può essere invocato IN PARTICOLARI CONDIZIONI alla creazione del programma <br>
     * La collezione viene svuotata <br>
     * Non deve essere sovrascritto <br>
     */
    public AResult resetForcing() {
        AResult result = AResult.build();
        String message;

        if (mongoService.isCollectionNullOrEmpty(entityClazz)) {
            return resetOnlyEmpty().method("resetForcing");
        }
        else {
            if (deleteAll()) {
                message = String.format("La collection [%s] esisteva ma è stata cancellata e i dati sono stati ricreati.", entityClazz.getSimpleName().toLowerCase());
                result = resetOnlyEmpty().method("resetForcing").validMessage(message).addValidMessage(String.format(" %d elementi.", count()));
                return result;
            }
            else {
                message = String.format("Non sono riuscito a cancellare la collection [%s]", entityClazz.getSimpleName().toLowerCase());
                return result.errorMessage(message);
            }
        }
    }


    /**
     * Creazione di alcuni dati <br>
     * Esegue SOLO se la collection NON esiste oppure esiste ma è VUOTA <br>
     * Viene invocato alla creazione del programma <br>
     * I dati possono essere presi da una Enumeration, da un file CSV locale, da un file CSV remoto o creati hardcoded <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public AResult resetOnlyEmpty() {
        AResult result = AResult.build().method("resetOnlyEmpty").target(entityClazz.getSimpleName());
        String message;

        if (mongoService.isCollectionNullOrEmpty(entityClazz)) {
            message = String.format("La collection [%s] era vuota e sono stati creati i nuovi dati.", entityClazz.getSimpleName().toLowerCase());
            return result.validMessage(message);
        }
        else {
            message = String.format("La collection [%s] esisteva già, non era vuota e non è stata toccata.", entityClazz.getSimpleName().toLowerCase());
            return result.errorMessage(message).intValue(count());
        }
    }


    public AResult fixResult(AResult result) {
        String message = String.format(" %d elementi.", count());
        return result.addValidMessage(message).intValue(count());
    }

    /**
     * Esegue un azione di download, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, senza invocare il metodo della superclasse <br>
     */
    public void download() {
    }

    /**
     * Esegue un azione di download, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, senza invocare il metodo della superclasse <br>
     *
     * @param wikiTitle della pagina sul web
     */
    public void download(final String wikiTitle) {
    }

}
