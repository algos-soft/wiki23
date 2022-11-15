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
public class BioBackendAttNazTest extends WikiTest {

    /**
     * Classe principale di riferimento <br>
     */
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








    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_SINGOLARE")
    @Order(51)
    @DisplayName("51 - countAttivitaSingola")
    void countAttivitaSingola(String singolare, boolean esiste) {
        System.out.println("51 - countAttivitaSingola");
        ottenutoIntero = backend.countAttivitaSingola(singolare);
        assertEquals(ottenutoIntero > 0, esiste);
        System.out.println(String.format("L'attività singolare '%s' contiene %s voci biografiche", singolare, ottenutoIntero));
    }


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_TRUE")
    @Order(52)
    @DisplayName("52 - countAttivitaPlurale")
    void countAttivitaPlurale(String plurale, boolean esiste) {
        System.out.println("52 - countAttivitaPlurale");
        ottenutoIntero = backend.countAttivitaPlurale(plurale);
        assertEquals(ottenutoIntero > 0, esiste);
        System.out.println(String.format("L'attività plurale '%s' contiene %s voci biografiche", plurale, ottenutoIntero));
    }


    @Test
    @Order(61)
    @DisplayName("61 - countAttivitaNazionalita")
    void countAttivitaNazionalita() {
        System.out.println("61 - countAttivitaNazionalita");

        sorgente = "badessa";
        sorgente2 = "belga";
        previstoIntero = 3;
        ottenutoIntero = backend.countAttivitaNazionalita(sorgente, sorgente2);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("L'attività '%s' contiene %d voci biografiche di %s", sorgente, ottenutoIntero, sorgente2));

