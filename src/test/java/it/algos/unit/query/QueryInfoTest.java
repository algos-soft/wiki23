package it.algos.unit.query;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.wiki.query.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.boot.test.context.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;

import java.time.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Thu, 29-Sep-2022
 * Time: 09:39
 * Unit test di una classe service o backend o query <br>
 * Estende la classe astratta AlgosTest che contiene le regolazioni essenziali <br>
 * Nella superclasse AlgosTest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse AlgosTest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("query")
@DisplayName("Test QueryInfo")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QueryInfoTest extends WikiTest {


    /**
     * Classe principale di riferimento <br>
     */
    private QueryInfo istanza;


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
    @DisplayName("1 - Costruttore base senza parametri")
    void costruttoreBase() {
        istanza = new QueryInfo();
        assertNotNull(istanza);
        System.out.println(("1 - Costruttore base senza parametri"));
        System.out.println(VUOTA);
        System.out.println(String.format("Costruttore base senza parametri per un'istanza di %s", istanza.getClass().getSimpleName()));
    }

    @Test
    @Order(2)
    @DisplayName("2 - Request vuota")
    void manca() {
        System.out.println(("2 - Request vuota"));
        System.out.println(VUOTA);

        ottenutoRisultato = appContext.getBean(QueryInfo.class).urlRequest(sorgente);
        assertNotNull(ottenutoRisultato);
        assertFalse(ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato);
    }


    @Test
    @Order(3)
    @DisplayName("3 - Request errata")
    void errata() {
        System.out.println(("3 - Request errata"));
        System.out.println(VUOTA);

        sorgente = "Pagina inesistente";
        ottenutoRisultato = appContext.getBean(QueryInfo.class).urlRequest(sorgente);
        assertNotNull(ottenutoRisultato);
        assertFalse(ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato);
    }


    @Test
    @Order(4)
    @DisplayName("4 - Request valida")
    void valida() {
        System.out.println(("4 - Request valida"));
        System.out.println(VUOTA);

        sorgente = "Matteo Renzi";
        ottenutoRisultato = appContext.getBean(QueryInfo.class).urlRequest(sorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        assertTrue(textService.isValid(ottenutoRisultato.getNewtimestamp()));
        assertTrue(ottenutoRisultato.getPageid() > 0);
        assertTrue(ottenutoRisultato.getLongValue() > 0);
        System.out.println(String.format("Dimensione della pagina: %d", ottenutoRisultato.getLongValue()));
        printRisultato(ottenutoRisultato);
    }

    @Test
    @Order(5)
    @DisplayName("5 - Request getLength")
    void getLength() {
        System.out.println(("5 - Request getLength"));
        System.out.println(VUOTA);

        sorgente = "Matteo Renzi";
        ottenutoIntero = appContext.getBean(QueryInfo.class).getLength(sorgente);
        assertTrue(ottenutoIntero > 0);
        System.out.println(String.format("Dimensione della pagina: %d", ottenutoIntero));
    }


    @Test
    @Order(6)
    @DisplayName("6 - Request getTouched")
    void getTouched() {
        System.out.println(("6 - Request getTouched"));
        System.out.println(VUOTA);

        sorgente = "Matteo Renzi";
        ottenuto = appContext.getBean(QueryInfo.class).getTouched(sorgente);
        assertTrue(textService.isValid(ottenuto));
        System.out.println(String.format("Ultima modifica della pagina: %s", ottenuto));
    }


    @Test
    @Order(7)
    @DisplayName("7 - Request getLast")
    void getLast() {
        LocalDateTime last;
        System.out.println(("7 - Request getLast"));
        System.out.println(VUOTA);

        sorgente = "Matteo Renzi";
        last = appContext.getBean(QueryInfo.class).getLast(sorgente);
        assertNotNull(last);
        System.out.println(String.format("Ultima modifica della pagina: %s", dateService.get(last)));
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