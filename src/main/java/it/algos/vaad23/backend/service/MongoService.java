package it.algos.vaad23.backend.service;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import com.mongodb.client.result.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.entity.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.logic.*;
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

    public static final String CONNECTION_STRING = "mongodb://localhost:27017/";

    public static final int STANDARD_MONGO_MAX_BYTES = 33554432;

    public static final int EXPECTED_ALGOS_MAX_BYTES = 50151432;

    /**
     * Versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;

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
        dataBase = getDB(databaseName);
        if (dataBase != null) {
            collezioni = new ArrayList<>();
            nomiCollezioni = dataBase.listCollectionNames();
            for (String stringa : nomiCollezioni) {
                collezioni.add(stringa);
            }
        }
    }

    /**
     * Restituisce un generico database
     */
    public MongoDatabase getDB(String databaseName) {
        ConnectionString connectionString = new ConnectionString(CONNECTION_STRING + databaseName);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        mongoClient = MongoClients.create(mongoClientSettings);

        return mongoClient.getDatabase(databaseName);
    }


    /**
     * Restituisce il database 'admin' di servizio, sempre presente in MongoDB
     */
    public MongoDatabase getDBAdmin() {
        return getDB("admin");
    }

    /**
     * Recupera il valore di un parametro
     *
     * @param parameterName
     *
     * @return value of parameter
     */
    public Object getParameter(String parameterName) {
        Map<String, Object> mappa;
        MongoDatabase dbAdmin = getDBAdmin();
        Document param;

        mappa = new LinkedHashMap<>();
        mappa.put("getParameter", 1);
        mappa.put(parameterName, 1);

        param = dbAdmin.runCommand(new Document(mappa));

        return param.get(parameterName);
    }

    /**
     * Regola il valore di un parametro
     *
     * @param parameterName
     * @param valueToSet    valore da regolare
     *
     * @return true se il valore è stato acquisito dal database
     */
    public boolean setParameter(String parameterName, Object valueToSet) {
        Map<String, Object> mappa;
        MongoDatabase dbAdmin = getDBAdmin();
        Document param;

        mappa = new LinkedHashMap<>();
        mappa.put("setParameter", 1);
        mappa.put(parameterName, valueToSet);

        param = dbAdmin.runCommand(new Document(mappa));
        return (double) param.get("ok") == 1;
    }

    /**
     * Regola il valore del parametro internalQueryExecMaxBlockingSortBytes
     *
     * @param maxBytes da regolare
     *
     * @return true se il valore è stato acquisito dal database
     */
    public boolean setMaxBlockingSortBytes(int maxBytes) {
        return setParameter("internalQueryExecMaxBlockingSortBytes", maxBytes);
    }

    /**
     * Recupera il valore del parametro internalQueryExecMaxBlockingSortBytes
     *
     * @return value of parameter
     */
    public int getMaxBlockingSortBytes() {
        int numBytes;
        String value = "";
        String message;
        logger.info(new WrapLog().message(VUOTA).type(AETypeLog.setup));

        numBytes = (int) getParameter("internalQueryExecMaxBlockingSortBytes");
        value = textService.format(numBytes);

        if (numBytes == STANDARD_MONGO_MAX_BYTES) {
            message = String.format("Algos - mongoDB. La variabile internalQueryExecMaxBlockingSortBytes è regolata col valore standard iniziale " +
                    "settato da mongoDB: %s", value);
            logger.info(new WrapLog().message(message).type(AETypeLog.setup));
        }
        else {
            if (numBytes == EXPECTED_ALGOS_MAX_BYTES) {
                message = String.format("Algos - mongoDB. La variabile internalQueryExecMaxBlockingSortBytes è regolata col valore " +
                        "richiesto da Algos: %s", value);
                logger.info(new WrapLog().message(message).type(AETypeLog.setup));
            }
            else {
                message = String.format("Algos - mongoDB. La variabile internalQueryExecMaxBlockingSortBytes è regolata a cazzo: ", value);
                logger.info(new WrapLog().message(message).type(AETypeLog.setup));
            }
        }

        logger.info(new WrapLog().message(VUOTA).type(AETypeLog.setup));
        return numBytes;
    }

    /**
     * Regola il valore iniziale del parametro internalQueryExecMaxBlockingSortBytes
     */
    public void fixMaxBytes() {
        this.setMaxBlockingSortBytes(EXPECTED_ALGOS_MAX_BYTES);
    }


    /**
     * Controlla che la collezione sia vuota. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     *
     * @return true if the collection is null or empty
     */
    public boolean isCollectionNullOrEmpty(final Class<? extends AEntity> entityClazz) {
        return entityClazz == null ? false : isCollectionNullOrEmpty(textService.primaMinuscola(entityClazz.getSimpleName()));
    }

    /**
     * Controlla che la collezione sia vuota. <br>
     *
     * @param collectionName corrispondente ad una collection sul database mongoDB
     *
     * @return true if the collection is null or empty
     */
    public boolean isCollectionNullOrEmpty(final String collectionName) {
        if (isExistsCollection(collectionName)) {
            return count(collectionName) == 0;
        }
        else {
            return true;
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
        return entityClazz == null ? false : isExistsCollection(entityClazz.getSimpleName().toLowerCase());
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

        String shortName = fileService.estraeClasseFinale(collectionName);
        shortName = textService.primaMinuscola(shortName);

        return mongoOp.collectionExists(shortName);
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
            if (isExistsCollection(shortName)) {
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

    /**
     * Conteggio delle entities di una collection <br>
     *
     * @param collectionName corrispondente ad una collection sul database mongoDB
     *
     * @return numero di entities totali
     */
    public int count(final String collectionName) {
        String simpleLowerCollectionName = fileService.estraeClasseFinale(collectionName);
        simpleLowerCollectionName = textService.primaMinuscola(simpleLowerCollectionName);
        Long entities;
        Query query = new Query();
        entities = mongoOp.count(query, simpleLowerCollectionName);
        return entities > 0 ? entities.intValue() : 0;
    }

    public void deleteAll(final Class clazz) {
        mongoOp.dropCollection(clazz);
    }

    public DeleteResult delete(final Object entityBean) {
        return mongoOp.remove(entityBean);
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
        String message;
        collection = getCollection(textService.primaMinuscola(entityClazz.getSimpleName()));

        if (collection == null) {
            message = String.format("Non esiste la collection", entityClazz.getSimpleName());
            logger.warn(new WrapLog().exception(new AlgosException(message)).usaDb());
            return null;
        }

        Bson projection = Projections.fields(Projections.include(property), Projections.excludeId());
        FindIterable<Document> documents = collection.find().projection(projection);

        for (var singolo : documents) {
            listaProperty.add(singolo.get(property, String.class));
        }
        return listaProperty;
    }

    public List<Long> projectionLong(Class<? extends AEntity> entityClazz, String property) {
        List<Long> listaProperty = new ArrayList();
        String message;
        collection = getCollection(textService.primaMinuscola(entityClazz.getSimpleName()));

        if (collection == null) {
            message = String.format("Non esiste la collection", entityClazz.getSimpleName());
            logger.warn(new WrapLog().exception(new AlgosException(message)).usaDb());
            return null;
        }

        Bson projection = Projections.fields(Projections.include(property), Projections.excludeId());
        FindIterable<Document> documents = collection.find().projection(projection);

        for (var singolo : documents) {
            listaProperty.add(singolo.get(property, Long.class));
        }
        return listaProperty;
    }

    public List<AEntity> projectionExclude(Class<? extends AEntity> entityClazz, CrudBackend backend, String property) {
        return projectionExclude(entityClazz, backend, null, property);
    }


    public List<AEntity> projectionExclude(Class<? extends AEntity> entityClazz, CrudBackend backend, Bson sort, String property) {
        List<AEntity> listaExcluded = new ArrayList();
        Bson projection;
        FindIterable<Document> documents;
        String message;
        collection = getCollection(textService.primaMinuscola(entityClazz.getSimpleName()));
        AEntity entityBean;

        if (collection == null) {
            message = String.format("Non esiste la collection", entityClazz.getSimpleName());
            logger.warn(new WrapLog().exception(new AlgosException(message)).usaDb());
            return null;
        }

        projection = Projections.fields(Projections.exclude(property), Projections.excludeId());
        documents = collection.find().sort(sort).projection(projection);

        for (var singolo : documents) {
            entityBean = backend.newEntity(singolo);
            if (entityBean != null) {
                listaExcluded.add(entityBean);
            }
        }

        return listaExcluded;
    }

}