package it.algos.vaad23.backend.service;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.entity.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
import org.bson.*;
import org.bson.conversions.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.*;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.*;

import javax.annotation.*;
import java.util.*;


/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: mar, 05-mag-2020
 * Time: 17:36
 * <p>
 * Classe di servizio per l'accesso al database <br>
 * Prioritario l'utilizzo di MongoOperations, inserito automaticamente da SpringBoot <br>
 * Per query più specifiche si può usare MongoClient <br>
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AAnnotationService.class); <br>
 * 3) @Autowired private AMongoService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MongoService<capture> extends AbstractService {


    /**
     * Versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    /**
     * Inietta da Spring
     */
    public MongoOperations mongoOp;


    private String databaseName;


    private MongoDatabase dataBase;

    private MongoClient mongoClient;

    private MongoCollection collection;

    private List<String> collezioni;


    /**
     * Costruttore @Autowired. <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * L' @Autowired implicito funziona SOLO per UN costruttore <br>
     * L' @Autowired esplicito è necessario per le classi di test <br>
     * Se ci sono DUE o più costruttori, va in errore <br>
     * Se ci sono DUE costruttori, di cui uno senza parametri, inietta quello senza parametri <br>
     */
    @Autowired
    public MongoService(MongoTemplate mongoOp, @Value("${spring.data.mongodb.database}") String databaseName) {
        this.mongoOp = mongoOp;
        this.databaseName = databaseName;
    }


    /**
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     * Se viene implementata una sottoclasse, passa di qui per ogni sottoclasse oltre che per questa istanza <br>
     * Se esistono delle sottoclassi, passa di qui per ognuna di esse (oltre a questa classe madre) <br>
     */
    @PostConstruct
    private void postConstruct() {
        fixProperties();
    }


    /**
     * Creazione iniziale di eventuali properties indispensabili per l'istanza <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Metodo private che NON può essere sovrascritto <br>
     */
    public void fixProperties() {
        MongoIterable<String> nomiCollezioni;
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/" + databaseName);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        mongoClient = MongoClients.create(mongoClientSettings);

        if (textService.isValid(databaseName)) {
            dataBase = mongoClient.getDatabase(databaseName);
        }

        if (dataBase != null) {
            collezioni = new ArrayList<>();
            nomiCollezioni = dataBase.listCollectionNames();
            for (String stringa : nomiCollezioni) {
                collezioni.add(stringa);
            }
        }
    }


    /**
     * Check the existence of a collection. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     *
     * @return true if the collection exist
     */
    public boolean isExistsCollection(final Class<? extends AEntity> entityClazz) {
        return isExistsCollection(entityClazz != null ? entityClazz.getSimpleName() : VUOTA);
    }


    /**
     * Check the existence of a collection. <br>
     *
     * @param collectionName corrispondente ad una collection sul database mongoDB
     *
     * @return true if the collection exist
     */
    public boolean isExistsCollection(final String collectionName) {
        if (textService.isEmpty(collectionName)) {
            logger.info(new WrapLog().exception(new AlgosException("Manca il nome della collection")).usaDb());
        }

        String shortName = fileService.estraeClasseFinale(collectionName).toLowerCase();
        return collezioni != null && collezioni.contains(shortName);
    }


    /**
     * Collection del database. <br>
     *
     * @param collectionName The name of the collection or view
     *
     * @return collection if exist
     */
    public MongoCollection<Document> getCollection(final String collectionName) {
        if (textService.isEmpty(collectionName)) {
            return null;
        }

        String shortName = fileService.estraeClasseFinale(collectionName);
        shortName = textService.primaMinuscola(shortName);

        if (textService.isValid(shortName)) {
            if (collezioni != null && collezioni.contains(shortName)) {
                return dataBase != null ? dataBase.getCollection(shortName) : null;
            }
            return null;
        }
        else {
            return null;
        }
    }

    /**
     * Database. <br>
     *
     * @return database
     */
    public MongoDatabase getDataBase() {
        return dataBase;
    }

    /**
     * Nome del database. <br>
     *
     * @return nome del database
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * Tutte le collezioni esistenti. <br>
     *
     * @return collezioni del database
     */
    public List<String> getCollezioni() {
        return collezioni;
    }


    /**
     * Conteggio delle entities di una collection <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     *
     * @return numero di entities totali
     */
    public int count(final Class entityClazz) {
        Long entities;
        String message;
        Query query = new Query();

        if (entityClazz == null) {
            message = "Manca la entityClazz";
            logger.info(new WrapLog().exception(new AlgosException(message)).usaDb());
            return 0;
        }
        if (!isExistsCollection(entityClazz)) {
            message = String.format("La entityClazz '%s' non ha una collection", entityClazz.getSimpleName());
            logger.info(new WrapLog().exception(new AlgosException(message)).usaDb());
            return 0;
        }

        entities = mongoOp.count(query, entityClazz);

        return entities > 0 ? entities.intValue() : 0;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setDataBase(MongoDatabase dataBase) {
        this.dataBase = dataBase;
    }

    public List<AEntity> query(Class<? extends AEntity> entityClazz) {
        List<AEntity> listaEntities;
        Query query = new Query();

        listaEntities = (List<AEntity>) mongoOp.find(query, entityClazz);

        return listaEntities;
    }

    public List<String> projectionString(Class<? extends AEntity> entityClazz, String property) {
        List<String> listaProperty = new ArrayList();
        collection = getCollection(textService.primaMinuscola(entityClazz.getSimpleName()));

        Bson projection = Projections.fields(Projections.include(property), Projections.excludeId());
        FindIterable<Document> documents = collection.find().projection(projection);

        for (var singolo : documents) {
            listaProperty.add(singolo.get(property, String.class));
        }
        return listaProperty;
    }

    public List<Long> projectionLong(Class<? extends AEntity> entityClazz, String property) {
        List<Long> listaProperty = new ArrayList();
        collection = getCollection(textService.primaMinuscola(entityClazz.getSimpleName()));

        Bson projection = Projections.fields(Projections.include(property), Projections.excludeId());
        FindIterable<Document> documents = collection.find().projection(projection);

        for (var singolo : documents) {
            listaProperty.add(singolo.get(property, Long.class));
        }
        return listaProperty;
    }
}