        sorgente = "badessa";
        sorgente2 = "belgi";
        previstoIntero = 3;
        ottenutoIntero = backend.countAttivitaNazionalita(sorgente, sorgente2);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("L'attività '%s' contiene %d voci biografiche di %s", sorgente, ottenutoIntero, sorgente2));

        sorgente = "abate";
        previstoIntero = 5;
        sorgente2 = "belga";
        ottenutoIntero = backend.countAttivitaNazionalita(sorgente, sorgente2);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("L'attività '%s' contiene %d voci biografiche di %s", sorgente, ottenutoIntero, sorgente2));

        sorgente = "abate";
        sorgente2 = "belgi";
        previstoIntero = 5;
        ottenutoIntero = backend.countAttivitaNazionalita(sorgente, sorgente2);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("L'attività '%s' contiene %d voci biografiche di %s", sorgente, ottenutoIntero, sorgente2));

        sorgente = "abate";
        sorgente2 = "belgi";
        previstoIntero = 8;
        ottenutoIntero = backend.countAttivitaNazionalitaAll(sorgente, sorgente2);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("L'attività '%s' contiene %d voci biografiche di %s", sorgente, ottenutoIntero, sorgente2));

        sorgente = "badessa";
        sorgente2 = "belgi";
        previstoIntero = 8;
        ottenutoIntero = backend.countAttivitaNazionalitaAll(sorgente, sorgente2);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("L'attività '%s' contiene %d voci biografiche di %s", sorgente, ottenutoIntero, sorgente2));

        sorgente = "abati e badesse";
        sorgente2 = "belgi";
        previstoIntero = 8;
        ottenutoIntero = backend.countAttivitaNazionalitaAll(sorgente, sorgente2);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("L'attività '%s' contiene %d voci biografiche di %s", sorgente, ottenutoIntero, sorgente2));
    }


    @Test
    @Order(62)
    @DisplayName("62 - countAttivitaNazionalitaAll")
    void countAttivitaNazionalitaAll() {
        System.out.println("62 - countAttivitaNazionalitaAll");

//        sorgente = "accademici";
//        sorgente2 = "francesi";
//        previstoIntero = 15;
//        ottenutoIntero = backend.countAttivitaNazionalitaAll(sorgente, sorgente2);
//        assertEquals(previstoIntero, ottenutoIntero);
//        System.out.println(VUOTA);
//        System.out.println(String.format("L'attività '%s' contiene %d voci biografiche di %s", sorgente, ottenutoIntero, sorgente2));

        sorgente = "accademici";
        sorgente2 = "francesi";
        sorgente3 = "C";
        previstoIntero = 3;
        ottenutoIntero = backend.countAttivitaNazionalitaAll(sorgente, sorgente2, sorgente3);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("L'attività '%s' contiene %d voci biografiche di %s che iniziano con %s", sorgente, ottenutoIntero, sorgente2, sorgente3));
        System.out.println(String.format("Tempo trascorso %s", dateService.deltaTextEsatto(inizio)));

        sorgente = "sovrani";
        sorgente2 = TAG_LISTA_NO_NAZIONALITA;
        sorgente3 = "A";
        previstoIntero = 328;
        ottenutoIntero = backend.countAttivitaNazionalitaAll(sorgente, sorgente2, sorgente3);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("L'attività '%s' contiene %d voci biografiche di %s che iniziano con %s", sorgente, ottenutoIntero, sorgente2, sorgente3));
        System.out.println(String.format("Tempo trascorso %s", dateService.deltaTextEsatto(inizio)));

    }


    @Test
    @Order(63)
    @DisplayName("63 - findAttivitaNazionalita")
    void findAttivitaNazionalita() {
        System.out.println("63 - findAttivitaNazionalita");

        sorgente = "badessa";
        sorgente2 = "belga";
        previstoIntero = 3;
        listaBeans = backend.findAttivitaNazionalita(sorgente, sorgente2);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("L'attività '%s' contiene %d voci biografiche di %s", sorgente, ottenutoIntero, sorgente2));
        printBio(listaBeans);

        sorgente = "badessa";
        sorgente2 = "belgi";
        previstoIntero = 3;
        listaBeans = backend.findAttivitaNazionalita(sorgente, sorgente2);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("L'attività '%s' contiene %d voci biografiche di %s", sorgente, ottenutoIntero, sorgente2));
        printBio(listaBeans);

        sorgente = "abate";
        previstoIntero = 5;
        sorgente2 = "belga";
        listaBeans = backend.findAttivitaNazionalita(sorgente, sorgente2);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("L'attività '%s' contiene %d voci biografiche di %s", sorgente, ottenutoIntero, sorgente2));
        printBio(listaBeans);

        sorgente = "abate";
        sorgente2 = "belgi";
        previstoIntero = 5;
        listaBeans = backend.findAttivitaNazionalita(sorgente, sorgente2);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("L'attività '%s' contiene %d voci biografiche di %s", sorgente, ottenutoIntero, sorgente2));
        printBio(listaBeans);

        sorgente = "abate";
        sorgente2 = "belgi";
        previstoIntero = 8;
        listaBeans = backend.findAllAttivitaNazionalita(sorgente, sorgente2);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("L'attività '%s' contiene %d voci biografiche di %s", sorgente, ottenutoIntero, sorgente2));
        printBio(listaBeans);

        sorgente = "badessa";
        sorgente2 = "belgi";
        previstoIntero = 8;
        listaBeans = backend.findAllAttivitaNazionalita(sorgente, sorgente2);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("L'attività '%s' contiene %d voci biografiche di %s", sorgente, ottenutoIntero, sorgente2));
        printBio(listaBeans);

        sorgente = "abati e badesse";
        sorgente2 = "belgi";
        previstoIntero = 8;
        listaBeans = backend.findAllAttivitaNazionalita(sorgente, sorgente2);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("L'attività '%s' contiene %d voci biografiche di %s", sorgente, ottenutoIntero, sorgente2));
        printBio(listaBeans);
    }

    @Test
    @Order(64)
    @DisplayName("64 - findAllAttivitaNazionalita")
    void findAllAttivitaNazionalita() {
        System.out.println("64 - findAllAttivitaNazionalita");

        sorgente = "accademici";
        sorgente2 = "russi";
        previstoIntero = 8;
        listaBeans = backend.findAllAttivitaNazionalita(sorgente, sorgente2);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("L'attività '%s' contiene %d voci biografiche di %s", sorgente, ottenutoIntero, sorgente2));
        printBio(listaBeans);

        sorgente = "accademici";
        sorgente2 = "russi";
        sorgente3 = "G";
        previstoIntero = 2;
        listaBeans = backend.findAllAttivitaNazionalita(sorgente, sorgente2, sorgente3);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("L'attività '%s' contiene %d voci biografiche di %s che iniziano con %s", sorgente, ottenutoIntero, sorgente2, sorgente3));
        printBio(listaBeans);
    }


    @Test
    @Order(71)
    @DisplayName("71 - altre")
    void altre() {
        System.out.println("71 - altre");

        sorgente = "dogi";
        sorgente2 = TAG_LISTA_ALTRE;
        ottenutoIntero = backend.countAttivitaNazionalitaAll(sorgente, sorgente2);
        listaBeans = backend.findAllAttivitaNazionalita(sorgente, sorgente2);
        assertNotNull(listaBeans);
        System.out.println(VUOTA);
        System.out.println(String.format("L'attività '%s' contiene %d voci biografiche di '%s'", sorgente, ottenutoIntero, sorgente2));
        printBio(listaBeans);
    }
    @Test
    @Order(72)
    @DisplayName("72 - Senza nazionalità specificata")
    void senzaNazionalitàSpecificata() {
        System.out.println("72 - Senza nazionalità specificata");

        sorgente = "dogi";
        sorgente2 = TAG_LISTA_NO_NAZIONALITA;
        ottenutoIntero = backend.countAttivitaNazionalitaAll(sorgente, sorgente2);
        listaBeans = backend.findAllAttivitaNazionalita(sorgente, sorgente2);
        assertNotNull(listaBeans);
        System.out.println(VUOTA);
        System.out.println(String.format("L'attività '%s' contiene %d voci biografiche di '%s'", sorgente, ottenutoIntero, sorgente2));
        printBio(listaBeans);
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
