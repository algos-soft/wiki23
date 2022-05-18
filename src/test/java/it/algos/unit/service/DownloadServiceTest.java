package it.algos.unit.service;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.service.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: mer, 18-mag-2022
 * Time: 19:52
 * <p>
 * Unit test di una classe di servizio (di norma) <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("service")
@DisplayName("Download service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DownloadServiceTest extends WikiTest {


    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private DownloadService service;

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
        service = downloadService;
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
    @DisplayName("1 - Ciclo (parziale)")
    void ciclo() {
        System.out.println(("1 - Ciclo (parziale)"));
        System.out.println((VUOTA));
        service.checkCategoria(CATEGORIA_ESISTENTE_DUE);
        service.checkBot();
    }

    @Test
    @Order(2)
    @DisplayName("2 - Ciclo (parziale) con categoria specifica")
    void ciclo2() {
        System.out.println(("2 - Ciclo (parziale) con categoria specifica"));
        sorgente = CATEGORIA_ESISTENTE_UNO;

        //--collegato come anonymous
        System.out.println((VUOTA));
        service.checkCategoria(sorgente);
        service.checkBot();

        //--si collega come user
        System.out.println((VUOTA));
        queryService.logAsUser();
        service.checkCategoria(sorgente);
        service.checkBot();

        //--si collega come admin
        System.out.println((VUOTA));
        queryService.logAsAdmin();
        service.checkCategoria(sorgente);
        service.checkBot();

        //--si collega come bot
        System.out.println((VUOTA));
        queryService.logAsBot();
        service.checkCategoria(sorgente);
        service.checkBot();
    }
    @Test
    @Order(3)
    @DisplayName("3 - Ciclo (parziale) con categoria specifica")
    void ciclo3() {
        System.out.println(("2 - Ciclo (parziale) con categoria specifica"));
        sorgente = CATEGORIA_ESISTENTE_UNO;

        //--collegato come admin
        System.out.println((VUOTA));
        queryService.logAsBot();
        service.checkCategoria(sorgente);
        service.checkBot();
        listaPageIds = queryService.getListaPageIds(sorgente);
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