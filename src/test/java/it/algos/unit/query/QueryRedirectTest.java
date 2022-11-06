package it.algos.unit.query;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.wiki.query.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Mon, 31-Oct-2022
 * Time: 09:46
 * Unit test di una classe service o backend o query <br>
 * Estende la classe astratta WikiTest che contiene le regolazioni essenziali <br>
 * Nella superclasse WikiTest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse WikiTest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("query")
@DisplayName("Test QueryRedirect")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class QueryRedirectTest extends WikiTest {


    /**
     * Classe principale di riferimento <br>
     */
    private QueryRedirect istanza;


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
        istanza = new QueryRedirect();
        assertNotNull(istanza);
        System.out.println(("1- Costruttore base senza parametri"));
        System.out.println(VUOTA);
        System.out.println(String.format("Costruttore base senza parametri per un'istanza di %s", istanza.getClass().getSimpleName()));
    }


    @Test
    @Order(2)
    @DisplayName("2 - Request errata. Manca il wikiTitle")
    void manca() {
        System.out.println(("2 - Request errata. Manca il wikiTitle"));
        System.out.println(VUOTA);

        ottenutoRisultato = appContext.getBean(QueryRedirect.class).urlRequest(sorgente);
        assertNotNull(ottenutoRisultato);
        assertFalse(ottenutoRisultato.isValido());
        System.out.println(VUOTA);
        printRisultato(ottenutoRisultato);
    }

    @Test
    @Order(3)
    @DisplayName("3 - Request errata. Non esiste la pagina")
    void inesistente() {
        System.out.println(("3 - Request errata. Non esiste la pagina"));
        System.out.println(VUOTA);

        sorgente = "Pippoz Belloz";
        ottenutoRisultato = appContext.getBean(QueryRedirect.class).urlRequest(sorgente);
        assertNotNull(ottenutoRisultato);
        assertFalse(ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato);
    }

    @Test
    @Order(4)
    @DisplayName("4 - Request corretta. Non è un redirect")
    void paginaNormaleNonRedirect() {
        System.out.println(("4 - Request corretta. Non è un redirect"));
        System.out.println(VUOTA);

        sorgente = "Piozzano";
        ottenutoRisultato = appContext.getBean(QueryRedirect.class).urlRequest(sorgente);
        assertNotNull(ottenutoRisultato);
        assertFalse(ottenutoRisultato.isValido());
        assertEquals(AETypePage.pagina, ottenutoRisultato.getTypePage());
        printRisultato(ottenutoRisultato);
    }


    @Test
    @Order(5)
    @DisplayName("5 - Request corretta. È un redirect")
    void paginaRedirect() {
        System.out.println(("5 - Request corretta. È un redirect"));
        System.out.println(VUOTA);

        sorgente = "Edson Arantes do Nascimento";
        ottenutoRisultato = appContext.getBean(QueryRedirect.class).urlRequest(sorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        assertEquals(AETypePage.redirect, ottenutoRisultato.getTypePage());

        message = String.format("La pagina '%s' è un redirect a '%s'", sorgente, ottenutoRisultato.getTxtValue());
        System.out.println((message));
        System.out.println((VUOTA));

        printRisultato(ottenutoRisultato);
    }


    @Test
    @Order(6)
    @DisplayName("5 - Risultato booleano e txtValue del wikiLink")
    void linkValue() {
        System.out.println(("5 - Risultato booleano e txtValue del wikiLink"));
        System.out.println(VUOTA);

        sorgente = "Earvin Johnson";
        ottenutoBooleano = appContext.getBean(QueryRedirect.class).isRedirect(sorgente);
        assertTrue(ottenutoBooleano);

        previsto = "Magic Johnson";
        ottenuto = appContext.getBean(QueryRedirect.class).getWikiLink(sorgente);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        message = String.format("La pagina '%s' è un redirect a '%s'", sorgente, ottenuto);
        System.out.println((message));
        System.out.println((VUOTA));
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
