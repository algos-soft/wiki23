package it.algos.integration.backend;

import it.algos.*;
import it.algos.base.*;
import it.algos.wiki23.backend.packages.nazionalita.*;
import org.junit.jupiter.api.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import java.util.*;
import java.util.stream.*;

import org.springframework.data.domain.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Wed, 06-Jul-2022
 * Time: 14:26
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("production")
@Tag("backend")
@DisplayName("Nazionalita Backend")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NazionalitaBackendTest extends WikiTest {


    @Autowired
    private NazionalitaRepository repository;

    protected List<Nazionalita> listaBeans;

    private Nazionalita entityBean;

    //--nome singolare
    //--esiste
    protected static Stream<Arguments> NAZIONALITA_SINGOLARE() {
        return Stream.of(
                Arguments.of(VUOTA, false),
                Arguments.of("turco", true),
                Arguments.of("errata", false),
                Arguments.of("tedesca", true),
                Arguments.of("tedeschi", false),
                Arguments.of("direttore di scena", false),
                Arguments.of("brasiliano", true)

        );
    }

    //--nome plurale
    //--esiste
    protected static Stream<Arguments> NAZIONALITA_PLURALI() {
        return Stream.of(
                Arguments.of(VUOTA,false),
                Arguments.of("tedesca",false),
                Arguments.of("britannici",true),
                Arguments.of("tedesco",false),
                Arguments.of("tedeschi",true),
                Arguments.of("non esiste",false)
        );
    }

    /**
     * Qui passa una volta sola <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();

        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(backend);
        Assertions.assertNotNull(backend);

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
        message = String.format("Ci sono in totale %s entities di %s", textService.format(listaBeans.size()), "Nazionalita");
        System.out.println(message);
    }


    @ParameterizedTest
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
        System.out.println(String.format("Le prime %s nazionalità ordinate per '%s' %s", num, property, direction));
        printBeans(listaBeans.subList(0, num));
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
    @DisplayName("5 - findNazionalitaDistinctByPlurali (Nazionalita nazionalità)")
    void allDistinct() {
        System.out.println("5 - findNazionalitaDistinctByPlurali (Nazionalita nazionalità)");
        listaBeans = backend.findNazionalitaDistinctByPlurali();
        assertNotNull(listaBeans);
        printPlurali(listaBeans);
    }

    @ParameterizedTest
    @MethodSource(value = "NAZIONALITA_SINGOLARE")
    @Order(6)
    @DisplayName("6 - isExist")
    void isExist(final String singolare, final boolean esiste) {
        System.out.println("6 - isExist");
        ottenutoBooleano = backend.isExist(singolare);
        assertEquals(esiste, ottenutoBooleano);
        if (ottenutoBooleano) {
            System.out.println(String.format("La nazionalità '%s' esiste", singolare));
        }
        else {
            System.out.println(String.format("La nazionalità '%s' non esiste", singolare));
        }
    }

    @ParameterizedTest
    @MethodSource(value = "NAZIONALITA_SINGOLARE")
    @Order(7)
    @DisplayName("7 - findBySingolare")
        //--nome singolare
        //--esiste
    void findBySingolare(String singolare, boolean esiste) {
        System.out.println("7 - findBySingolare");
        entityBean = backend.findFirstBySingolare(singolare);
        assertEquals(entityBean != null, esiste);
        if (esiste) {
            System.out.println(String.format("La nazionalità '%s' esiste", singolare));
        }
        else {
            System.out.println(String.format("La nazionalità '%s' non esiste", singolare));
        }
    }


    @ParameterizedTest
    @MethodSource(value = "NAZIONALITA_PLURALI")
    @Order(8)
    @DisplayName("8 - findByPlurale e trova 'nazionalità'")
        //--nome plurale
        //--esiste
    void findByPlurale(String plurale, boolean esiste) {
        System.out.println("8 - findByPlurale e trova 'nazionalità'");
        entityBean = backend.findFirstByPlurale(plurale);
        assertEquals(entityBean != null, esiste);
        if (esiste) {
            System.out.println(String.format("La nazionalità '%s' esiste", plurale));
        }
        else {
            System.out.println(String.format("La nazionalità '%s' non esiste", plurale));
        }
        listaBeans = backend.findAllByPlurale(plurale);
        assertNotNull(listaBeans);
        assertEquals(listaBeans.size() > 0, esiste);
        printSingolariNazionalita(plurale, listaBeans);
    }


    @ParameterizedTest
    @MethodSource(value = "NAZIONALITA_PLURALI")
    @Order(9)
    @DisplayName("9 - findSingolariByPlurale e trova 'singolari'")
        //--nome plurale
        //--esiste
    void findSingolariByPlurale(String plurale, boolean esiste) {
        System.out.println("9 - findSingolariByPlurale e trova 'singolari'");
        listaStr = backend.findSingolariByPlurale(plurale);
        assertNotNull(listaStr);
        assertEquals(listaStr.size() > 0, esiste);
        printAllSingolari(plurale,  listaStr,"nazionalità");
    }


    @Test
    @Order(10)
    @DisplayName("10 - findMappaSingolariByPlurale")
    void findMappaByPlurali() {
        System.out.println("10 - findMappaSingolariByPlurale");
        mappa = backend.findMappaSingolariByPlurale();
        assertNotNull(mappa);
        printMappa(mappa, "di nazionalità plurali con le relative nazionalità singolari");
    }


    @ParameterizedTest
    @MethodSource(value = "NAZIONALITA_SINGOLARE")
    @Order(11)
    @DisplayName("11 - countBySingolare")
    void countBySingolare(String singolare, boolean esiste) {
        System.out.println("11 - countBySingolare");
        ottenutoIntero = bioBackend.countNazionalita(singolare);
        System.out.println(String.format("La nazionalità '%s' contiene %s voci biografiche", singolare, ottenutoIntero));
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
    void printPlurali(List<Nazionalita> listaNazionalita) {
        System.out.println(String.format("Ci sono %d nazionalità plurali distinte", listaNazionalita.size()));
        System.out.println(VUOTA);
        int k = 0;

        for (Nazionalita nazionalita : listaNazionalita) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.println(nazionalita.pluraleLista);
        }
    }

    void printBeans(List<Nazionalita> listaBeans) {
        System.out.println(VUOTA);
        int k = 0;

        for (Nazionalita bean : listaBeans) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.println(bean);
        }
    }
    void printSingolariNazionalita(String plurale, List<Nazionalita> listaNazionalita) {
        System.out.println(String.format("Ci sono %d nazionalita singolari per %s", listaNazionalita.size(), plurale));
        System.out.println(VUOTA);

        for (Nazionalita nazionalita : listaNazionalita) {
            System.out.println(nazionalita.singolare);
        }
    }

}
