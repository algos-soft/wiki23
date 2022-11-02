package it.algos.unit.query;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.didascalia.enumeration.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.wiki.query.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.boot.test.context.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: mar, 17-mag-2022
 * Time: 17:03
 * Unit test di una classe service o backend o query <br>
 * Estende la classe astratta AlgosTest che contiene le regolazioni essenziali <br>
 * Nella superclasse AlgosTest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse AlgosTest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("query")
@DisplayName("Test QueryAssert")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QueryAssertTest extends WikiTest {


    /**
     * Classe principale di riferimento <br>
     */
    private QueryAssert istanza;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();
        assertNull(istanza);
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
        istanza = new QueryAssert();
        assertNotNull(istanza);
        System.out.println(("1- Costruttore base senza parametri"));
        System.out.println(VUOTA);
        System.out.println(String.format("Costruttore base senza parametri per un'istanza di %s", istanza.getClass().getSimpleName()));
    }

    @Test
    @Order(2)
    @DisplayName("2 - Non collegato")
    void collegatoUser() {
        appContext.getBean(QueryLogin.class).urlRequest(AETypeUser.user);
        assertNotNull(botLogin);
        assertTrue(botLogin.isValido());
        assertEquals(botLogin.getUserType(), AETypeUser.user);

        ottenutoBooleano = appContext.getBean(QueryAssert.class).isEsiste();
        assertFalse(ottenutoBooleano);
    }

    @Test
    @Order(3)
    @DisplayName("3 - Collegato come bot")
    void collegatoBot() {
        appContext.getBean(QueryLogin.class).urlRequest(AETypeUser.bot);
        assertNotNull(botLogin);
        assertTrue(botLogin.isValido());
        assertEquals(botLogin.getUserType(), AETypeUser.bot);

        ottenutoBooleano = appContext.getBean(QueryAssert.class).isEsiste();
        assertTrue(ottenutoBooleano);
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