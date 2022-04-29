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
    //--esiste
    protected static Stream<Arguments> CONFIG() {
        return Stream.of(
                Arguments.of(null, false),
                Arguments.of(VUOTA, false),
                Arguments.of("config.password.txt", false),
                Arguments.of("/config.password.txt", false),
                Arguments.of("/config/password.txt", false),
                Arguments.of("/config.password.txt", false),
                Arguments.of("/config.password.txt", false),
                Arguments.of("config/password.txt", false),
                Arguments.of("password.txt", false),
                Arguments.of("at.png", true),
                Arguments.of("africa", true),
                Arguments.of("regioni", true),
                Arguments.of("continenti", true)
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


    @Test
    @Order(1)
    @DisplayName("1 - Legge nella directory 'frontend'")
    void leggeFrontend() {
        System.out.println("1 - Legge nella directory 'frontend'");
        //--path parziale
        //--esiste
        FRONT_END().forEach(this::leggeFrontendBase);
    }

    //--path parziale
    //--esiste
    void leggeFrontendBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        previstoBooleano = (boolean) mat[1];
        System.out.println(VUOTA);

        ottenuto = service.leggeFrontend(sorgente);
        if (previstoBooleano) {
            assertNotNull(ottenuto);
            System.out.println(String.format("%s%s%s", sorgente, FORWARD, "esiste nella cartella frontend"));
        }
        else {
            assertEquals(VUOTA, ottenuto);
            System.out.println(String.format("%s%s%s", sorgente, FORWARD, "non esiste nella cartella frontend"));
        }
    }

    @Test
    @Order(2)
    @DisplayName("2 - Legge nella directory META-INF")
    void leggeMetaInf() {
        System.out.println("2 - Legge nella directory META-INF");
        //--path parziale
        //--esiste
        META_INF().forEach(this::leggeMetaInfBase);
    }

    //--path parziale
    //--esiste
    void leggeMetaInfBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        previstoBooleano = (boolean) mat[1];
        System.out.println(VUOTA);

        ottenuto = service.leggeMetaInf(sorgente);
        if (previstoBooleano) {
            assertTrue(textService.isValid(ottenuto));
            System.out.println(String.format("%s%s%s", sorgente, FORWARD, "esiste nella cartella META-INF"));
        }
        else {
            assertTrue(textService.isEmpty(ottenuto));
            System.out.println(String.format("%s%s%s", sorgente, FORWARD, "non esiste nella cartella META-INF"));
        }
    }


    @Test
    @Order(3)
    @DisplayName("3 - Legge i bytes[]")
    void getBytes() {
        System.out.println("3 - Legge i bytes[]");
        //--path parziale
        //--esiste
        META_INF().forEach(this::getBytesBase);
    }

    //--path parziale
    //--esiste
    void getBytesBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        previstoBooleano = (boolean) mat[1];
        System.out.println(VUOTA);

        bytes = service.getBytes(sorgente);
        if (previstoBooleano) {
            assertNotNull(bytes);
            System.out.println(String.format("Il file di risorse %s nella cartella META_INF esiste e non è vuoto", sorgente));
        }
        else {
            assertNull(bytes);
            System.out.println(String.format("Nella cartella META_INF non esiste il file di risorse %s", sorgente));
        }
    }


    @Test
    @Order(4)
    @DisplayName("4 - Legge le risorse")
    void getSrc() {
        System.out.println("4 - Legge le risorse");
        //--path parziale
        //--esiste
        META_INF().forEach(this::getSrcBase);
    }

    //--path parziale
    //--esiste
    void getSrcBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        previstoBooleano = (boolean) mat[1];
        System.out.println(VUOTA);

        ottenuto = service.getSrc(sorgente);
        if (previstoBooleano) {
            assertTrue(textService.isValid(ottenuto));
            System.out.println(String.format("Il file di risorse %s nella cartella META_INF esiste e non è vuoto", sorgente));
        }
        else {
            assertTrue(textService.isEmpty(ottenuto));
            System.out.println(String.format("Nella cartella META_INF non esiste il file di risorse %s", sorgente));
        }
    }


    @Test
    @Order(5)
    @DisplayName("5 - Legge un file nella directory 'config'")
    void leggeConfig() {
        System.out.println("5 - Legge un file nella directory 'config'");
        //--path parziale
        //--esiste
        CONFIG().forEach(this::leggeConfigBase);
    }

    //--path parziale
    //--esiste
    void leggeConfigBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        previstoBooleano = (boolean) mat[1];
        System.out.println(VUOTA);

        ottenuto = service.leggeConfig(sorgente);
        if (previstoBooleano) {
            assertTrue(textService.isValid(ottenuto));
            System.out.println(String.format("%s%s%s", sorgente, FORWARD, "esiste nella cartella config"));
        }
        else {
            assertTrue(textService.isEmpty(ottenuto));
            System.out.println(String.format("%s%s%s", sorgente, FORWARD, "non esiste nella cartella config"));
        }
    }


    @Test
    @Order(6)
    @DisplayName("6 - Legge una lista dalla directory 'config'")
    void leggeListaConfig() {
        sorgente = "continenti";
        System.out.println(String.format("6 - Legge dalla directory 'config' una lista per il file CSV '%s'", sorgente));

        ottenuto = service.leggeConfig(sorgente);
        assertTrue(textService.isValid(ottenuto));

        listaStr = service.leggeListaConfig(sorgente, true);
        assertNotNull(listaStr);
        printVuota(listaStr, "compresi i titoli");

        listaStr = service.leggeListaConfig(sorgente, false);
        assertNotNull(listaStr);
        System.out.println(VUOTA);
        printVuota(listaStr, "esclusi i titoli");

        listaStr = service.leggeListaConfig(sorgente);
        assertNotNull(listaStr);
        System.out.println(VUOTA);
        printVuota(listaStr, "coi titoli di default");
    }


    @Test
    @Order(7)
    @DisplayName("7 - Legge una mappa dalla directory 'config'")
    void leggeMappaConfig() {
        sorgente = "continenti";
        System.out.println(String.format("7 - Legge dalla directory 'config' una mappa per il file CSV '%s'", sorgente));
        ottenuto = service.leggeConfig(sorgente);
        assertTrue(textService.isValid(ottenuto));

        mappa = service.leggeMappaConfig(sorgente, true);
        assertNotNull(mappa);
        printMappa(mappa, "compresi i titoli");

        mappa = service.leggeMappaConfig(sorgente, false);
        assertNotNull(mappa);
        System.out.println(VUOTA);
        printMappa(mappa, "esclusi i titoli");

        mappa = service.leggeMappaConfig(sorgente);
        assertNotNull(mappa);
        System.out.println(VUOTA);
        printMappa(mappa, "coi titoli di default");
    }


    @Test
    @Order(8)
    @DisplayName("8 - Legge un file dal server 'algos'")
    void leggeServer() {
        sorgente = "continenti";
        System.out.println(String.format("8 - Legge dal server 'algos' un file CSV '%s'", sorgente));

        ottenuto = service.leggeServer(sorgente);
        assertTrue(textService.isValid(ottenuto));
        System.out.println(ottenuto.substring(0, 200));
    }


    @Test
    @Order(9)
    @DisplayName("9 - Legge una lista dal server 'algos'")
    void leggeListaServer() {
        sorgente = "continenti";
        System.out.println(String.format("9 - Legge dal server 'algos' una lista '%s'", sorgente));

        listaStr = service.leggeListaServer(sorgente, true);
        assertNotNull(listaStr);
        printVuota(listaStr, "letta compresi i titoli");

        listaStr = service.leggeListaServer(sorgente, false);
        assertNotNull(listaStr);
        printVuota(listaStr, "letta esclusi i titoli");

        listaStr = service.leggeListaServer(sorgente);
        assertNotNull(listaStr);
        printVuota(listaStr, "letta coi titoli di default");
    }


    @Test
    @Order(10)
    @DisplayName("10 - Legge una mappa dal server 'algos'")
    void leggeMappaServer() {
        sorgente = "continenti";
        System.out.println(String.format("10 - Legge dalla server 'algos' una mappa per il file CSV '%s'", sorgente));

        mappa = service.leggeMappaServer(sorgente, true);
        assertNotNull(mappa);
        printMappa(mappa, "compresi i titoli");

        mappa = service.leggeMappaServer(sorgente, false);
        assertNotNull(mappa);
        System.out.println(VUOTA);
        printMappa(mappa, "esclusi i titoli");

        mappa = service.leggeMappaServer(sorgente);
        assertNotNull(mappa);
        System.out.println(VUOTA);
        printMappa(mappa, "coi titoli di default");
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