package it.algos.unit.wrapper;

import com.vaadin.flow.component.*;
import com.vaadin.flow.server.*;
import it.algos.test.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.wrapper.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: ven, 25-mar-2022
 * Time: 21:22
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("quickly")
@DisplayName("WrapSpan")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WrapSpanTest extends ATest {

    private UI ui = new UI();

    /**
     * Classe principale di riferimento <br>
     */
    private WrapSpan istanza;

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();
        assertNull(istanza);

        UI.setCurrent(ui);

        VaadinSession session = Mockito.mock(VaadinSession.class);
        Mockito.when(session.hasLock()).thenReturn(true);
        ui.getInternals().setSession(session);
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
        istanza = new WrapSpan();
        assertNotNull(istanza);
        System.out.println(("1- Costruttore base senza parametri"));
        System.out.println(VUOTA);
        System.out.println(String.format("Costruttore base senza parametri per un'istanza di %s", istanza.getClass().getSimpleName()));
    }

    @Test
    @Order(2)
    @DisplayName("2- Fluent API")
    void dialogo() {
        System.out.println(("2- Fluent API"));
        System.out.println(VUOTA);
        sorgente = "Messaggio di testo";

        istanza = new WrapSpan().message(sorgente);
        assertNotNull(istanza);
        span = htmlService.getSpan(istanza);
        printSpan(span);

        istanza = new WrapSpan().message(sorgente).color(AETypeColor.verde);
        assertNotNull(istanza);
        span = htmlService.getSpan(istanza);
        printSpan(span);

        istanza = new WrapSpan().message(sorgente).color(AETypeColor.verde).weight(AEFontWeight.bold);
        assertNotNull(istanza);
        span = htmlService.getSpan(istanza);
        printSpan(span);

        istanza = new WrapSpan().message(sorgente).color(AETypeColor.verde).weight(AEFontWeight.bold).fontHeight(AEFontHeight.number16);
        assertNotNull(istanza);
        span = htmlService.getSpan(istanza);
        printSpan(span);

        span = htmlService.getSpan(istanza);
        printSpan(span);
    }

    /**
     * Qui passa al termine di ogni singolo test <br>
     */
    @AfterEach
    void tearDown() {
        UI.setCurrent(null);
    }


    /**
     * Qui passa una volta sola, chiamato alla fine di tutti i tests <br>
     */
    @AfterAll
    void tearDownAll() {
    }

}
