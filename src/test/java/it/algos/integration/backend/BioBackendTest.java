package it.algos.integration.backend;

import com.mongodb.client.*;
import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.wrapper.*;
import org.bson.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.data.mongodb.core.query.Query;

import java.util.*;

import org.springframework.data.domain.*;

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


    @Test
    @Order(2)
    @DisplayName("2 - findAllAll (repository)")
    void findAllAll() {
        System.out.println("2 - findAllAll (repository)");
        String message;

        listaBeans = backend.findAllAll();
        assertNotNull(listaBeans);
        message = String.format("Ci sono in totale %s entities di %s caricate dalla repository", textService.format(listaBeans.size()), "Bio");
        System.out.println(message);
        System.out.println(VUOTA);
        message = String.format("Le %s entities sono stata caricate in %s", textService.format(listaBeans.size()), dateService.deltaTextEsatto(inizio));
        System.out.println(message);
    }


    @Test
    @Order(3)
    @DisplayName("3 - findAll (mongoOp)")
    void findQuery() {
        System.out.println("3 - findAll (mongoOp)");
        String message;

        listaBeans = mongoService.query(Bio.class);
        assertNotNull(listaBeans);
        message = String.format("Ci sono in totale %s entities di %s caricate da mongoService.mongoOp", textService.format(listaBeans.size()), "Bio");
        System.out.println(message);
        System.out.println(VUOTA);
        message = String.format("Le %s entities sono stata caricate in %s", textService.format(listaBeans.size()), dateService.deltaTextEsatto(inizio));
        System.out.println(message);
    }


    @Test
    @Order(4)
    @DisplayName("4 - findSenzaTmpl ridotto senza tmplBio")
    void findSenzaTmpl() {
        System.out.println("4 - findSenzaTmpl ridotto senza tmplBio");
        String message;

        listaBeans = backend.findSenzaTmpl();
        assertNotNull(listaBeans);
        message = String.format("Ci sono in totale %s entities di %s senza tmplBio", textService.format(listaBeans.size()), "Bio");
        System.out.println(message);
        System.out.println(VUOTA);
        message = String.format("Le %s entities sono stata caricate in %s", textService.format(listaBeans.size()), dateService.deltaTextEsatto(inizio));
        System.out.println(message);
    }


    @Test
    @Order(5)
    @DisplayName("5 - findAll minimale wikiTitle (stringa)")
    void projectionString() {
        System.out.println("5 - findAll minimale wikiTitle (stringa)");
        String message;

        listaBeans = mongoService.projectionString(Bio.class, "wikiTitle");
        assertNotNull(listaBeans);
        message = String.format("Ci sono in totale %s entities di %s con solo wikiTitle", textService.format(listaBeans.size()), "Bio");
        System.out.println(message);
        System.out.println(VUOTA);
        message = String.format("Le %s entities sono stata caricate in %s", textService.format(listaBeans.size()), dateService.deltaTextEsatto(inizio));
        System.out.println(message);
    }


    @Test
    @Order(6)
    @DisplayName("6 - findAllWikiTitle (stringa)")
    void findAllWikiTitle() {
        System.out.println("6 - findAllWikiTitle (stringa)");
        String message;

        listaStr = backend.findAllWikiTitle();
        assertNotNull(listaStr);
        message = String.format("Ci sono in totale %s string di wikiTitle", textService.format(listaStr.size()));
        System.out.println(message);
        System.out.println(VUOTA);
        message = String.format("Le %s string sono stata caricate in %s", textService.format(listaStr.size()), dateService.deltaTextEsatto(inizio));
        System.out.println(message);
    }

    @Test
    @Order(7)
    @DisplayName("7 - findAll minimale pageId (long)")
    void projectionLong() {
        System.out.println("7 - findAll minimale pageId (long)");
        String message;

        listaBeans = mongoService.projectionLong(Bio.class, "pageId");
        assertNotNull(listaBeans);
        message = String.format("Ci sono in totale %s entities di %s con solo pageId", textService.format(listaBeans.size()), "Bio");
        System.out.println(message);
        System.out.println(VUOTA);
        message = String.format("Le %s entities sono stata caricate in %s", textService.format(listaBeans.size()), dateService.deltaTextEsatto(inizio));
        System.out.println(message);
    }

    @Test
    @Order(8)
    @DisplayName("8 - findAllPageId (long)")
    void findAllPageId() {
        System.out.println("8 - findAllPageId (long)");
        String message;

        listaLong = backend.findAllPageId();
        assertNotNull(listaLong);
        message = String.format("Ci sono in totale %s long di pageId", textService.format(listaLong.size()));
        System.out.println(message);
        System.out.println(VUOTA);
        message = String.format("I %s long sono stati caricati in %s", textService.format(listaLong.size()), dateService.deltaTextEsatto(inizio));
        System.out.println(message);
    }


    @Test
    @Order(9)
    @DisplayName("9 - find wikiTitle + pageId")
    void findAllWikiTitlePageId() {
        System.out.println("9 - find wikiTitle + pageId");
        String message;

        listaBeans = backend.findAllWikiTitlePageId();
        assertNotNull(listaBeans);
        message = String.format("Ci sono in totale %s bio con wikiTitle e pageId", textService.format(listaBeans.size()));
        System.out.println(message);
        System.out.println(VUOTA);
        message = String.format("Le %s entities sono stata caricate in %s", textService.format(listaBeans.size()), dateService.deltaTextEsatto(inizio));
        System.out.println(message);
    }


    @Test
    @Order(10)
    @DisplayName("10 - findAllWrapTime")
    void findAllWrapTime() {
        System.out.println("10 - findAllWrapTime");
        String message;

        List<WrapTime> listaWrap = backend.findAllWrapTime();
        assertNotNull(listaWrap);
        message = String.format("Ci sono in totale %s wrapTime con wikiTitle e pageId e lastModifica", textService.format(listaWrap.size()));
        System.out.println(message);
        System.out.println(VUOTA);
        message = String.format("Le %s wrapTime sono stata caricate in %s", textService.format(listaWrap.size()), dateService.deltaTextEsatto(inizio));
        System.out.println(message);
    }


    @Test
    @Order(31)
    @DisplayName("31 - find inesistente")
    void find() {
        System.out.println("31 - find inesistente");
        String message;

        sorgente = "inesistente";
        entityBean = backend.findByTitle(sorgente);
        assertNull(entityBean);
        System.out.println(String.format("La biografia '%s' non esiste su mongoDB", sorgente));
    }

    @Test
    @Order(32)
    @DisplayName("32 - find esistente")
    void find2() {
        System.out.println("32 - find esistente");

        sorgente = "Elisabeth Kopp";
        entityBean = backend.findByTitle(sorgente);
        assertNotNull(entityBean);
        System.out.println(String.format("Trovata la biografia '%s' su mongoDB", sorgente));
    }

    //    @Test
    @Order(33)
    @DisplayName("33 - controllo due biografie")
    void find3() {
        System.out.println("33 - controllo due biografie");

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
    @Order(34)
    @DisplayName("34 - insert")
    void insert() {
        System.out.println("34 - insert");
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
    @Order(35)
    @DisplayName("35 - save")
    void save() {
        System.out.println("35 - save");
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
    @Order(36)
    @DisplayName("36 - sort")
    void find8() {
        System.out.println("36 - Sort");
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
    @Order(37)
    @DisplayName("37 - Index")
    void find9() {
        System.out.println("37 - Index");
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


    @Test
    @Order(81)
    @DisplayName("81 - findAllCognomiDistinti")
    void findAllCognomiDistinti() {
        System.out.println("81 - findAllCognomiDistinti");

        inizio = System.currentTimeMillis();
        listaStr = backend.findAllCognomiDistinti();

        System.out.println(VUOTA);
        System.out.println(String.format("La ricerca/selezione dei %s cognomi distinti ha richiesto %s", textService.format(listaStr.size()), dateService.deltaTextEsatto(inizio)));
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
