package it.algos.integration.backend;

import it.algos.*;
import it.algos.base.*;
import it.algos.wiki23.backend.packages.pagina.*;
import org.junit.jupiter.api.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
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
    @DisplayName("3 - fixCognomiDiacritici")
    void fixCognomiDiacritici() {
        System.out.println("3 - fixCognomiDiacritici");
        String message;
        String tag = "Persone di cognome";

        List<String> listaStr = queryService.getList(tag);
        message = String.format("Ci sono in totale %s cognomi indifferenziati con o senza accento", textService.format(listaStr.size()));
        System.out.println(message);

        listaStr = backend.fixCognomiDiacritici(listaStr);
        assertNotNull(listaStr);
        message = String.format("Ci sono in totale %s cognomi con accenti diacritici", textService.format(listaStr.size()));
        System.out.println(message);
        System.out.println(VUOTA);
        print(listaStr);
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
