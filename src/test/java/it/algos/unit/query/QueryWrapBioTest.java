package it.algos.unit.query;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.wiki.query.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.boot.test.context.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: dom, 15-mag-2022
 * Time: 08:49
 * Unit test di una classe service o backend o query <br>
 * Estende la classe astratta AlgosTest che contiene le regolazioni essenziali <br>
 * Nella superclasse AlgosTest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse AlgosTest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("query")
@DisplayName("Test QueryWrapBio")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QueryWrapBioTest extends WikiTest {


    /**
     * Classe principale di riferimento <br>
     */
    private QueryWrapBio istanza;


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
        istanza = new QueryWrapBio();
        assertNotNull(istanza);
        System.out.println(("1- Costruttore base senza parametri"));
        System.out.println(VUOTA);
        System.out.println(String.format("Costruttore base senza parametri per un'istanza di %s", istanza.getClass().getSimpleName()));
    }

    @Test
    @Order(2)
    @DisplayName("2- Test per una pagina inesistente")
    void nonEsiste() {
        System.out.println(("2- Test per una pagina inesistente"));
        assertTrue(istanza == null);
        istanza = appContext.getBean(QueryWrapBio.class);
        assertNotNull(istanza);

        sorgente = PAGINA_INESISTENTE;
        ottenutoRisultato = istanza.urlRequest(sorgente);
        assertNotNull(ottenutoRisultato);
        assertFalse(ottenutoRisultato.isValido());

        listWrapBio = istanza.getWrap(sorgente);
        assertNull(listWrapBio);

        System.out.println(VUOTA);
        System.out.println(String.format("La pagina [[%s]] non esiste su wikipedia", sorgente));
        printRisultato(ottenutoRisultato);
    }

//    @Test
    @Order(3)
    @DisplayName("3- Test per una singola biografia esistente (urlRequest)")
    void urlRequest() {
        System.out.println(("3- Test per una singola biografia esistente (urlRequest)"));

        sorgente = PAGINA_ESISTENTE_UNO;
        listWrapBio = appContext.getBean(QueryWrapBio.class).getWrap(sorgente);
        assertNotNull(listWrapBio);
        assertTrue(listWrapBio.size() > 0);

        wrapBio = listWrapBio.get(0);
        System.out.println(VUOTA);
        System.out.println(String.format("Trovata la biografia [[%s]] su wikipedia", sorgente));
        printWrapBio(wrapBio);
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