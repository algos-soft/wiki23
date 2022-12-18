package it.algos.backend;

import it.algos.*;
import it.algos.base.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.genere.*;
import org.junit.jupiter.api.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import java.util.*;
import java.util.stream.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Sun, 10-Jul-2022
 * Time: 19:44
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("production")
@Tag("backend")
@DisplayName("Genere Backend")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GenereBackendTest extends WikiTest {

    /**
     * The Service.
     */
    @InjectMocks
    private GenereBackend backend;

    @Autowired
    private GenereRepository repository;


    private Genere entityBean;


    private List<Genere> listaBeans;

    //--genere singolare
    //--tipologia di genere
    //--risultato ricerca corrispondente genere plurale maschile
    //--risultato ricerca corrispondente genere plurale femminile
    //--risultato ricerca corrispondente genere plurale indifferenziato con type
    //--risultato ricerca corrispondente genere plurale senza specificare maschile o femminile
    protected static Stream<Arguments> GENERE_PLURALI() {
        return Stream.of(
                Arguments.of("abate", AETypeGenere.maschile, "abati", VUOTA, "abati", "abati"),
                Arguments.of("badessa", AETypeGenere.femminile, VUOTA, "badesse", "badesse", "badesse"),
                Arguments.of("agronomo", AETypeGenere.maschile, "agronomi", VUOTA, "agronomi", "agronomi"),
                Arguments.of("agronoma", AETypeGenere.femminile, VUOTA, "agronome", "agronome", "agronome"),
                Arguments.of("alchimista", AETypeGenere.maschile, "alchimisti", "alchimiste", "alchimisti", VUOTA),
                Arguments.of("alchimista", AETypeGenere.femminile, "alchimisti", "alchimiste", "alchimiste", VUOTA)
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
        message = String.format("Ci sono in totale %s entities di %s", textService.format(listaBeans.size()), "Genere");
        System.out.println(message);
    }


    @Test
    @Order(3)
    @DisplayName("3 - singolari")
    void findSingolari() {
        System.out.println("3 - singolari");
        String message;

        listaStr = backend.findAllSingolari();
        assertNotNull(listaStr);
        message = String.format("Ci sono in totale %s valori singolari di %s", textService.format(listaStr.size()), "Genere");
        System.out.println(message);
        printString(listaStr, 10);
    }

    @Test
    @Order(4)
    @DisplayName("4 - plurali maschili distinti")
    void findAllPluraliMaschili() {
        System.out.println("4 - plurali maschili distinti");
        String message;

        listaStr = backend.findAllPluraliMaschili();
        assertNotNull(listaStr);
        message = String.format("Ci sono in totale %s valori plurali maschili distinti", textService.format(listaStr.size()));
        System.out.println(message);
        printString(listaStr, 10);
    }


    @Test
    @Order(5)
    @DisplayName("5 - plurali femminili distinti")
    void findAllPluraliFemminili() {
        System.out.println("5 - plurali femminili distinti");
        String message;

        listaStr = backend.findAllPluraliFemminili();
        assertNotNull(listaStr);
        message = String.format("Ci sono in totale %s valori plurali femminili distinti", textService.format(listaStr.size()));
        System.out.println(message);
        printString(listaStr, 10);
    }


    @Test
    @Order(6)
    @DisplayName("6 - plurali all distinti")
    void findAllPlural() {
        System.out.println("6 - plurali all distinti");
        String message;

        listaStr = backend.findAllPluraliDistinti();
        assertNotNull(listaStr);
        message = String.format("Ci sono in totale %s valori plurali distinti (maschili + femminili)", textService.format(listaStr.size()));
        System.out.println(message);
        printString(listaStr, 20);
    }


    @Test
    @Order(7)
    @DisplayName("7 - delta singolari versus plurali distinti")
    void delta() {
        System.out.println("7 - delta singolari versus plurali distinti");
        String message;
        List<String> listaUno;
        List<String> listaDue;
        List<String> listaTre;
        listaUno = backend.findAllSingolari();
        listaDue = backend.findAllPluraliDistinti();

        listaTre = new ArrayList<>();
        for (String nome : listaUno) {
            if (listaDue.contains(nome)) {
                listaTre.add(nome);
            }
        }

        assertNotNull(listaTre);
        message = String.format("Ci sono in totale %s valori differenti tra singolari versus plurali distinti", textService.format(listaTre.size()));
        System.out.println(message);
        printString(listaTre, 100);
    }


    @ParameterizedTest
    @MethodSource(value = "GENERE_PLURALI")
    @Order(8)
    @DisplayName("8 - find plurale")
        //--genere singolare
        //--tipologia di genere
        //--risultato ricerca corrispondente genere plurale maschile
        //--risultato ricerca corrispondente genere plurale femminile
        //--risultato ricerca corrispondente genere plurale indifferenziato con type
        //--risultato ricerca corrispondente genere plurale senza specificare maschile o femminile
    void findPlurale(String singolare, AETypeGenere type, String pluraleMaschile, String pluraleFemminile, String pluraleType,String pluraleIndifferenziato) {
        System.out.println("8 - find plurale");
        sorgente = singolare;

        ottenuto = backend.getPluraleMaschile(sorgente);
        assertEquals(pluraleMaschile, ottenuto);
        System.out.println(VUOTA);
        System.out.println("Maschile");
        System.out.println(String.format("%s%s%s", sorgente, FORWARD, ottenuto));

        ottenuto = backend.getPluraleFemminile(sorgente);
        assertEquals(pluraleFemminile, ottenuto);
        System.out.println(VUOTA);
        System.out.println("Femminile");
        System.out.println(String.format("%s%s%s", sorgente, FORWARD, ottenuto));

        ottenuto = backend.getPlurale(sorgente,type);
        assertEquals(pluraleType, ottenuto);
        System.out.println(VUOTA);
        System.out.println("Type");
        System.out.println(String.format("%s%s%s", sorgente, FORWARD, ottenuto));

        ottenuto = backend.getPlurale(sorgente);
        assertEquals(pluraleIndifferenziato, ottenuto);
        System.out.println(VUOTA);
        System.out.println("Indifferenziato");
        System.out.println(String.format("%s%s%s", sorgente, FORWARD, ottenuto));
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

    void printBeans(List<Genere> listaBeans) {
        System.out.println(VUOTA);
        int k = 0;

        for (Genere bean : listaBeans) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.println(bean);
        }
    }

}