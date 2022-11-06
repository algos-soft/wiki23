package it.algos.integration.backend;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.packages.crono.giorno.*;
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
 * Date: sab, 07-mag-2022
 * Time: 14:50
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("backend")
@DisplayName("Giorno Backend")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GiornoBackendTest extends AlgosTest {

    /**
     * The Service.
     */
    @InjectMocks
    private GiornoBackend backend;

    @Autowired
    private GiornoRepository repository;

    @Autowired
    private MeseRepository meseRepository;

    protected List<Giorno> listaGiorni;

    @Autowired
    private MeseBackend meseBackend;

    @Autowired
    protected MongoService mongoService;

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
        backend.meseBackend = meseBackend;
        meseBackend.repository = meseRepository;
        backend.mongoService = mongoService;
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

        listaGiorni = backend.findAll();
        assertNotNull(listaGiorni);
        message = String.format("Ci sono in totale %s giorni", textService.format(listaGiorni.size()));
        System.out.println(message);
        printGiorni(listaGiorni);
    }

    @Test
    @Order(3)
    @DisplayName("3 - findNomi (nome)")
    void findNomi() {
        System.out.println("3 - findNomi (nome)");
        String message;

        listaStr = backend.findNomi();
        assertNotNull(listaStr);
        message = String.format("Ci sono in totale %s giorni", textService.format(listaStr.size()));
        System.out.println(message);
        printNomiGiorni(listaStr);
    }

    @Test
    @Order(4)
    @DisplayName("4 - findAllByMese (entity)")
    void findAllByMese() {
        System.out.println("4 - findAllByMese (entity)");

        for (Mese sorgente : meseBackend.findAll()) {
            listaGiorni = backend.findAllByMese(sorgente);
            assertNotNull(listaGiorni);
            message = String.format("Nel mese di %s ci sono %s giorni", sorgente, textService.format(listaGiorni.size()));
            System.out.println(VUOTA);
            System.out.println(message);
            printGiorni(listaGiorni);
        }
    }
    @Test
    @Order(5)
    @DisplayName("5 - findNomiByMese (nome)")
    void findNomiByMese() {
        System.out.println("5 - findNomiByMese (nome)");

        for (String sorgente : meseBackend.findNomi()) {
            listaStr = backend.findNomiByMese(sorgente);
            assertNotNull(listaStr);
            message = String.format("Nel mese di %s ci sono %s giorni", sorgente, textService.format(listaStr.size()));
            System.out.println(VUOTA);
            System.out.println(message);
            printNomiGiorni(listaStr);
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

    void printGiorni(List<Giorno> listaGiorni) {
        int k = 0;

        for (Giorno giorno : listaGiorni) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.print(giorno.nome);
            System.out.print(SPAZIO);
            System.out.print(giorno.trascorsi);
            System.out.print(SPAZIO);
            System.out.println(giorno.mancanti);
        }
    }

    void printNomiGiorni(List<String> listaGiorni) {
        int k = 0;

        for (String giorno : listaGiorni) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.println(giorno);
        }
    }

}
