package it.algos.unit.service;

import it.algos.test.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.service.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.stream.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: dom, 13-mar-2022
 * Time: 08:12
 * <p>
 * Unit test di una classe di servizio (di norma) <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("quickly")
@DisplayName("Resource service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ResourceServiceTest extends ATest {


    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private ResourceService service;

    //--path parziale
    //--esiste
    protected static Stream<Arguments> FRONT_END() {
        return Stream.of(
                Arguments.of(null, false),
                Arguments.of(VUOTA, false),
                Arguments.of("styles.shared-styles.css", false),
                Arguments.of("styles/shared-styles.css", true)
        );
    }


    //--path parziale
    //--esiste
    protected static Stream<Arguments> META_INF() {
        return Stream.of(
                Arguments.of(null, false),
                Arguments.of(VUOTA, false),
                Arguments.of("rainbow.png", false),
                Arguments.of("img.rainbow.png", false),
                Arguments.of("img/rainbow.png", true),
                Arguments.of("src/main/resources/META-INF/resources/img.rainbow.png", false),
                Arguments.of("src/main/resources/META-INF/resources/img/rainbow.png", true),
                Arguments.of("bandiere.ca.png", false),
                Arguments.of("bandiere/ca.png", true),
                Arguments.of("src/main/resources/META-INF/resources/bandiere/ca", false),
                Arguments.of("src/main/resources/META-INF/resources/bandiere.ca.png", false),
                Arguments.of("src/main/resources/META-INF/resources/bandiere/ca.png", true)
        );
    }

    //--path parziale
    //--esiste in locale
    //--esiste sul server
    protected static Stream<Arguments> CONFIG() {
        return Stream.of(
                Arguments.of(null, false, false),
                Arguments.of(VUOTA, false, false),
                Arguments.of("config.password.txt", false, false),
                Arguments.of("/config.password.txt", false, false),
                Arguments.of("/config/password.txt", false, false),
                Arguments.of("/config.password.txt", false, false),
                Arguments.of("/config.password.txt", false, false),
                Arguments.of("config/password.txt", false, false),
                Arguments.of("password.txt", false, false),
                Arguments.of("at.png", true, false),
                Arguments.of("africa", true, false),
                Arguments.of("regioni", true, false),
                Arguments.of("continenti", true, true),
                Arguments.of("mesi", true, true),
                Arguments.of("secoli", true, true)
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
        service = resourceService;
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


    @ParameterizedTest
    @MethodSource(value = "FRONT_END")
    @Order(1)
    @DisplayName("1 - Legge nella directory 'frontend'")
        //--path parziale
        //--esiste in locale
    void leggeFrontend(String sorgente, boolean esiste) {
        System.out.println("1 - Legge nella directory 'frontend'");
        System.out.println(VUOTA);

        ottenuto = service.leggeFrontend(sorgente);
        if (esiste) {
            assertTrue(textService.isValid(ottenuto));
            System.out.println(String.format("Il file %s%s%s", sorgente, FORWARD, "esiste nella cartella frontend"));
        }
        else {
            assertTrue(textService.isEmpty(ottenuto));
            System.out.println(String.format("%s%s%s", sorgente, FORWARD, "non esiste nella cartella frontend"));
        }
    }


    @ParameterizedTest
    @Order(2)
    @MethodSource(value = "META_INF")
    @DisplayName("2 - Legge nella directory META-INF")
        //--path parziale
        //--esiste in locale
    void leggeMetaInf(String sorgente, boolean esiste) {
        System.out.println("2 - Legge nella directory META-INF");
        System.out.println(VUOTA);

        ottenuto = service.leggeMetaInf(sorgente);
        if (esiste) {
            assertTrue(textService.isValid(ottenuto));
            System.out.println(String.format("%s%s%s", sorgente, FORWARD, "esiste nella cartella META-INF"));
        }
        else {
            assertTrue(textService.isEmpty(ottenuto));
            System.out.println(String.format("%s%s%s", sorgente, FORWARD, "non esiste nella cartella META-INF"));
        }
    }


    @ParameterizedTest
    @Order(3)
    @MethodSource(value = "META_INF")
    @DisplayName("3 - Legge i bytes[]")
        //--path parziale
        //--esiste in locale
    void getBytes(String sorgente, boolean esiste) {
        System.out.println("3 - Legge i bytes[]");
        System.out.println(VUOTA);

        bytes = service.getBytes(sorgente);
        if (esiste) {
            assertNotNull(bytes);
            System.out.println(String.format("Il file di risorse %s nella cartella META_INF esiste e non è vuoto", sorgente));
        }
        else {
            assertNull(bytes);
            System.out.println(String.format("Nella cartella META_INF non esiste il file di risorse %s", sorgente));
        }
    }


    @ParameterizedTest
    @Order(4)
    @MethodSource(value = "META_INF")
    @DisplayName("4 - Legge le risorse")
        //--path parziale
        //--esiste in locale
    void getSrc(String sorgente, boolean esiste) {
        System.out.println("4 - Legge le risorse");
        System.out.println(VUOTA);

        ottenuto = service.getSrc(sorgente);
        if (esiste) {
            assertTrue(textService.isValid(ottenuto));
            System.out.println(String.format("Il file di risorse %s nella cartella META_INF esiste e non è vuoto", sorgente));
        }
        else {
            assertTrue(textService.isEmpty(ottenuto));
            System.out.println(String.format("Nella cartella META_INF non esiste il file di risorse %s", sorgente));
        }
    }


    @ParameterizedTest
    @Order(5)
    @MethodSource(value = "CONFIG")
    @DisplayName("5 - Legge un file nella directory 'config'")
        //--path parziale
        //--esiste in locale
    void leggeFileConfig(String sorgente, boolean esiste) {
        System.out.println("5 - Legge un file nella directory 'config'");
        System.out.println(VUOTA);

        ottenuto = service.leggeConfig(sorgente);
        if (esiste) {
            assertTrue(textService.isValid(ottenuto));
            System.out.println(String.format("%s%s%s", sorgente, FORWARD, "esiste nella cartella config"));
        }
        else {
            assertTrue(textService.isEmpty(ottenuto));
            System.out.println(String.format("%s%s%s", sorgente, FORWARD, "non esiste nella cartella config"));
        }
    }


    @ParameterizedTest
    @Order(6)
    @MethodSource(value = "CONFIG")
    @DisplayName("6 - Legge una lista dalla directory 'config'")
        //--path parziale
        //--esiste in locale
    void leggeListaConfig(String sorgente, boolean esiste) {
        System.out.println(String.format("6 - Legge dalla directory 'config' una lista per il file CSV '%s'", sorgente));
        System.out.println(VUOTA);

        listaStr = service.leggeListaConfig(sorgente);
        if (esiste) {
            assertTrue(listaStr != null && listaStr.size() > 0);
            System.out.println(String.format("%s%s%s", sorgente, FORWARD, "esiste nella cartella config"));
            listaStr = service.leggeListaConfig(sorgente, true);
            printVuota(listaStr, "compresi i titoli");

            System.out.println(VUOTA);
            listaStr = service.leggeListaConfig(sorgente, false);
            assertNotNull(listaStr);
            printVuota(listaStr, "esclusi i titoli");
        }
        else {
            assertTrue(listaStr == null);
            System.out.println(String.format("%s%s%s", sorgente, FORWARD, "non esiste nella cartella config"));
        }
    }


    @ParameterizedTest
    @Order(7)
    @MethodSource(value = "CONFIG")
    @DisplayName("7 - Legge una mappa dalla directory 'config'")
        //--path parziale
        //--esiste in locale
    void leggeMappaConfig(String sorgente, boolean esiste) {
        System.out.println(String.format("7 - Legge dalla directory 'config' una mappa per il file CSV '%s'", sorgente));
        System.out.println(VUOTA);

        mappa = service.leggeMappaConfig(sorgente);
        if (esiste) {
            System.out.println(String.format("Il file %s%s%s", sorgente, FORWARD, "esiste sul server Algos"));

            System.out.println(VUOTA);
            mappa = service.leggeMappaConfig(sorgente, true);
            assertNotNull(mappa);
            printMappa(mappa, "compresi i titoli");

            System.out.println(VUOTA);
            mappa = service.leggeMappaConfig(sorgente, false);
            assertNotNull(mappa);
            printMappa(mappa, "esclusi i titoli");
        }
        else {
            assertTrue(mappa == null);
            System.out.println(String.format("%s%s%s", sorgente, FORWARD, "non esiste nella cartella config"));
        }
    }

    @ParameterizedTest
    @Order(8)
    @MethodSource(value = "CONFIG")
    @DisplayName("8 - Legge un file dal server 'algos'")
        //--path parziale
        //--esiste in locale
        //--esiste sul server
    void leggeFileServer(String sorgente, boolean nonUsato, boolean esisteSulServer) {
        System.out.println(String.format("8 - Legge dal server 'algos' un file CSV '%s'", sorgente));
        System.out.println(VUOTA);

        ottenuto = service.leggeServer(sorgente);
        if (esisteSulServer) {
            assertTrue(textService.isValid(ottenuto));
            System.out.println(String.format("Il file %s%s%s", sorgente, FORWARD, "esiste sul server Algos"));
        }
        else {
            assertTrue(textService.isEmpty(ottenuto));
            System.out.println(String.format("Il file %s%s%s", sorgente, FORWARD, "NON esiste sul server Algos"));
        }
    }

    @ParameterizedTest
    @Order(9)
    @MethodSource(value = "CONFIG")
    @DisplayName("9 - Legge una lista dal server 'algos'")
        //--path parziale
        //--esiste in locale
        //--esiste sul server
    void leggeListaServer(String sorgente, boolean nonUsato, boolean esisteSulServer) {
        System.out.println(String.format("9 - Legge dal server 'algos' una lista '%s'", sorgente));
        System.out.println(VUOTA);

        listaStr = service.leggeListaServer(sorgente);
        if (esisteSulServer) {
            System.out.println(String.format("Il file %s%s%s", sorgente, FORWARD, "esiste sul server Algos"));

            System.out.println(VUOTA);
            listaStr = service.leggeListaServer(sorgente, true);
            assertNotNull(listaStr);
            printVuota(listaStr, "nella lista compresi i titoli");

            System.out.println(VUOTA);
            listaStr = service.leggeListaServer(sorgente, false);
            assertNotNull(listaStr);
            printVuota(listaStr, "nella lista esclusi i titoli");
        }
        else {
            assertNull(listaStr);
            System.out.println(String.format("Il file %s%s%s", sorgente, FORWARD, "NON esiste sul server Algos"));
        }
    }

    @ParameterizedTest
    @Order(10)
    @MethodSource(value = "CONFIG")
    @DisplayName("10 - Legge una mappa dal server 'algos'")
        //--path parziale
        //--esiste in locale
        //--esiste sul server
    void leggeMappaServer(String sorgente, boolean nonUsato, boolean esisteSulServer) {
        System.out.println(String.format("10 - Legge dalla server 'algos' una mappa per il file CSV '%s'", sorgente));
        System.out.println(VUOTA);

        mappa = service.leggeMappaServer(sorgente);
        if (esisteSulServer) {
            System.out.println(String.format("Il file %s%s%s", sorgente, FORWARD, "esiste sul server Algos"));

            System.out.println(VUOTA);
            mappa = service.leggeMappaServer(sorgente, true);
            assertNotNull(mappa);
            printMappa(mappa, "nella mappa compresi i titoli");

            System.out.println(VUOTA);
            mappa = service.leggeMappaServer(sorgente, false);
            assertNotNull(mappa);
            printMappa(mappa, "nella mappa esclusi i titoli");
        }
        else {
            assertNull(mappa);
            System.out.println(String.format("Il file %s%s%s", sorgente, FORWARD, "NON esiste sul server Algos"));
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