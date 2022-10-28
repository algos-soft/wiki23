package it.algos.integration.backend;

import it.algos.*;
import it.algos.base.*;
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
    @DisplayName("1 - count")
    void count() {
        System.out.println("1 - count");
        String message;

        ottenutoIntero = backend.count();
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
        message = String.format("Ci sono in totale %s entities di %s", textService.format(listaBeans.size()), "Pagina");
        System.out.println(message);
    }


    @Test
    @Order(3)
    @DisplayName("3 - getListaPagine singola")
    void getListaPagine() {
        System.out.println("3 - getListaPagine singola");
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
        message = String.format("Ci sono %s pagine che iniziano con %s", textService.format(ottenutoArray.size()), sorgenteArray);
        System.out.println(message);
        System.out.println(VUOTA);
        print(ottenutoArray);
    }

    @Test
    @Order(4)
    @DisplayName("4 - getTagGiorni")
    void getTagGiorni() {
        System.out.println("4 - getTagGiorni");
        System.out.println(VUOTA);
        String message;
        previstoIntero = 4;

        listaStr = backend.getTagGiorni();
        assertNotNull(listaStr);
        assertEquals(previstoIntero, listaStr.size());
        message = String.format("Ci sono %s tag per i giorni", listaStr);
        System.out.println(message);
        System.out.println(VUOTA);
        print(listaStr);
    }

    @Test
    @Order(5)
    @DisplayName("5 - getListaPagine array tag")
    void getListaPagine2() {
        System.out.println("5 - getListaPagine array tag");
        String message;

        ottenutoArray = backend.getListaPagine(null);
        assertNotNull(ottenutoArray);
        assertTrue(ottenutoArray.size() == 0);

        sorgenteArray = backend.getTagGiorni();
        ottenutoArray = backend.getListaPagine(sorgenteArray);
        assertNotNull(ottenutoArray);
        assertTrue(ottenutoArray.size() > 0);
        System.out.println(VUOTA);
        message = String.format("Ci sono %s pagine che iniziano con %s", textService.format(ottenutoArray.size()), sorgenteArray);
        System.out.println(message);
        System.out.println(VUOTA);
        print(ottenutoArray);
    }

    @Test
    @Order(6)
    @DisplayName("6 - getPagineGiorni")
    void getPagineGiorni() {
        System.out.println("6 - getPagineGiorni");
        String message;
        previstoIntero = 735;

        listaStr = backend.getPagineGiorni();
        assertNotNull(listaStr);
        assertEquals(previstoIntero, listaStr.size());
        message = String.format("Ci sono %s pagine per i giorni", textService.format(listaStr.size()));
        System.out.println(message);
        System.out.println(VUOTA);
        print(listaStr);
    }


//    @Test
    @Order(11)
    @DisplayName("11 - elaboraGiorniPagine")
    void elaboraGiorniPagine() {
        System.out.println("11 - elaboraGiorniPagine");
        mongoService.deleteAll(Pagina.class);
        String message;
        List<String> valideBase = giornoWikiBackend.findAllPagine();
        List<String> pagineAllGiorni = backend.getPagineGiorni();
        List<String> pagine = backend.getGiorniAnni(pagineAllGiorni);

        inizio = System.currentTimeMillis();
        listaBeans = backend.elaboraGiorniPagine(valideBase, pagine);
        assertNotNull(listaBeans);
        message = String.format("Ci sono %s pagine per i giorni, elaborate in %s", textService.format(listaBeans.size()),dateService.deltaText(inizio));
        System.out.println(message);
        printBeans(listaBeans);
    }


    @Test
    @Order(12)
    @DisplayName("12 - elaboraGiorniSottoPagine")
    void elaboraGiorniSottoPagine() {
        System.out.println("12 - elaboraGiorniSottoPagine");
        mongoService.deleteAll(Pagina.class);
        String message;
        List<String> valideBase = giornoWikiBackend.findAllPagine();
        List<String> pagineAllGiorni = backend.getPagineGiorni();
        List<String> pagineSotto = backend.getGiorniAnniSotto(pagineAllGiorni);

        inizio = System.currentTimeMillis();
        listaBeans = backend.elaboraGiorniSottoPagine(valideBase, pagineSotto);
        assertNotNull(listaBeans);
        message = String.format("Ci sono %s sotto-pagine per i giorni, elaborate in %s", textService.format(listaBeans.size()),dateService.deltaText(inizio));
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
        message = String.format("Ci sono %s pagine e sotto-pagine per i giorni, elaborate in %s", textService.format(listaBeans.size()),dateService.deltaText(inizio));
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
