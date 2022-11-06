package it.algos.integration.backend;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.packages.crono.mese.*;
import it.algos.vaad23.backend.service.*;
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
 * Date: Mon, 24-Oct-2022
 * Time: 06:43
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("backend")
@DisplayName("Mese Backend")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MeseBackendTest extends AlgosTest {

    /**
     * The Service.
     */
    @InjectMocks
    private MeseBackend backend;

    @Autowired
    private MeseRepository repository;

    @Autowired
    public LogService logger;

    private Mese entityBean;


    private List<Mese> listaBeans;

    private String nomeFile;

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
        backend.logger = logger;

        this.nomeFile = "mesi";
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
        message = String.format("Ci sono in totale %s entities di %s", textService.format(listaBeans.size()), "Mese");
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
        message = String.format("Ci sono in totale %s mesi", textService.format(listaStr.size()));
        System.out.println(message);
        printNomiMesi(listaStr);
    }

    @Test
    @Order(4)
    @DisplayName("4 - resetServer")
    void resetServer() {
        System.out.println("4 - resetServer");
        String message;
        backend.deleteAll();

        listaBeans = backend.resetServer(nomeFile);
        assertNotNull(listaBeans);
        message = String.format("Ci sono in totale %s entities di %s", textService.format(listaBeans.size()), "Mese");
        System.out.println(message);
        printBeans(listaBeans);
    }

    @Test
    @Order(5)
    @DisplayName("5 - resetConfig")
    void resetConfig() {
        System.out.println("5 - resetConfig");
        String message;
        backend.deleteAll();

        listaBeans = backend.resetConfig(nomeFile);
        assertNotNull(listaBeans);
        message = String.format("Ci sono in totale %s entities di %s", textService.format(listaBeans.size()), "Mese");
        System.out.println(message);
        printBeans(listaBeans);
    }

    @Test
    @Order(6)
    @DisplayName("6 - reset")
    void reset() {
        System.out.println("6 - reset");
        String message;

        ottenutoBooleano = backend.reset();
        assertTrue(ottenutoBooleano);
        listaBeans = backend.findAll();
        assertNotNull(listaBeans);
        message = String.format("Ci sono in totale %s entities di %s", textService.format(listaBeans.size()), "Mese");
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

    void printBeans(List<Mese> listaBeans) {
        System.out.println(VUOTA);
        int k = 0;

        System.out.println("Nome, breve, giorni, primo, ultimo");
        System.out.println(VUOTA);

        for (Mese bean : listaBeans) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.print(bean.nome);
            System.out.print(SPAZIO);
            System.out.print(bean.breve);
            System.out.print(SPAZIO);
            System.out.print(bean.giorni);
            System.out.print(SPAZIO);
            System.out.print(bean.primo);
            System.out.print(SPAZIO);
            System.out.println(bean.ultimo);
        }
    }

    void printNomiMesi(List<String> listaMesi) {
        int k = 0;

        for (String mese : listaMesi) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.println(mese);
        }
    }

}
