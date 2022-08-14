package it.algos.wiki23.backend.packages.cognome;

import com.mongodb.client.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.service.*;
import it.algos.vaad23.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.packages.wiki.*;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.mongodb.core.query.*;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Wed, 10-Aug-2022
 * Time: 08:43
 * <p>
 * Service di una entityClazz specifica e di un package <br>
 * Garantisce i metodi di collegamento per accedere al database <br>
 * Non mantiene lo stato di una istanza entityBean <br>
 * Mantiene lo stato della entityClazz <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, esiste già @Service) <br>
 */
@Service
public class CognomeBackend extends WikiBackend {

    public CognomeRepository repository;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public MongoService mongoService;

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
    //@todo registrare eventualmente come costante in VaadCost il valore del Qualifier
    public CognomeBackend(@Autowired @Qualifier(TAG_COGNOME) final MongoRepository crudRepository) {
        super(crudRepository, Cognome.class);
        this.repository = (CognomeRepository) crudRepository;
    }

    public Cognome creaIfNotExist(final String cognomeTxt, int numBio) {
        return checkAndSave(newEntity(cognomeTxt, numBio));
    }

    public Cognome checkAndSave(final Cognome cognome) {
        return isExist(cognome.cognome) ? null : repository.insert(cognome);
    }

    public boolean isExist(final String cognome) {
        return repository.findFirstByCognome(cognome) != null;
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Cognome newEntity() {
        return newEntity(VUOTA,0);
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param cognomeTxt (obbligatorio, unico)
     * @param numBio     (obbligatorio, unico)
     *
     * @return la nuova entity appena creata (non salvata e senza keyID)
     */
    public Cognome newEntity(final String cognomeTxt, int numBio) {
        return Cognome.builder()
                .cognome(textService.isValid(cognomeTxt) ? cognomeTxt : null)
                .numBio(numBio)
                .build();
    }

    /**
     * Fetches all code of Prenome <br>
     *
     * @return all selected property
     */
    public List<String> fetchCognome() {
        List<String> lista = new ArrayList<>();
        List<Cognome> listaEntities = repository.findAll();

        for (Cognome cognome : listaEntities) {
            lista.add(cognome.cognome);
        }

        return lista;
    }


    /**
     * Cancella i cognomi esistenti <br>
     * Crea tutti i cognomi <br>
     * Controlla che ci siano almeno n voci biografiche per il singolo cognome <br>
     * Registra la entity <br>
     * Non registra la entity col cognomi mancante <br>
     */
    public void elabora() {
        long inizio = System.currentTimeMillis();
        int tot = 0;
        int cont = 0;

        //--Cancella tutte le entities della collezione
        deleteAll();

        DistinctIterable<String> listaCognomiDistinti = mongoService.mongoOp.getCollection("bio").distinct("cognome", String.class);
        for (String cognomeTxt : listaCognomiDistinti) {
            tot++;

            if (saveCognome(cognomeTxt)) {
                cont++;
            }
        }
        logger.info(new WrapLog().message(String.format("Ci sono %d cognomi distinti", tot)));
        //        super.setLastElabora(EATempo.minuti, inizio);
        //        logger.info("Creazione di " + text.format(cont) + " cognomi su un totale di " + text.format(tot) + " cognomi distinti. Tempo impiegato: " + date.deltaText(inizio));
    }


    /**
     * Registra il numero di voci biografiche che hanno il cognome indicato <br>
     */
    public boolean saveCognome(String cognomeTxt) {
        Cognome cognome = null;
        //--Soglia minima per creare una entity nella collezione Cognomi sul mongoDB
        int sogliaMongo = WPref.sogliaCognomiMongo.getInt();
        //--Soglia minima per creare una pagina sul server wiki
        int sogliaWiki = WPref.sogliaCognomiWiki.getInt();
        long numBio = 0;
        Query query = new Query();

        query.addCriteria(Criteria.where("cognome").is(cognomeTxt));
        numBio = mongoService.mongoOp.count(query, Bio.class);

        if (numBio >= sogliaMongo) {
            cognome = creaIfNotExist(cognomeTxt, (int) numBio);
        }

        return cognome != null;
    }

}// end of crud backend class
