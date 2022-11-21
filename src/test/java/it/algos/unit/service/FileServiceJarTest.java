package it.algos.unit.service;

import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.service.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: Sat, 19-Nov-2022
 * Time: 07:12
 * <p>
 * Unit test di una classe di servizio (di norma) <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("File service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileServiceJarTest extends AlgosTest {

    protected static final String PATH_TARGET = "/Users/gac/Documents/IdeaProjects/operativi/wiki23/target";

    protected static final String PATH_JAR = "/Users/gac/Documents/IdeaProjects/operativi/wiki23/target/wiki23-1.0.jar";

    protected static final String PATH_JAR_OLD = "/Users/gac/Desktop/IdeaProjects/vaad.jar";

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private FileService service;

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
     * Qui passa prima di ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUpEach() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    protected void setUpEach() {
        super.setUpEach();
    }


    @Test
    @Order(1)
    @DisplayName("1 - scanJar")
    void scanJar() {
        System.out.println("1 - tutti i contenuti del jar");
        System.out.println(VUOTA);

        sorgente = PATH_JAR;
        listaStr = service.scanJar(sorgente);
        assertNotNull(listaStr);
        ottenutoIntero = listaStr.size();
        assertTrue(ottenutoIntero > 0);

        System.out.println(String.format("Ci sono %d elementi nel jar %s", ottenutoIntero, sorgente));
        System.out.println(VUOTA);
        print(listaStr);
    }

    @Test
    @Order(2)
    @DisplayName("2 - scanJarClasses")
    void scanJarClasses() {
        System.out.println("2 - classi del jar");
        System.out.println(VUOTA);

        sorgente = PATH_JAR;
        listaStr = service.scanJarClasses(sorgente);
        assertNotNull(listaStr);
        ottenutoIntero = listaStr.size();
        assertTrue(ottenutoIntero > 0);

        System.out.println(String.format("Ci sono %d classi nel jar %s", ottenutoIntero, sorgente));
        System.out.println(VUOTA);
        print(listaStr);
    }

    @Test
    @Order(3)
    @DisplayName("3 - scanJarDir")
    void scanJarDir() {
        System.out.println("3 - classi della directory");
        System.out.println(VUOTA);

        sorgente = PATH_JAR;

        sorgente2 = "it/algos/wiki23/backend/statistiche";
        previstoIntero = 7;
        listaStr = service.scanJarDir(sorgente, sorgente2);
        assertNotNull(listaStr);
        ottenutoIntero = listaStr.size();
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d classi nella directory %s del jar %s", ottenutoIntero, sorgente2, sorgente));
        System.out.println(VUOTA);
        print(listaStr);

        sorgente2 = "it/algos/vaad23/backend/packages/crono/anno";
        previstoIntero = 4;
        listaStr = service.scanJarDir(sorgente, sorgente2);
        assertNotNull(listaStr);
        ottenutoIntero = listaStr.size();
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Ci sono %d classi nella directory %s del jar %s", ottenutoIntero, sorgente2, sorgente));
        System.out.println(VUOTA);
        print(listaStr);

        sorgente2 = "it/algos/wiki23/backend/packages/anno";
        previstoIntero = 5;
        listaStr = service.scanJarDir(sorgente, sorgente2);
        assertNotNull(listaStr);
        ottenutoIntero = listaStr.size();
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Ci sono %d classi nella directory %s del jar %s", ottenutoIntero, sorgente2, sorgente));
        System.out.println(VUOTA);
        print(listaStr);
    }


    @Test
    @Order(4)
    @DisplayName("4 - scanJarDirType")
    void scanJarDirType() {
        System.out.println("4 - classi di un determinato type della directory");
        System.out.println(VUOTA);

        sorgente = PATH_JAR;

        sorgente2 = "it/algos/wiki23/backend/statistiche";
        sorgente3 = "pippo";
        listaStr = service.scanJarDirType(sorgente, sorgente2, sorgente3);
        assertNotNull(listaStr);
        assertTrue(listaStr.size() == 0);
        System.out.println(String.format("Non ci sono classi del typo '%s' nella directory %s", sorgente3, sorgente2));
        System.out.println(VUOTA);
        print(listaStr);

        sorgente2 = "it/algos/vaad23/backend/packages/crono/anno";
        sorgente3 = "Backend";
        previstoIntero = 1;
        listaStr = service.scanJarDirType(sorgente, sorgente2, sorgente3);
        assertNotNull(listaStr);
        ottenutoIntero = listaStr.size();
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Ci sono %d classi nella directory %s del jar %s", ottenutoIntero, sorgente2, sorgente));
        System.out.println(VUOTA);
        print(listaStr);

        sorgente2 = "it/algos/wiki23/backend/packages/attivita";
        sorgente3 = "Backend";
        previstoIntero = 1;
        listaStr = service.scanJarDirType(sorgente, sorgente2, sorgente3);
        assertNotNull(listaStr);
        ottenutoIntero = listaStr.size();
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Ci sono %d classi nella directory %s del jar %s", ottenutoIntero, sorgente2, sorgente));
        System.out.println(VUOTA);
        print(listaStr);

        sorgente2 = "it/algos/wiki23/backend/packages";
        sorgente3 = "Backend";
        previstoIntero = 12;
        listaStr = service.scanJarDirType(sorgente, sorgente2, sorgente3);
        assertNotNull(listaStr);
        ottenutoIntero = listaStr.size();
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Ci sono %d classi nella directory %s del jar %s", ottenutoIntero, sorgente2, sorgente));
        System.out.println(VUOTA);
        print(listaStr);
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