package it.algos.integration.backend;

import it.algos.*;
import it.algos.base.*;
import it.algos.wiki23.backend.packages.pagina.*;
import org.junit.jupiter.api.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Sun, 30-Oct-2022
 * Time: 20:54
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("backend")
@DisplayName("Pagina BackendCognomi")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PaginaBackendCognomiTest extends WikiTest {

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
        message = String.format("Ci sono in totale %s entities di %s", textService.format(listaBeans.size()), "PaginaBackend");
        System.out.println(message);
    }


    @Test
    @Order(3)
    @DisplayName("3 - listaAllPagine")
    void listaAllPagine() {
        System.out.println("3 - listaAllPagine");
        String message;
        String tag = "Persone di cognome";

        listaStr = queryService.getList(tag);
        assertNotNull(listaStr);
        System.out.println(VUOTA);
        message = String.format("Sul server ci sono in totale %d liste di cognomi indifferenziati con o senza accento", listaStr.size());
        System.out.println(message);
        print(listaStr);
    }

    @Test
    @Order(11)
    @DisplayName("11 - getCognomiAcriticiAll")
    void getCognomiAcriticiAll() {
        System.out.println("11 - getCognomiAcriticiAll");
        String message;
        String tag = "Persone di cognome";

        listaStr = queryService.getList(tag);
        assertNotNull(listaStr);
        System.out.println(VUOTA);
        message = String.format("Sul server ci sono in totale %d liste di cognomi indifferenziati con o senza accento", listaStr.size());
        System.out.println(message);

        listaStr = backend.getCognomiAcriticiAll(listaStr);
        assertNotNull(listaStr);
        message = String.format("Sul server ci sono %d liste di cognomi senza accenti diacritici con o senza un corrispondente acritico", listaStr.size());
        System.out.println(VUOTA);
        System.out.println(message);
        print(listaStr);
    }



    @Test
    @Order(12)
    @DisplayName("12 - getCognomiAcriticiSingoli")
    void getCognomiAcriticiSingoli() {
        System.out.println("12 - getCognomiAcriticiSingoli");
        String message;
        String tag = "Persone di cognome";

        listaStr = queryService.getList(tag);
        assertNotNull(listaStr);
        System.out.println(VUOTA);
        message = String.format("Sul server ci sono in totale %d liste di cognomi indifferenziati con o senza accento", listaStr.size());
        System.out.println(message);

        listaStr = backend.getCognomiAcriticiSingoli(listaStr);
        assertNotNull(listaStr);
        message = String.format("Sul server ci sono %d liste di cognomi senza accenti diacritici che non hanno un corrispondente diacritico", listaStr.size());
        System.out.println(VUOTA);
        System.out.println(message);
        print(listaStr);
    }


    @Test
    @Order(13)
    @DisplayName("13 - getCognomiAcriticiDoppi")
    void getCognomiAcriticiDoppi() {
        System.out.println("13 - getCognomiAcriticiDoppi");
        String message;
        String tag = "Persone di cognome";

        listaStr = queryService.getList(tag);
        assertNotNull(listaStr);
        System.out.println(VUOTA);
        message = String.format("Sul server ci sono in totale %d liste di cognomi indifferenziati con o senza accento", listaStr.size());
        System.out.println(message);

        listaStr = backend.getCognomiAcriticiDoppi(listaStr);
        assertNotNull(listaStr);
        message = String.format("Sul server ci sono %d liste di cognomi senza accenti diacritici che però hanno il corrispondente diacritico", listaStr.size());
        System.out.println(VUOTA);
        System.out.println(message);
        print(listaStr);
    }


    @Test
    @Order(21)
    @DisplayName("21 - getCognomiDiacriticiAll")
    void getCognomiDiacriticiAll() {
        System.out.println("21 - getCognomiDiacriticiAll");
        String message;
        String tag = "Persone di cognome";

        listaStr = queryService.getList(tag);
        assertNotNull(listaStr);
        System.out.println(VUOTA);
        message = String.format("Sul server ci sono in totale %d liste di cognomi indifferenziati con o senza accento", listaStr.size());
        System.out.println(message);

        listaStr = backend.getCognomiDiacriticiAll(listaStr);
        assertNotNull(listaStr);
        message = String.format("Sul server ci sono %d liste di cognomi con accenti diacritici con o senza corrispondente acritico", listaStr.size());
        System.out.println(VUOTA);
        System.out.println(message);
        print(listaStr);
    }

    @Test
    @Order(22)
    @DisplayName("22 - getCognomiDiacriticiSingoli")
    void getCognomiDiacriticiSingoli() {
        System.out.println("22 - getCognomiDiacriticiSingoli");
        String message;
        String tag = "Persone di cognome";

        listaStr = queryService.getList(tag);
        assertNotNull(listaStr);
        System.out.println(VUOTA);
        message = String.format("Sul server ci sono in totale %d liste di cognomi indifferenziati con o senza accento", listaStr.size());
        System.out.println(message);

        listaStr = backend.getCognomiDiacriticiSingoli(listaStr);
        assertNotNull(listaStr);
        message = String.format("Sul server ci sono %d liste di cognomi con accenti diacritici che non hanno il corrispondente acritico", listaStr.size());
        System.out.println(VUOTA);
        System.out.println(message);
        print(listaStr);
    }


    @Test
    @Order(23)
    @DisplayName("23 - getCognomiDiacriticiDoppi")
    void getCognomiDiacriticiDoppi() {
        System.out.println("23 - getCognomiDiacriticiDoppi");
        String message;
        String tag = "Persone di cognome";

        listaStr = queryService.getList(tag);
        assertNotNull(listaStr);
        System.out.println(VUOTA);
        message = String.format("Sul server ci sono in totale %d liste di cognomi indifferenziati con o senza accento", listaStr.size());
        System.out.println(message);

        listaStr = backend.getCognomiDiacriticiDoppi(listaStr);
        assertNotNull(listaStr);
        message = String.format("Sul server ci sono %d liste di cognomi con accenti diacritici che hanno anche il corrispondente acritico", listaStr.size());
        System.out.println(VUOTA);
        System.out.println(message);
        print(listaStr);
    }


    //    @Test
    @Order(8)
    @DisplayName("8 - elaboraCognomi")
    void elaboraCognomi() {
        System.out.println("8 - elaboraCognomi");
        backend.elaboraCognomi();
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

    void printBeans(List<PaginaBackend> listaBeans) {
        System.out.println(VUOTA);
        int k = 0;

        for (PaginaBackend bean : listaBeans) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.println(bean);
        }
    }

}
