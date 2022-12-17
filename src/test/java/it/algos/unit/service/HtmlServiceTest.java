package it.algos.unit.service;

import it.algos.base.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.service.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: ven, 07-mag-2021
 * Time: 18:57
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("quickly")
@DisplayName("HtmlService - Gestione dei testi html.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HtmlServiceTest extends AlgosTest {


    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private HtmlService service;


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
        service = htmlService;
    }

    @Test
    @Order(1)
    @DisplayName("1 - getNumTag")
    void getNumTag() {
        sorgente = "Forse domani ]] me ne vado {{ davvero [[ e forse { no { mercoledì";
        previstoIntero = 0;

        ottenutoIntero = service.getNumTag(VUOTA, sorgente2);
        assertEquals(ottenutoIntero, previstoIntero);

        ottenutoIntero = service.getNumTag(sorgente, sorgente2);
        assertEquals(ottenutoIntero, previstoIntero);

        sorgente2 = "]";
        previstoIntero = 2;
        ottenutoIntero = service.getNumTag(sorgente, sorgente2);
        assertEquals(ottenutoIntero, previstoIntero);

        sorgente2 = "]]";
        previstoIntero = 1;
        ottenutoIntero = service.getNumTag(sorgente, sorgente2);
        assertEquals(ottenutoIntero, previstoIntero);

        sorgente2 = GRAFFA_INI;
        previstoIntero = 4;
        ottenutoIntero = service.getNumTag(sorgente, sorgente2);
        assertEquals(ottenutoIntero, previstoIntero);

        sorgente2 = DOPPIE_GRAFFE_INI;
        previstoIntero = 1;
        ottenutoIntero = service.getNumTag(sorgente, sorgente2);
        assertEquals(ottenutoIntero, previstoIntero);
    }

    @Test
    @Order(2)
    @DisplayName("2 - isPariTag")
    void isPariTag() {
        sorgente = "Forse domani ]] me ne [ vado {{ davvero [[ e [forse } no } mercoledì}}";
        previstoBooleano = false;

        ottenutoBooleano = service.isPariTag(VUOTA, sorgente2, sorgente3);
        assertEquals(ottenutoBooleano, previstoBooleano);

        ottenutoBooleano = service.isPariTag(sorgente, sorgente2, sorgente3);
        assertEquals(ottenutoBooleano, previstoBooleano);

        ottenutoBooleano = service.isPariTag(sorgente, sorgente2, sorgente3);
        assertEquals(ottenutoBooleano, previstoBooleano);

        sorgente2 = DOPPIE_GRAFFE_END;
        sorgente3 = DOPPIE_GRAFFE_INI;
        previstoBooleano = true;
        ottenutoBooleano = service.isPariTag(sorgente, sorgente2, sorgente3);
        assertEquals(ottenutoBooleano, previstoBooleano);

        sorgente2 = GRAFFA_END;
        sorgente3 = GRAFFA_INI;
        previstoBooleano = false;
        ottenutoBooleano = service.isPariTag(sorgente, sorgente2, sorgente3);
        assertEquals(ottenutoBooleano, previstoBooleano);

        sorgente2 = DOPPIE_QUADRE_END;
        sorgente3 = DOPPIE_QUADRE_INI;
        previstoBooleano = true;
        ottenutoBooleano = service.isPariTag(sorgente, sorgente2, sorgente3);
        assertEquals(ottenutoBooleano, previstoBooleano);

        sorgente2 = QUADRA_END;
        sorgente3 = QUADRA_INI;
        previstoBooleano = false;
        ottenutoBooleano = service.isPariTag(sorgente, sorgente2, sorgente3);
        assertEquals(ottenutoBooleano, previstoBooleano);
    }

    @Test
    @Order(3)
    @DisplayName("3 - setNoHtmlTag")
    void setNoHtmlTag() {
        sorgente = "<code>IT-65</code>";

        sorgente2 = "ref";
        previsto = "<code>IT-65</code>";
        ottenuto = service.setNoHtmlTag(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        System.out.println(ottenuto);

        sorgente2 = "code";
        previsto = "IT-65";
        ottenuto = service.setNoHtmlTag(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        System.out.println(ottenuto);

        sorgente = "<ref>Altro testo con ref</ref>";
        sorgente2 = "ref";
        previsto = "Altro testo con ref";
        ottenuto = service.setNoHtmlTag(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        System.out.println(ottenuto);
    }

    @Test
    @Order(4)
    @DisplayName("4 - verde")
    void verde() {
        sorgente = "Testo colorato verde";
        previsto = "<span style=\"color:green\">Testo colorato verde</span>";
        ottenuto = service.verde(sorgente);
        assertEquals(previsto, ottenuto);
        System.out.println(ottenuto);
    }

    @Test
    @Order(5)
    @DisplayName("5 - rosso")
    void rosso() {
        sorgente = "Testo colorato rosso";
        previsto = "<span style=\"color:red\">Testo colorato rosso</span>";
        ottenuto = service.rosso(sorgente);
        assertEquals(previsto, ottenuto);
        System.out.println(ottenuto);
    }

    @Test
    @Order(6)
    @DisplayName("6 - blue")
    void blu() {
        sorgente = "Testo colorato blue";
        previsto = "<span style=\"color:blue\">Testo colorato blue</span>";
        ottenuto = service.blue(sorgente);
        assertEquals(previsto, ottenuto);
        System.out.println(ottenuto);
    }

    @Test
    @Order(7)
    @DisplayName("7 - span normale")
    void span() {
        sorgente = "Testo non colorato";

        previsto = "<span>Testo non colorato</span>";
        span = service.getSpan(sorgente);
        assertNotNull(span);
        assertEquals(previsto, span.getElement().toString());
        printSpan(span);
    }

    @Test
    @Order(8)
    @DisplayName("8 - span rosso")
    void spanRosso() {
        sorgente = "Testo colorato rosso";

        sorgente2 = "rosso";
        previsto = "<span style=\"color:red\">Testo colorato rosso</span>";
        span = service.getSpanRosso(sorgente);
        assertNotNull(span);
        assertEquals(previsto, span.getElement().toString());
        printSpan(span);
    }

    @Test
    @Order(9)
    @DisplayName("9 - span verde")
    void spanVerde() {
        sorgente = "Testo colorato verde";

        sorgente2 = "rosso";
        previsto = "<span style=\"color:green\">Testo colorato verde</span>";
        span = service.getSpanVerde(sorgente);
        assertNotNull(span);
        assertEquals(previsto, span.getElement().toString());
        printSpan(span);
    }

    @Test
    @Order(10)
    @DisplayName("10 - span blue")
    void colore() {
        sorgente = "Testo colorato blue";

        sorgente2 = "rosso";
        previsto = "<span style=\"color:blue\">Testo colorato blue</span>";
        span = service.getSpanBlu(sorgente);
        assertNotNull(span);
        assertEquals(previsto, span.getElement().toString());
        printSpan(span);
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