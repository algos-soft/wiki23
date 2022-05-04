package it.algos.unit.backend;

import it.algos.*;
import it.algos.test.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.packages.attivita.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.data.domain.*;

import java.util.*;
import java.util.stream.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: mar, 03-mag-2022
 * Time: 10:10
 * <p>
 * Unit test di una classe di servizio (di norma) <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("backend")
@DisplayName("Attivita service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AttivitaBackendTest extends WTest {


    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    @InjectMocks
    private AttivitaBackend backend;

    @Autowired
    private AttivitaRepository repository;

    protected List<Attivita> listaAttivita;

    public static final String SINGOLARE = "singolare";

    public static final String PLURALE = "plurale";

    private Attivita attivita;

    //--nome singolare
    //--esiste
    protected static Stream<Arguments> ATTIVITA_SINGOLARE() {
        return Stream.of(
                Arguments.of(VUOTA, false),
                Arguments.of("politico", true),
                Arguments.of("errata", false),
                Arguments.of("attrice", true),
                Arguments.of("direttore di scena", false)
        );
    }

    //--nome plurale
    //--esiste
    protected static Stream<Arguments> ATTIVITA_PLURALI() {
        return Stream.of(
                Arguments.of(VUOTA),
                Arguments.of("politici"),
                Arguments.of("attori"),
                Arguments.of("accademici")
        );
    }

    //--direzione
    //--property
    protected static Stream<Arguments> SORT() {
        return Stream.of(
                Arguments.of(Sort.Direction.ASC, SINGOLARE),
                Arguments.of(Sort.Direction.DESC, SINGOLARE),
                Arguments.of(Sort.Direction.ASC, PLURALE),
                Arguments.of(Sort.Direction.DESC, PLURALE)
        );
    }

    /**
     * Execute only once before running all tests <br>
     * Esegue una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpAll() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();

        backend.repository = repository;
        backend.crudRepository = repository;
        backend.arrayService = arrayService;
        backend.reflectionService = reflectionService;
    }


    /**
     * Qui passa prima di ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUpEach() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    protected void setUpEach() {
        super.setUpEach();

        listaAttivita = null;
        listaStr = null;
        mappa = null;
        attivita = null;
    }


    @Test
    @Order(1)
    @DisplayName("1 - count")
    void count() {
        System.out.println("1 - count");
        String message;

        ottenutoIntero = backend.count();
        assertTrue(ottenutoIntero > 0);
        message = String.format("Ci sono in totale %s attività", textService.format(ottenutoIntero));
        System.out.println(message);
    }

    @Test
    @Order(2)
    @DisplayName("2 - findAll")
    void findAll() {
        System.out.println("2 - findAll");
        String message;

        listaAttivita = backend.findAll();
        assertNotNull(listaAttivita);
        message = String.format("Ci sono in totale %s attività", textService.format(listaAttivita.size()));
        System.out.println(message);
        printSingolari(listaAttivita);
    }

    @ParameterizedTest
    @MethodSource(value = "SORT")
    @Order(3)
    @DisplayName("3 - findAll sort")
        //--direzione
        //--property
    void findAllSort(Sort.Direction direction, String property) {
        System.out.println("3 - findAll sort");
        int num = 10;
        Sort sort = Sort.by(direction, property);

        listaAttivita = backend.findAll(sort);
        assertNotNull(listaAttivita);
        System.out.println(String.format("Le prime %s attività ordinate per '%s' %s", num, property, direction));
        printSingolari(listaAttivita.subList(0, num));
        System.out.println(VUOTA);
        System.out.println(VUOTA);
    }


    @Test
    @Order(4)
    @DisplayName("4 - findAllPlurali")
    void allPlurali() {
        System.out.println("4 - findAllPlurali (String plurale)");
        listaStr = backend.findAllPlurali();
        assertNotNull(listaStr);
        print(listaStr, "di tutti i plurali distinti");
    }

    @Test
    @Order(5)
    @DisplayName("5 - findAttivitaDistinctByPlurali")
    void allDistinct() {
        System.out.println("5 - findAttivitaDistinctByPlurali (Attivita attività)");
        listaAttivita = backend.findAttivitaDistinctByPlurali();
        assertNotNull(listaAttivita);
        printPlurali(listaAttivita);
    }

    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_SINGOLARE")
    @Order(6)
    @DisplayName("6 - isExist")
    void isExist(String singolare, boolean esiste) {
        System.out.println("6 - isExist");
        ottenutoBooleano = backend.isExist(singolare);
        assertEquals(esiste, ottenutoBooleano);
        if (ottenutoBooleano) {
            System.out.println(String.format("L'attività '%s' esiste", singolare));
        }
        else {
            System.out.println(String.format("L'attività '%s' non esiste", singolare));
        }
    }

    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_SINGOLARE")
    @Order(7)
    @DisplayName("7 - findBySingolare")
    void findBySingolare(String singolare, boolean esiste) {
        System.out.println("7 - findBySingolare");
        attivita = backend.findBySingolare(singolare);
        assertEquals(attivita != null, esiste);
        if (esiste) {
            System.out.println(String.format("L'attività '%s' esiste", singolare));
        }
        else {
            System.out.println(String.format("L'attività '%s' non esiste", singolare));
        }
    }

    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_PLURALI")
    @Order(8)
    @DisplayName("8 - findByPlurale")
    void findByPlurale(String plurale) {
        System.out.println("8 - findByPlurale");
        listaAttivita = backend.findByPlurale(plurale);
        assertNotNull(listaAttivita);
        printSingolariAttivita(plurale, listaAttivita);
    }

    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_PLURALI")
    @Order(9)
    @DisplayName("9 - findSingolariByPlurale")
    void findSingolariByPlurale(String plurale) {
        System.out.println("9 - findSingolariByPlurale");
        listaStr = backend.findSingolariByPlurale(plurale);
        assertNotNull(listaStr);
        printAllSingolari(plurale, listaStr);
    }


    @Test
    @Order(10)
    @DisplayName("10 - findMappaSingolariByPlurale")
    void findMappaByPlurali() {
        System.out.println("10 - findMappaSingolariByPlurale");
        mappa = backend.findMappaSingolariByPlurale();
        assertNotNull(mappa);
        printMappa(mappa, "di attività plurali con le relative attività singolari");
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

    void printSingolariAttivita(String plurale, List<Attivita> listaAttivita) {
        System.out.println(String.format("Ci sono %d attività singolari per %s", listaAttivita.size(), plurale));
        System.out.println(VUOTA);

        for (Attivita attivita : listaAttivita) {
            System.out.println(attivita.singolare);
        }
    }

    void printAllSingolari(String plurale, List<String> listaSingolari) {
        System.out.println(String.format("Ci sono %d attività singolari per %s", listaSingolari.size(), plurale));
        System.out.println(VUOTA);

        for (String singolare : listaSingolari) {
            System.out.println(singolare);
        }
    }

    void printPlurali(List<Attivita> listaAttivita) {
        System.out.println(String.format("Ci sono %d attività plurali distinte", listaAttivita.size()));
        System.out.println(VUOTA);
        int k = 0;

        for (Attivita attivita : listaAttivita) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.println(attivita.plurale);
        }
    }

    void printSingolari(List<Attivita> listaAttivita) {
        System.out.println(VUOTA);
        int k = 0;

        for (Attivita attivita : listaAttivita) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.println(attivita.singolare);
        }
    }

}