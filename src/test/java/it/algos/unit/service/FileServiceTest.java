package it.algos.unit.service;

import it.algos.test.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.service.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.provider.*;

import java.util.stream.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: dom, 13-mar-2022
 * Time: 08:11
 * <p>
 * Unit test di una classe di servizio (di norma) <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@SpringBootTest(classes = {SimpleApplication.class})
@Tag("quickly")
@DisplayName("File service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileServiceTest extends ATest {


    private static String PATH_DIRECTORY_TEST = "/Users/gac/Desktop/test/";

    private static String PATH_DIRECTORY_UNO = PATH_DIRECTORY_TEST + "Pippo/";

    private static String PATH_DIRECTORY_DUE = PATH_DIRECTORY_TEST + "Possibile/";

    private static String PATH_DIRECTORY_TRE = PATH_DIRECTORY_TEST + "Mantova/";

    private static String PATH_DIRECTORY_NON_ESISTENTE = PATH_DIRECTORY_TEST + "Genova/";

    private static String PATH_DIRECTORY_DA_COPIARE = PATH_DIRECTORY_TEST + "NuovaDirectory/";

    private static String PATH_DIRECTORY_MANCANTE = PATH_DIRECTORY_TEST + "CartellaCopiata/";

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private FileService service;

    //--path
    //--esiste
    protected static Stream<Arguments> DIRECTORY() {
        return Stream.of(
                Arguments.of(null, false),
                Arguments.of(VUOTA, false),
                Arguments.of("/Users/gac/Desktop/test/", false),
                Arguments.of("/Users/gac/Desktop/test/Mantova", false),
                Arguments.of("/Users/gac/Desktop/test/Mantova.txt", false),
                Arguments.of("Users/gac/Documents/IdeaProjects/operativi/vaadin23/src/", false),
                Arguments.of("/Users/gac/Documents/IdeaProjects/operativi/vaadin23/src/", true),
                Arguments.of("/Users/gac/Desktop/test/Pippo/", false)
        );
    }


    //--path
    //--esiste
    protected static Stream<Arguments> FILE() {
        return Stream.of(
                Arguments.of(null, false),
                Arguments.of(VUOTA, false),
                Arguments.of("/Users/gac/Desktop/test/Mantova/", false),
                Arguments.of("/Users/gac/Desktop/test/Mantova", false),
                Arguments.of("/Users/gac/Desktop/test/Mantova.", false),
                Arguments.of("/Users/gac/Desktop/test/Mantova.tx", false),
                Arguments.of("/Users/gac/Desktop/test/Mantova.txt", false),
                Arguments.of("/Users/gac/Documents/IdeaProjects/operativi/vaadin23/src/vaadin23", false),
                Arguments.of("Users/gac/Documents/IdeaProjects/operativi/vaadin23/src/vaadin23.iml", false),
                Arguments.of("/Users/gac/Documents/IdeaProjects/operativi/vaadin23/src/vaadin23.iml", true),
                Arguments.of("/Users/gac/Desktop/test/Pippo", false)
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

        //--reindirizzo l'istanza della superclasse
        service = fileService;
    }


    /**
     * Qui passa prima di ogni test <br>
     * Invocare PRIMA il metodo setUpEach() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    protected void setUpEach() {
        super.setUpEach();
    }


    @Test
    @Order(1)
    @DisplayName("1 - Esistenza di una directory")
    void isEsisteDirectory() {
        System.out.println("1 - Esistenza di una directory");
        //--path
        //--esiste
        DIRECTORY().forEach(this::isEsisteDirectoryBase);
    }


    //--path
    //--esiste
    void isEsisteDirectoryBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        previstoBooleano = (boolean) mat[1];

        ottenutoBooleano = service.isEsisteDirectory(sorgente);
        assertEquals(previstoBooleano, ottenutoBooleano);

        System.out.println(VUOTA);
        System.out.println(String.format("La directory %s è %b", sorgente, ottenutoBooleano));
    }

    @Test
    @Order(2)
    @DisplayName("2 - Errore nella ricerca di una directory")
    void isEsisteDirectoryStr() {
        System.out.println("2 - Errore nella ricerca di una directory");
        //--path
        //--esiste
        DIRECTORY().forEach(this::isEsisteDirectoryStrBase);
    }

    //--path
    //--esiste
    void isEsisteDirectoryStrBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        previstoBooleano = (boolean) mat[1];

        ottenuto = service.isEsisteDirectoryStr(sorgente);
        if (previstoBooleano) {
            assertEquals(VUOTA, ottenuto);
            System.out.println(VUOTA);
            System.out.println(String.format("%s%s%s", sorgente, FORWARD, "esiste"));
        }
        else {
            assertNotNull(ottenuto);
            System.out.println(VUOTA);
            System.out.println(String.format("%s%s%s", sorgente, FORWARD, ottenuto));
        }
    }


    @Test
    @Order(3)
    @DisplayName("3 - Esistenza di un file")
    void isEsisteFile() {
        System.out.println("3 - Esistenza di un file");
        //--path
        //--esiste
        FILE().forEach(this::isEsisteFileBase);
    }

    //--path
    //--esiste
    void isEsisteFileBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        previstoBooleano = (boolean) mat[1];

        ottenutoBooleano = service.isEsisteFile(sorgente);
        assertEquals(previstoBooleano, ottenutoBooleano);

        System.out.println(VUOTA);
        System.out.println(String.format("Il file %s è %b", sorgente, ottenutoBooleano));
    }

    @Test
    @Order(4)
    @DisplayName("4 - Errore nella ricerca di un file")
    void isEsisteFileStr() {
        System.out.println("2 - Errore nella ricerca di un file");
        //--path
        //--esiste
        FILE().forEach(this::isEsisteFileStrBase);
    }

    //--path
    //--esiste
    void isEsisteFileStrBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        previstoBooleano = (boolean) mat[1];

        ottenuto = service.isEsisteFileStr(sorgente);
        if (previstoBooleano) {
            assertEquals(VUOTA, ottenuto);
            System.out.println(VUOTA);
            System.out.println(String.format("%s%s%s", sorgente, FORWARD, "File trovato"));
        }
        else {
            assertNotNull(ottenuto);
            System.out.println(VUOTA);
            System.out.println(String.format("%s%s%s", sorgente, FORWARD, ottenuto));
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

}