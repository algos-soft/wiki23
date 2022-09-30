package it.algos.integration.backend;

import com.mongodb.client.*;
import com.mongodb.client.model.*;
import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.packages.bio.*;
import org.bson.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.data.mongodb.core.query.Query;

import java.util.*;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.data.domain.*;

import javax.persistence.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Fri, 27-May-2022
 * Time: 18:44
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("production")
@Tag("backend")
@DisplayName("Bio Backend")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BioBackendTest extends WikiTest {

    //    /**
    //     * The Service.
    //     */
    //    @InjectMocks
    private BioBackend backend;

    @Autowired
    private BioRepository repository;


    private Bio entityBean;

    private Bio bioSalvini;

    private Bio bioRenzi;


    private List<Bio> listaBeans;

    protected static final long PAGE_UNO = 196744;

    protected static final String TITLE_UNO = "Annie Proulx";

    protected static final String TMPL_UNO = "{{Bio\n" +
            "|Nome = Annie\n" +
            "|Cognome = Proulx\n" +
            "|PostCognomeVirgola = all'anagrafe '''Edna Annie Proulx'''\n" +
            "|PreData = /pruː/\n" +
            "|Sesso = F\n" +
            "|LuogoNascita = Norwich\n" +
            "|LuogoNascitaLink = Norwich (Connecticut)\n" +
            "|GiornoMeseNascita = 22 agosto\n" +
            "|AnnoNascita = 1935\n" +
            "|LuogoMorte = \n" +
            "|GiornoMeseMorte = \n" +
            "|AnnoMorte = \n" +
            "|Epoca = 1900\n" +
            "|Attività = scrittrice\n" +
            "|Nazionalità = statunitense\n" +
            "|PostNazionalità = e di origini [[Canada|canadesi]], vincitrice del [[Premio Pulitzer per la narrativa]] con il romanzo ''[[Avviso ai naviganti (romanzo)|Avviso ai naviganti]]''\n" +
            "|Immagine = 2018-us-nationalbookfestival-annie-proulx.jpg\n" +
            "|Didascalia = Annie Proulx al National Book Festival 2018\n" +
            "}}";

    /**
     * Qui passa una volta sola <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();

        //        MockitoAnnotations.initMocks(this);
        //        MockitoAnnotations.initMocks(backend);
        //        Assertions.assertNotNull(backend);

        backend.repository = repository;
        backend.crudRepository = repository;
        backend.arrayService = arrayService;
        backend.reflectionService = reflectionService;
        backend.textService = textService;
    }


    /**
     * Inizializzazione dei service <br>
     * Devono essere tutti 'mockati' prima di iniettare i riferimenti incrociati <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void initMocks() {
        super.initMocks();
    }


    /**
     * Regola tutti riferimenti incrociati <br>
     * Deve essere fatto dopo aver costruito tutte le referenze 'mockate' <br>
     * Nelle sottoclassi devono essere regolati i riferimenti dei service specifici <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixRiferimentiIncrociati() {
        super.fixRiferimentiIncrociati();
        backend = bioBackend;
        backend.mongoService = mongoService;
    }

    @BeforeEach
    protected void setUpEach() {
        super.setUpEach();

        this.entityBean = null;
        this.listaBeans = null;
    }

    /**
     * Controlla l'esistenza di un paio di biografie da usare nei test <br>
     * Se non esistono, le crea scaricandole dal server wiki <br>
     */
    protected void creaBiografieIniziali() {

        if (!backend.isExist(BIO_RENZI_PAGEID)) {
            bioRenzi = queryService.getBio(BIO_RENZI_PAGEID);
        }
        if (!backend.isExist(BIO_SALVINI_PAGEID)) {
            bioSalvini = queryService.getBio(BIO_SALVINI_PAGEID);
        }
    }

    @Test
    @Order(1)
    @DisplayName("1 - count")
    void count() {
        System.out.println("1 - count");
        String message;

        ottenutoIntero = backend.count();
        assertTrue(ottenutoIntero > 0);
        message = String.format("Ci sono in totale %s entities nel database mongoDB", textService.format(ottenutoIntero));
        System.out.println(message);
    }


    //    @Test
    @Order(2)
    @DisplayName("2 - findAll")
    void findAll() {
        System.out.println("2 - findAll");
        String message;

        listaBeans = mongoService.query(Bio.class);
        assertNotNull(listaBeans);
        message = String.format("Ci sono in totale %s entities di %s", textService.format(listaBeans.size()), "Bio");
        System.out.println(message);
        System.out.println(VUOTA);
        message = String.format("Le %s entities sono stata caricate in %s", textService.format(listaBeans.size()), dateService.deltaTextEsatto(inizio));
        System.out.println(message);
    }


    //    @Test
    @Order(3)
    @DisplayName("3 - findAll ridotto")
    void findAllRidotto() {
        System.out.println("3 - findAll ridotto");
        String message;

        listaBeans = backend.findSenzaTmpl();
        assertNotNull(listaBeans);
        message = String.format("Ci sono in totale %s entities di %s", textService.format(listaBeans.size()), "Bio");
        System.out.println(message);
        System.out.println(VUOTA);
        message = String.format("Le %s entities sono stata caricate in %s", textService.format(listaBeans.size()), dateService.deltaTextEsatto(inizio));
        System.out.println(message);
    }

    //    @Test
    @Order(4)
    @DisplayName("4 - find inesistente")
    void find() {
        System.out.println("4 - find inesistente");
        String message;

        sorgente = "inesistente";
        entityBean = backend.findByTitle(sorgente);
        assertNull(entityBean);
        System.out.println(String.format("La biografia '%s' non esiste su mongoDB", sorgente));
    }

    //    @Test
    @Order(5)
    @DisplayName("5 - find esistente")
    void find2() {
        System.out.println("5 - find esistente");

        sorgente = "Elisabeth Kopp";
        entityBean = backend.findByTitle(sorgente);
        assertNotNull(entityBean);
        System.out.println(String.format("Trovata la biografia '%s' su mongoDB", sorgente));
    }

    //    @Test
    @Order(6)
    @DisplayName("6 - controllo due biografie")
    void find3() {
        System.out.println("6 - controllo due biografie");

        entityBean = backend.findByTitle(BIO_SALVINI);
        assertNotNull(entityBean);
        bioSalvini = backend.findByKey(BIO_SALVINI_PAGEID);
        assertNotNull(bioSalvini);
        System.out.println(String.format("La biografia '%s' esiste su mongoDB", bioSalvini.getWikiTitle()));

        entityBean = backend.findByTitle(BIO_RENZI);
        assertNotNull(entityBean);
        bioRenzi = backend.findByKey(BIO_RENZI_PAGEID);
        assertNotNull(bioRenzi);
        System.out.println(String.format("La biografia '%s' esiste su mongoDB", bioRenzi.getWikiTitle()));

    }


    //    @Test
    @Order(7)
    @DisplayName("7 - insert")
    void insert() {
        System.out.println("7 - insert");
        System.out.println(String.format("Inizialmente nel mongoDB ci sono %s biografie", textService.format(backend.count())));
        pageId = PAGE_UNO;
        sorgente = TITLE_UNO;
        sorgente2 = TMPL_UNO;

        bio = backend.findByTitle(sorgente);
        if (bio != null && backend.isExist(bio.pageId)) {
            backend.delete(bio);
            System.out.println(String.format("Adesso sono %s (dopo una cancellazione iniziale)", textService.format(backend.count())));
        }
        assertFalse(backend.isExist(pageId));

        bio = backend.newEntity(pageId, sorgente, sorgente2);
        assertNotNull(bio);
        bio = elaboraService.esegue(bio);
        backend.save(bio);
        System.out.println(String.format("Adesso sono %s (dopo un insert)", textService.format(backend.count())));
        assertTrue(backend.isExist(pageId));

        backend.delete(bio);
        assertFalse(backend.isExist(pageId));
        System.out.println(String.format("Adesso sono tornate %s (dopo un delete)", textService.format(backend.count())));
    }

    //    @Test
    @Order(8)
    @DisplayName("8 - save")
    void save() {
        System.out.println("8 - save");
        System.out.println(String.format("Inizialmente nel mongoDB ci sono %s biografie", textService.format(backend.count())));
        Bio bioModificato;
        pageId = PAGE_UNO;
        sorgente = TITLE_UNO;
        sorgente2 = TMPL_UNO;

        bio = backend.findByTitle(sorgente);
        if (bio != null && backend.isExist(bio.pageId)) {
            backend.delete(bio);
            System.out.println(String.format("Adesso sono %s (dopo una cancellazione iniziale)", textService.format(backend.count())));
        }
        assertFalse(backend.isExist(pageId));

        bio = backend.newEntity(pageId, sorgente, sorgente2);
        assertNotNull(bio);
        bio = elaboraService.esegue(bio);
        backend.save(bio);
        System.out.println(String.format("Adesso sono %s (dopo un insert)", textService.format(backend.count())));
        assertTrue(backend.isExist(pageId));

        bio.setAnnoNato("3457");
        backend.save(bio);
        System.out.println(String.format("Adesso sono ancora %s (dopo un save)", textService.format(backend.count())));
        assertTrue(backend.isExist(pageId));

        bioModificato = backend.findByTitle(sorgente);
        assertNotNull(bioModificato);
        assertEquals("3457", bioModificato.annoNato);

        assertTrue(backend.isExist(pageId));
        backend.delete(bio);
        assertFalse(backend.isExist(pageId));
        System.out.println(String.format("Adesso sono tornate %s (dopo un delete)", textService.format(backend.count())));
    }

    //    @Test
    @Order(9)
    @DisplayName("9 - sort")
    void find8() {
        System.out.println("9 - Sort");
        MongoCollection<Document> collection;
        int numVoci;

        clazz = Bio.class;
        sorgente = clazz.getSimpleName().toLowerCase();
        sorgente2 = "pageId";
        sorgente3 = "ordinamento";
        collection = mongoService.getCollection(sorgente);
        assertNotNull(collection);
        numVoci = backend.count();
        System.out.println(String.format("Nella collezione %s ci sono %s entities", sorgente, textService.format(numVoci)));

        startTime();
        listaBeans = mongoService.query(clazz);
        System.out.println(VUOTA);
        System.out.println("Senza sort - mongoService.query(clazz)");
        printTimeEsatto();
        printDieci(listaBeans);

        startTime();
        listaBeans = mongoService.mongoOp.findAll(clazz);
        System.out.println(VUOTA);
        System.out.println("Senza sort - mongoOp.findAll(clazz)");
        printTimeEsatto();
        printDieci(listaBeans);

        Query query = new Query();
        Sort sort = Sort.by(Sort.Direction.ASC, sorgente2);
        startTime();
        listaBeans = mongoService.mongoOp.find(query, clazz);
        System.out.println(VUOTA);
        System.out.println(String.format("Sort con %s - mongoOp.find(query, clazz) - Esiste indice", sorgente2));
        printTimeEsatto();
        printDieci(listaBeans);

        query = new Query();
        query.limit(1000);
        startTime();
        listaBeans = mongoService.mongoOp.find(query, clazz);
        System.out.println(VUOTA);
        System.out.println(String.format("Senza sort - mongoOp.find(query, clazz, 1000)", sorgente2));
        printTimeEsatto();
        printDieci(listaBeans);

        query = new Query();
        sort = Sort.by(Sort.Direction.ASC, sorgente2);
        query.with(sort);
        query.limit(1000);
        startTime();
        listaBeans = mongoService.mongoOp.find(query, clazz);
        System.out.println(VUOTA);
        System.out.println(String.format("Sort con %s - mongoOp.find(query, clazz, 1000) - Esiste indice", sorgente2));
        printTimeEsatto();
        printDieci(listaBeans);

        query = new Query();
        sort = Sort.by(Sort.Direction.ASC, sorgente3);
        query.with(sort);
        query.limit(1000);
        startTime();
        listaBeans = mongoService.mongoOp.find(query, clazz);
        System.out.println(VUOTA);
        System.out.println(String.format("Sort con %s - mongoOp.find(query, clazz, 1000) - Non esiste indice", sorgente3));
        printTimeEsatto();
        printDieci(listaBeans);
    }

    //    @Test
    @Order(10)
    @DisplayName("10 - Index")
    void find9() {
        System.out.println("10 - Index");
        MongoCollection<Document> collection;

        clazz = Bio.class;
        sorgente = clazz.getSimpleName().toLowerCase();
        sorgente2 = "pageId";
        collection = mongoService.getCollection(sorgente);
        assertNotNull(collection);

        startTime();
        mongoService.mongoOp.findAll(clazz);
        printTimeEsatto();

        sorgente3 = "wikiTitle";
        //        collection.createIndex(Indexes.ascending(sorgente3), new IndexOptions().unique(false));
        //        ListIndexesIterable<Document> listaIndex = collection.listIndexes();
        //        for (Document doc : listaIndex) {
        //            String alfa = doc.toJson();
        //        }
        //
        //        collection.dropIndex(sorgente3 + "_1");
        //
        //
        //
        ////        collection.dropIndex(sorgente2);
        //        inizio = System.currentTimeMillis();
        //        listaBeans = backend.findAll();
        //        System.out.println(dateService.deltaTextEsatto(inizio));
        //
        //        collection.createIndex(Indexes.ascending(sorgente2), new IndexOptions().unique(false));
        //        inizio = System.currentTimeMillis();
        //        listaBeans = backend.findAll(Sort.by(sorgente2));
        //        System.out.println(dateService.deltaTextEsatto(inizio));

        //        listaBeans = backend.findOnlyPageId();
        int a = 87;
    }


    //    @Test
    @Order(11)
    @DisplayName("11 - count")
    void countSex() {
        System.out.println("11 - count");
        String message;

        backend.fixErroriSesso();
    }

    @Test
    @Order(12)
    @DisplayName("12 - findAllAnnoNatoMese")
    void findAllAnnoNatoMese() {
        System.out.println("12 - findAllAnnoNatoMese");
        sorgente = "1645";

        sorgente2 = VUOTA;
        listaBeans = backend.findAllAnnoNatoMese(sorgente, sorgente2);
        assertNotNull(listaBeans);

        sorgente2 = "marzo";
        listaBeans = backend.findAllAnnoNatoMese(sorgente, sorgente2);
        assertNotNull(listaBeans);
        printBio(listaBeans);
    }

    @Test
    @Order(13)
    @DisplayName("13 - countAllAnnoNatoMese")
    void countAllAnnoNatoMese() {
        System.out.println("13 - countAllAnnoNatoMese");
        sorgente = "1645";

        sorgente2 = VUOTA;
        ottenutoIntero = backend.countAllAnnoNatoMese(sorgente, sorgente2);
        assertTrue(ottenutoIntero > 0);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nell'anno %s", ottenutoIntero, sorgente));

        sorgente2 = "marzo";
        ottenutoIntero = backend.countAllAnnoNatoMese(sorgente, sorgente2);
        assertTrue(ottenutoIntero > 0);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nell'anno %s per il mese di %s", ottenutoIntero, sorgente, sorgente2));
    }


    @Test
    @Order(14)
    @DisplayName("14 - findAllAnnoMortoMese")
    void findAllAnnoMortoMese() {
        System.out.println("14 - findAllAnnoMortoMese");
        sorgente = "1645";

        sorgente2 = VUOTA;
        listaBeans = backend.findAllAnnoMortoMese(sorgente, sorgente2);
        assertNotNull(listaBeans);

        sorgente2 = "marzo";
        listaBeans = backend.findAllAnnoMortoMese(sorgente, sorgente2);
        assertNotNull(listaBeans);
        printBio(listaBeans);
    }


    @Test
    @Order(15)
    @DisplayName("15 - countAllAnnoMortoMese")
    void countAllAnnoMortoMese() {
        System.out.println("15 - countAllAnnoMortoMese");
        sorgente = "1645";

        sorgente2 = VUOTA;
        ottenutoIntero = backend.countAllAnnoMortoMese(sorgente, sorgente2);
        assertTrue(ottenutoIntero > 0);
        System.out.println(String.format("Ci sono %d voci biografiche di morti nell'anno %s", ottenutoIntero, sorgente));

        sorgente2 = "marzo";
        ottenutoIntero = backend.countAllAnnoMortoMese(sorgente, sorgente2);
        assertTrue(ottenutoIntero > 0);
        System.out.println(String.format("Ci sono %d voci biografiche di morti nell'anno %s per il mese di %s", ottenutoIntero, sorgente, sorgente2));
    }

    void printDieci(List<Bio> lista) {
        for (Bio bio : lista.subList(0, 10)) {
            System.out.println(bio.wikiTitle);
        }
    }

    /**
     * Qui passa al termine di ogni singolo test <br>
     */
    @AfterEach
    void tearDown() {
    }


    /**
     * Qui passa una volta sola, chiamato alla fine di tutti i tests <br>
     */
    @AfterAll
    void tearDownAll() {
    }

    void printBeans(List<Bio> listaBeans) {
        System.out.println(VUOTA);
        int k = 0;

        for (Bio bean : listaBeans) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.println(bean);
        }
    }

}
