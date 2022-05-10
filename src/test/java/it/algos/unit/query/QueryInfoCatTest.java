package it.algos.unit.query;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.wiki.query.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: ven, 29-apr-2022
 * Time: 21:20
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("query")
@DisplayName("Test QueryCat")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QueryInfoCatTest extends WikiTest {


    /**
     * Classe principale di riferimento <br>
     */
    private QueryInfoCat istanza;

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();
    }


    /**
     * Qui passa prima di ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    protected void setUpEach() {
        super.setUpEach();
        istanza = null;
    }


    @Test
    @Order(1)
    @DisplayName("1- Costruttore base senza parametri")
    void costruttoreBase() {
        istanza = appContext.getBean(QueryInfoCat.class);
        assertNotNull(istanza);

        System.out.println(VUOTA);
        System.out.println(String.format("Costruttore base senza parametri per un'istanza di %s", istanza.getClass().getSimpleName()));
    }

    @Test
    @Order(2)
    @DisplayName("2- Test per una categoria inesistente")
    void nonEsiste() {
        System.out.println(("2- Test per una pagina inesistente"));
        istanza = appContext.getBean(QueryInfoCat.class);
        assertNotNull(istanza);

        sorgente = CATEGORIA_INESISTENTE;
        ottenutoRisultato = istanza.urlRequest(sorgente);
        assertNotNull(ottenutoRisultato);
        assertFalse(ottenutoRisultato.isValido());

        System.out.println(VUOTA);
        System.out.println(String.format("La categoria [[%s]] non esiste su wikipedia", sorgente));
        printRisultato(ottenutoRisultato);
    }


    @Test
    @Order(3)
    @DisplayName("3- Test per una categoria esistente")
    void urlRequest() {
        System.out.println(("3- Test per una categoria esistente"));

        sorgente = CATEGORIA_ESISTENTE_UNO;
        ottenutoRisultato = appContext.getBean(QueryInfoCat.class).urlRequest(sorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());

        System.out.println(VUOTA);
        System.out.println(String.format("Trovata la categoria [[%s]] su wikipedia", sorgente));
        printRisultato(ottenutoRisultato);
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