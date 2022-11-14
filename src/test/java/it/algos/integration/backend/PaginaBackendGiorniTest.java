package it.algos.integration.backend;

import it.algos.*;
import it.algos.base.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.pagina.*;
import org.junit.jupiter.api.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Sun, 23-Oct-2022
 * Time: 08:48
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("backend")
@DisplayName("Pagina BackendGiorni")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PaginaBackendGiorniTest extends WikiTest {

    /**
     * Classe principale di riferimento <br>
     */
    private PaginaBackend backend;

    @Autowired
    private PaginaRepository repository;


    private Pagina entityBean;


    private List<Pagina> listaBeans;

    /**
     * Qui passa una volta sola <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();

        backend = paginaBackend;

        backend.repository = repository;
        backend.crudRepository = repository;
        backend.arrayService = arrayService;
        backend.reflectionService = reflectionService;
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


    @Test
    @Order(1)
    @DisplayName("1 - count (mongoDB)")
    void count() {
        System.out.println("1 - count (mongoDB)");
        String message;

        ottenutoIntero = backend.count();
        message = String.format("Ci sono in totale %s entities nel database mongoDB", textService.format(ottenutoIntero));
        System.out.println(message);
    }


    @Test
    @Order(2)
    @DisplayName("2 - findAll (mongoDB)")
    void findAll() {
        System.out.println("2 - findAll (mongoDB)");
        String message;

        listaBeans = backend.findAll();
        assertNotNull(listaBeans);
        message = String.format("Ci sono in totale %s entities di %s", textService.format(listaBeans.size()), "Pagina");
        System.out.println(message);
        printBeans(listaBeans);
    }


    @Test
    @Order(11)
    @DisplayName("11 - pagine di giorni (nati) sul server")
    void getListaPagineNati() {
        System.out.println("11 - pagine di giorni (nati) sul server");
        String message;

        ottenutoArray = backend.getListaPagine(null);
        assertNotNull(ottenutoArray);
        assertTrue(ottenutoArray.size() == 0);

        sorgente = "Nati il";
        sorgenteArray = arrayService.creaArraySingolo(sorgente);
        ottenutoArray = backend.getListaPagine(sorgenteArray);
        assertNotNull(ottenutoArray);
        assertTrue(ottenutoArray.size() > 0);
        System.out.println(VUOTA);
        message = String.format("Ci sono %s pagine sul server che iniziano con %s", textService.format(ottenutoArray.size()), sorgenteArray);
        System.out.println(message);
        System.out.println(VUOTA);
        print(ottenutoArray);
    }

    @Test
    @Order(12)
    @DisplayName("12 - pagine di giorni (nati) sul server")
    void getListaPagineNati2() {
        System.out.println("12 - pagine di giorni (nati) sul server");
        String message;

        ottenutoArray = backend.getListaPagine(null);
        assertNotNull(ottenutoArray);
        assertTrue(ottenutoArray.size() == 0);

        sorgente = "Nati l'";
        sorgenteArray = arrayService.creaArraySingolo(sorgente);
        ottenutoArray = backend.getListaPagine(sorgenteArray);
        assertNotNull(ottenutoArray);
        assertTrue(ottenutoArray.size() > 0);
        System.out.println(VUOTA);
        message = String.format("Ci sono %s pagine sul server che iniziano con %s", textService.format(ottenutoArray.size()), sorgenteArray);
        System.out.println(message);
        System.out.println(VUOTA);
        print(ottenutoArray);
    }

    @Test
    @Order(13)
    @DisplayName("13 - pagine di giorni (morti) sul server")
    void getListaPagineMorti() {
        System.out.println("13 - pagine di giorni (morti) sul server");
        String message;

        ottenutoArray = backend.getListaPagine(null);
        assertNotNull(ottenutoArray);
        assertTrue(ottenutoArray.size() == 0);

        sorgente = "Morti il";
        sorgenteArray = arrayService.creaArraySingolo(sorgente);
        ottenutoArray = backend.getListaPagine(sorgenteArray);
        assertNotNull(ottenutoArray);
        assertTrue(ottenutoArray.size() > 0);
        System.out.println(VUOTA);
        message = String.format("Ci sono %s pagine sul server che iniziano con %s", textService.format(ottenutoArray.size()), sorgenteArray);
        System.out.println(message);
        System.out.println(VUOTA);
        print(ottenutoArray);
    }

    @Test
    @Order(14)
    @DisplayName("14 - pagine di giorni (morti) sul server")
    void getListaPagineMorti2() {
        System.out.println("14 - pagine di giorni (morti) sul server");
        String message;

        ottenutoArray = backend.getListaPagine(null);
        assertNotNull(ottenutoArray);
        assertTrue(ottenutoArray.size() == 0);

        sorgente = "Morti l'";
        sorgenteArray = arrayService.creaArraySingolo(sorgente);
        ottenutoArray = backend.getListaPagine(sorgenteArray);
        assertNotNull(ottenutoArray);
        assertTrue(ottenutoArray.size() > 0);
        System.out.println(VUOTA);
        message = String.format("Ci sono %s pagine sul server che iniziano con %s", textService.format(ottenutoArray.size()), sorgenteArray);
        System.out.println(message);
        System.out.println(VUOTA);
        print(ottenutoArray);
    }

    @Test
    @Order(21)
    @DisplayName("21 - getTagGiorni")
    void getTagGiorni() {
        System.out.println("21 - getTagGiorni");
        System.out.println(VUOTA);
        String message;
        previstoIntero = 4;

        listaStr = backend.getTagGiorni();
        assertNotNull(listaStr);
        assertEquals(previstoIntero, listaStr.size());
        message = String.format("Tag per i giorni%s%s", FORWARD, listaStr);
        System.out.println(message);
        System.out.println(VUOTA);
        print(listaStr);
    }

    @Test
    @Order(31)
    @DisplayName("31 - pagine di giorni sul server via backend.getTagGiorni()")
    void getListaPagine() {
        System.out.println("31 - pagine di giorni sul server via backend.getTagGiorni()");
        String message;

        ottenutoArray = backend.getListaPagine(null);
        assertNotNull(ottenutoArray);
        assertTrue(ottenutoArray.size() == 0);

        sorgenteArray = backend.getTagGiorni();
        ottenutoArray = backend.getListaPagine(sorgenteArray);
        assertNotNull(ottenutoArray);
        assertTrue(ottenutoArray.size() > 0);
        System.out.println(VUOTA);
        message = String.format("Ci sono %s pagine sul server che iniziano con %s", textService.format(ottenutoArray.size()), sorgenteArray);
        System.out.println(message);
        System.out.println(VUOTA);
        print(ottenutoArray);
    }

    @Test
    @Order(32)
    @DisplayName("32 - pagine di giorni sul server via backend.getPagineGiorni()")
    void getListaPagine2() {
        System.out.println("32 - pagine di giorni sul server via backend.getPagineGiorni()");
        String message;
        previstoIntero = 739;

        listaStr = backend.getPagineGiorni();
        assertNotNull(listaStr);
        assertEquals(previstoIntero, listaStr.size());
        message = String.format("Ci sono %s pagine sul server per i giorni", textService.format(listaStr.size()));
        System.out.println(message);
        System.out.println(VUOTA);
        print(listaStr);
    }


    @Test
    @Order(41)
    @DisplayName("41 - pagine di giorni di primo livello sul server")
    void getGiorniPrimoLivello() {
        System.out.println("41 - pagine di giorni di primo livello sul server");
        String message;
        previstoIntero = 736;

        listaStr = backend.getGiorniPrimoLivello();
        assertNotNull(listaStr);
        assertEquals(previstoIntero, listaStr.size());
        message = String.format("Ci sono %s pagine di primo livello sul server per i giorni", textService.format(listaStr.size()));
        System.out.println(message);
        System.out.println(VUOTA);
        print(listaStr);
    }


    @Test
    @Order(42)
    @DisplayName("42 - pagine di giorni di secondo livello sul server")
    void getGiorniSecondoLivello() {
        System.out.println("42 - pagine di giorni di secondo livello sul server");
        String message;
        previstoIntero = 3;

        listaStr = backend.getGiorniSecondoLivello();
        assertNotNull(listaStr);
        assertEquals(previstoIntero, listaStr.size());
        message = String.format("Ci sono %s pagine di secondo livello sul server per i giorni", textService.format(listaStr.size()));
        System.out.println(message);
        System.out.println(VUOTA);
        print(listaStr);
    }


    @Test
    @Order(51)
    @DisplayName("51 - count cancellare primo livello (mongoDB)")
    void countGiorniPrimoLivelloErrati() {
        System.out.println("51 - count cancellare primo livello (mongoDB)");
        String message;

        ottenutoIntero = backend.countGiorniPrimoLivelloErrati();
        message = String.format("Ci sono %d pagine di primo livello da cancellare su mongoDB per i giorni", ottenutoIntero);
        System.out.println(message);
    }

    @Test
    @Order(52)
    @DisplayName("52 - count cancellare secondo livello (mongoDB)")
    void countGiorniSecondoLivelloErrati() {
        System.out.println("52 - count cancellare secondo livello (mongoDB)");
        String message;

        ottenutoIntero = backend.countGiorniSecondoLivelloErrati();
        message = String.format("Ci sono %d pagine di secondo livello da cancellare su mongoDB per i giorni", ottenutoIntero);
        System.out.println(message);
    }

    @Test
    @Order(53)
    @DisplayName("53 - count cancellare pagine giorni (mongoDB)")
    void countGiorniErrati() {
        System.out.println("53 - count cancellare pagine giorni (mongoDB)");
        String message;

        ottenutoIntero = backend.countGiorniErrati();
        message = String.format("Ci sono %d pagine da cancellare su mongoDB per i giorni", ottenutoIntero);
        System.out.println(message);
    }


    @Test
    @Order(61)
    @DisplayName("61 - countCancellareByType (entity)")
    void countCancellareByType() {
        System.out.println("61 - countCancellareByType (entity)");
        String message;

        AETypePaginaCancellare type = AETypePaginaCancellare.giornoSotto;
        previstoIntero = 1;
        ottenutoIntero = backend.countCancellareByType(type);
        assertEquals(previstoIntero, ottenutoIntero);
        message = String.format("Ci sono in totale %d entities di %s", ottenutoIntero, "pagine di giorni sotto");
        System.out.println(message);
    }

    @Test
    @Order(62)
    @DisplayName("62 - findAllCancellareByType (entity)")
    void findAllCancellareByType() {
        System.out.println("62 - findAllCancellareByType (entity)");
        String message;

        AETypePaginaCancellare type = AETypePaginaCancellare.giornoSotto;
        listaBeans = backend.findAllCancellareByType(type);
        assertNotNull(listaBeans);
        message = String.format("Ci sono in totale %s entities di %s", textService.format(listaBeans.size()), "pagine di giorni sotto");
        System.out.println(message);
        printBeans(listaBeans);
    }


    @Test
    @Order(77)
    @DisplayName("77 - elaboraGiorniPagine")
    void elaboraGiorniPagine() {
        System.out.println("77 - elaboraGiorniPagine");
        mongoService.deleteAll(Pagina.class);
        String message;

        List<String> valideBase = giornoWikiBackend.findAllPagine();
        System.out.println(String.format("Tempo %s", dateService.deltaTextEsatto(inizio)));

        inizio = System.currentTimeMillis();
        List<String> pagineAllGiorni = backend.getPagineGiorni();
        System.out.println(String.format("Tempo %s", dateService.deltaTextEsatto(inizio)));

        List<String> pagine = backend.getGiorniAnniPrimoLivello(pagineAllGiorni);

        inizio = System.currentTimeMillis();
        listaBeans = backend.elaboraGiorniPagine(valideBase, pagine);
        assertNotNull(listaBeans);
        message = String.format("Ci sono %s pagine per i giorni, elaborate in %s", textService.format(listaBeans.size()), dateService.deltaText(inizio));
        System.out.println(message);
        printBeans(listaBeans);
    }


    //    @Test
    @Order(42)
    @DisplayName("42 - elaboraGiorniSottoPagine")
    void elaboraGiorniSottoPagine() {
        System.out.println("42 - elaboraGiorniSottoPagine");
        mongoService.deleteAll(Pagina.class);
        String message;
        List<String> valideBase = giornoWikiBackend.findAllPagine();
        List<String> pagineAllGiorni = backend.getPagineGiorni();
        List<String> pagineSotto = backend.getGiorniAnniSotto(pagineAllGiorni);

        inizio = System.currentTimeMillis();
        listaBeans = backend.elaboraGiorniSottoPagine(valideBase, pagineSotto);
        assertNotNull(listaBeans);
        message = String.format("Ci sono %s sotto-pagine per i giorni, elaborate in %s", textService.format(listaBeans.size()), dateService.deltaText(inizio));
        System.out.println(message);
        printBeans(listaBeans);
    }


    //    @Test
    @Order(13)
    @DisplayName("13 - elaboraGiorni")
    void elaboraGiorni() {
        System.out.println("13 - elaboraGiorni");
        mongoService.deleteAll(Pagina.class);
        String message;

        inizio = System.currentTimeMillis();
        listaBeans = backend.elaboraGiorni();
        assertNotNull(listaBeans);
        message = String.format("Ci sono %s pagine e sotto-pagine per i giorni, elaborate in %s", textService.format(listaBeans.size()), dateService.deltaText(inizio));
        System.out.println(message);
        printBeans(listaBeans);
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

    void printBeans(List<Pagina> listaBeans) {
        System.out.println(VUOTA);
        int k = 0;

        for (Pagina bean : listaBeans) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.print(bean.pagina);
            System.out.print(SPAZIO);
            System.out.print(bean.voci);
            System.out.print(SPAZIO);
            System.out.println(bean.cancella);
        }
    }

}
