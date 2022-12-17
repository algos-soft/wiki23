package it.algos.unit.wrapper;

import it.algos.base.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.enumeration.*;
import it.algos.vaad24.backend.wrapper.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: lun, 21-mar-2022
 * Time: 16:19
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("quickly")
@Tag("wrapper")
@DisplayName("WrapLog")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WrapLogTest extends AlgosTest {


    /**
     * Classe principale di riferimento <br>
     */
    private WrapLog istanza;


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
        istanza = new WrapLog();
        assertNotNull(istanza);
        System.out.println(("1- Costruttore base senza parametri"));
        System.out.println(VUOTA);
        System.out.println(String.format("Costruttore base senza parametri per un'istanza di %s", istanza.getClass().getSimpleName()));
    }

    @Test
    @Order(2)
    @DisplayName("2- Costruttore statico")
    void costruttoreStatico() {
        istanza = WrapLog.build();
        assertNotNull(istanza);
        System.out.println(("2- Costruttore statico"));
        System.out.println(VUOTA);
        System.out.println(String.format("Costruttore base senza parametri per un'istanza di %s", istanza.getClass().getSimpleName()));
    }

    @Test
    @Order(3)
    @DisplayName("3- Check methods senza API")
    void normalMethods() {
        istanza = WrapLog.build();
        assertNotNull(istanza);
        System.out.println(("3- Check methods senza API"));
        System.out.println(VUOTA);
        System.out.println(String.format("Add type e message a un'istanza di %s SENZA API", istanza.getClass().getSimpleName()));

        istanza.type(AETypeLog.checkData);
        istanza.message("messaggio di testo");
    }

    @Test
    @Order(4)
    @DisplayName("4- Fluent API")
    void fluentApi() {
        istanza = WrapLog.build();
        assertNotNull(istanza);
        System.out.println(("4- Fluent API"));

        System.out.println(VUOTA);
        System.out.println(String.format("Add type e message a un'istanza di %s usando le API", istanza.getClass().getSimpleName()));
        WrapLog.build().type(AETypeLog.checkData).message("messaggio di testo");

        System.out.println(VUOTA);
        System.out.println(String.format("Add message e type (ordine diverso) a un'istanza di %s usando le API", istanza.getClass().getSimpleName()));
        WrapLog.build().message("messaggio di testo").type(AETypeLog.checkData);
    }

    @Test
    @Order(5)
    @DisplayName("5- Fluent API get message back")
    void fluentApiGet() {
        istanza = WrapLog.build();
        assertNotNull(istanza);
        System.out.println(("5- Fluent API get message back"));

        System.out.println(VUOTA);
        System.out.println("Restituisce un messaggio");
        ottenuto = WrapLog.build().type(AETypeLog.checkData).message("messaggio di testo").getMessage();
        System.out.println(ottenuto);
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