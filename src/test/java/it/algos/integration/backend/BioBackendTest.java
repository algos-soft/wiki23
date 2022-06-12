package it.algos.integration.backend;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.packages.bio.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import java.util.*;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;

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
@Tag("backend")
@DisplayName("Bio Backend")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BioBackendTest extends WikiTest {

    /**
     * The Service.
     */
    @InjectMocks
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
    @DisplayName("2 - findAll")
    void findAll() {
        System.out.println("2 - findAll");
        String message;

        listaBeans = backend.findAll();
        assertNotNull(listaBeans);
        message = String.format("Ci sono in totale %s entities di %s", textService.format(listaBeans.size()), "Bio");
        System.out.println(message);
        System.out.println(VUOTA);
        message = String.format("Le %s entities sono stata caricate in %s", textService.format(listaBeans.size()), dateService.deltaTextEsatto(inizio));
        System.out.println(message);
    }

    @Test
    @Order(3)
    @DisplayName("3 - find inesistente")
    void find() {
        System.out.println("3 - find inesistente");
        String message;

        sorgente = "inesistente";
        entityBean = backend.findByTitle(sorgente);
        assertNull(entityBean);
        System.out.println(String.format("La biografia '%s' non esiste su mongoDB", sorgente));
    }

    @Test
    @Order(4)
    @DisplayName("4 - find esistente")
    void find2() {
        System.out.println("4 - find esistente");

        sorgente = "Elisabeth Kopp";
        entityBean = backend.findByTitle(sorgente);
        assertNotNull(entityBean);
        System.out.println(String.format("Trovata la biografia '%s' su mongoDB", sorgente));
    }

    @Test
    @Order(5)
    @DisplayName("5 - controllo due biografie")
    void find3() {
        System.out.println("5 - controllo due biografie");

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


    @Test
    @Order(6)
    @DisplayName("6 - insert")
    void insert() {
        System.out.println("6 - insert");
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

    @Test
    @Order(7)
    @DisplayName("7 - save")
    void save() {
        System.out.println("7 - save");
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

    @Test
    @Order(8)
    @DisplayName("8 - Only one property")
    void find8() {
        System.out.println("8 - Only one property");

//        listaBeans = backend.findOnlyPageId();
        int a=87;
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
