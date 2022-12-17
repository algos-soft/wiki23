package it.algos.integration.backend;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.packages.crono.secolo.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import java.util.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: Sun, 06-Nov-2022
 * Time: 20:10
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("backend")
@DisplayName("Secolo Backend")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SecoloBackendTest extends AlgosTest {

    /**
     * The Service.
     */
    @InjectMocks
    private SecoloBackend backend;

    @Autowired
    private SecoloRepository repository;


    private Secolo entityBean;


    private List<Secolo> listaBeans;

    /**
     * Qui passa una volta sola <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();

        Assertions.assertNotNull(backend);

        backend.repository = repository;
        backend.crudRepository = repository;
        backend.arrayService = arrayService;
        backend.reflectionService = reflectionService;
        backend.resourceService = resourceService;
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
    @DisplayName("2 - findAll (entity)")
    void findAll() {
        System.out.println("2 - findAll (entity)");
        String message;

        listaBeans = backend.findAll();
        assertNotNull(listaBeans);
        message = String.format("Ci sono in totale %s entities di %s", textService.format(listaBeans.size()), "Secolo");
        System.out.println(message);
        printBeans(listaBeans);
    }

    @Test
    @Order(3)
    @DisplayName("3 - findNomi (nome)")
    void findNomi() {
        System.out.println("3 - findNomi (nome)");
        String message;

        listaStr = backend.findNomi();
        assertNotNull(listaStr);
        message = String.format("Ci sono in totale %s secoli", textService.format(listaStr.size()));
        System.out.println(message);
        printNomiSecoli(listaStr);
    }

    @Test
    @Order(3)
    @DisplayName("3 - findNomi (nome)")
    void findNomi2() {
        System.out.println("3 - findNomi (nome)");
        String message;

        listaStr = backend.findNomi();
        assertNotNull(listaStr);
        message = String.format("Ci sono in totale %s secoli", textService.format(listaStr.size()));
        System.out.println(message);
        printNomiSecoli(listaStr);
    }


    @Test
    @Order(4)
    @DisplayName("4 - findNomiAscendenti (nome)")
    void findNomiAscendenti() {
        System.out.println("4 - findNomiAscendenti (nome)");
        String message;

        listaStr = backend.findNomiAscendenti();
        assertNotNull(listaStr);
        message = String.format("Ci sono in totale %s secoli", textService.format(listaStr.size()));
        System.out.println(message);
        printNomiSecoli(listaStr);
    }

    @Test
    @Order(40)
    @DisplayName("40 - reset")
    void reset() {
        System.out.println("40 - reset");
        String message;

//        ottenutoBooleano = backend.reset();
//        assertTrue(ottenutoBooleano);
//        listaBeans = backend.findAll();
//        assertNotNull(listaBeans);
//        message = String.format("Ci sono in totale %s entities di %s", textService.format(listaBeans.size()), "Secolo");
//        System.out.println(message);
//        printBeans(listaBeans);
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

    void printBeans(List<Secolo> listaBeans) {
        System.out.println(VUOTA);
        int k = 0;

        for (Secolo bean : listaBeans) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.print(bean.nome);
            System.out.print(SPAZIO);
            System.out.print(bean.inizio);
            System.out.print(SPAZIO);
            System.out.println(bean.fine);
        }
    }

    void printNomiSecoli(List<String> listaSecoli) {
        int k = 0;

        for (String secolo : listaSecoli) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.println(secolo);
        }
    }

}
