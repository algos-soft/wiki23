package it.algos.integration.backend;

import it.algos.*;
import it.algos.base.*;
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
@DisplayName("Attivita backend")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AttivitaBackendTest extends WikiTest {


    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    @InjectMocks
    private AttivitaBackend backend;

    @Autowired
    private AttivitaRepository repository;

    protected List<Attivita> listaBeans;


    private Attivita entityBean;

    //--nome singolare
    //--esiste
    protected static Stream<Arguments> ATTIVITA_SINGOLARE() {
        return Stream.of(
                Arguments.of(VUOTA, false),
                Arguments.of("politico", true),
                Arguments.of("errata", false),
                Arguments.of("attrice", true),
                Arguments.of("direttore di scena", false),
                Arguments.of("vescovo ariano", true)

        );
    }

    //--nome plurale
    //--esiste
    protected static Stream<Arguments> ATTIVITA_PLURALI() {
        return Stream.of(
                Arguments.of(VUOTA, false),
                Arguments.of("politici", true),
                Arguments.of("fantasmi", false),
                Arguments.of("attori", true),
                Arguments.of("nessuna", false),
                Arguments.of("accademici", true)
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

        listaBeans = null;
        listaStr = null;
        mappa = null;
        entityBean = null;
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

        listaBeans = backend.findAll();
        assertNotNull(listaBeans);
        message = String.format("Ci sono in totale %s attività", textService.format(listaBeans.size()));
        System.out.println(message);
        printSingolari(listaBeans);
    }

//    @ParameterizedTest
    @MethodSource(value = "SORT")
    @Order(3)
    @DisplayName("3 - findAll sort")
        //--direzione
        //--property
    void findAllSort(final Sort.Direction direction, final String property) {
        System.out.println("3 - findAll sort");
        int num = 10;
        Sort sort = Sort.by(direction, property);

        listaBeans = backend.findAll(sort);
        assertNotNull(listaBeans);
        System.out.println(String.format("Le prime %s attività ordinate per '%s' %s", num, property, direction));
        printSingolari(listaBeans.subList(0, num));
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
    @DisplayName("5 - findAttivitaDistinctByPlurali (Attivita attività)")
    void allDistinct() {
        System.out.println("5 - findAttivitaDistinctByPlurali (Attivita attività)");
        listaBeans = backend.findAttivitaDistinctByPluraliOld();
        assertNotNull(listaBeans);
        printPlurali(listaBeans);
    }

    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_SINGOLARE")
    @Order(6)
    @DisplayName("6 - isExist")
        //--nome singolare
        //--esiste
    void isExist(final String singolare, final boolean esiste) {
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
        //--nome singolare
        //--esiste
    void findBySingolare(String singolare, boolean esiste) {
        System.out.println("7 - findBySingolare");
        entityBean = backend.findFirstBySingolare(singolare);
        assertEquals(entityBean != null, esiste);
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
    @DisplayName("8 - findByPlurale e trova 'attività'")
        //--nome plurale
        //--esiste
    void findByPlurale(String plurale, boolean esiste) {
        System.out.println("8 - findByPlurale e trova 'attività'");
        entityBean = backend.findFirstByPlurale(plurale);
        assertEquals(entityBean != null, esiste);
        if (esiste) {
            System.out.println(String.format("L'attività '%s' esiste", plurale));
        }
        else {
            System.out.println(String.format("L'attività '%s' non esiste", plurale));
        }
        listaBeans = backend.findAllByPagina(plurale);
        assertNotNull(listaBeans);
        assertEquals(listaBeans.size() > 0, esiste);
        printSingolariAttivita(plurale, listaBeans);
    }

    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_PLURALI")
    @Order(9)
    @DisplayName("9 - findSingolariByPlurale e trova 'singolari'")
        //--nome plurale
        //--esiste
    void findSingolariByPlurale(String plurale, boolean esiste) {
        System.out.println("9 - findSingolariByPlurale e trova 'singolari'");
        listaStr = backend.findSingolariByPlurale(plurale);
        assertNotNull(listaStr);
        assertEquals(listaStr.size() > 0, esiste);
        printAllSingolari(plurale,  listaStr,"attività");
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

    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_SINGOLARE")
    @Order(11)
    @DisplayName("11 - countBySingolare")
    void countBySingolare(String singolare, boolean esiste) {
        System.out.println("11 - countBySingolare");
        ottenutoIntero = bioBackend.countAttivita(singolare);
        System.out.println(String.format("L'attività '%s' contiene %s voci biografiche", singolare, ottenutoIntero));
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


    void printPlurali(List<Attivita> listaAttivita) {
        System.out.println(String.format("Ci sono %d attività plurali distinte", listaAttivita.size()));
        System.out.println(VUOTA);
        int k = 0;

        for (Attivita attivita : listaAttivita) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.println(attivita.pagina);
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