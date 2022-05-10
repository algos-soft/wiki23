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
@DisplayName("Test QueryBio")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QueryBioTest extends WikiTest {


    /**
     * Classe principale di riferimento <br>
     */
    private QueryBio istanza;

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
        istanza = appContext.getBean(QueryBio.class);
        assertNotNull(istanza);

        System.out.println(VUOTA);
        System.out.println(String.format("Costruttore base senza parametri per un'istanza di %s", istanza.getClass().getSimpleName()));
    }

    @Test
    @Order(2)
    @DisplayName("2- Test per una pagina inesistente")
    void nonEsiste() {
        System.out.println(("2- Test per una pagina inesistente"));
        istanza = appContext.getBean(QueryBio.class);
        assertNotNull(istanza);

        sorgente = PAGINA_INESISTENTE;
        ottenutoRisultato = istanza.urlRequest(sorgente);
        assertNotNull(ottenutoRisultato);
        assertFalse(ottenutoRisultato.isValido());
        wrapBio = istanza.getWrap(sorgente);
        assertNotNull(wrapBio);
        assertFalse(wrapBio.isValida());

        System.out.println(VUOTA);
        System.out.println(String.format("La pagina [[%s]] non esiste su wikipedia", sorgente));
        printRisultato(ottenutoRisultato);
    }


    @Test
    @Order(3)
    @DisplayName("3- Test per una biografia esistente (urlRequest)")
    void urlRequest() {
        System.out.println(("3- Test per una biografia esistente (urlRequest)"));

        sorgente = PAGINA_ESISTENTE_UNO;
        ottenutoRisultato = appContext.getBean(QueryBio.class).urlRequest(sorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());

        System.out.println(VUOTA);
        System.out.println(String.format("Trovata la biografia [[%s]] su wikipedia", sorgente));
        printRisultato(ottenutoRisultato);
    }

    @Test
    @Order(4)
    @DisplayName("4- Test per una biografia esistente (getWrap)")
    void getWrap() {
        System.out.println(("4- Test per una biografia esistente (getWrap)"));

        sorgente = PAGINA_ESISTENTE_DUE;
        wrapBio = appContext.getBean(QueryBio.class).getWrap(sorgente);
        assertTrue(wrapBio.isValida());

        System.out.println(VUOTA);
        System.out.println(String.format("Trovata la biografia [[%s]] su wikipedia", wrapBio.getTitle()));
        printWrapBio(wrapBio);
    }


    @Test
    @Order(5)
    @DisplayName("5- Test per una pagina esistente ma non bio")
    void getWrap2() {
        System.out.println(("5- Test per una pagina esistente ma non bio"));

        sorgente = PAGINA_ESISTENTE_TRE;
        wrapBio = appContext.getBean(QueryBio.class).getWrap(sorgente);
        assertFalse(wrapBio.isValida());

        System.out.println(VUOTA);
        System.out.println(String.format("Esiste la pagina [[%s]] ma non è una biografia", sorgente));
        printWrapBio(wrapBio);
    }

    @Test
    @Order(6)
    @DisplayName("6- Test tramite pageid per una pagina esistente ma non biografia")
    void getWrapPageids() {
        System.out.println(("6- Test tramite pageid per una pagina esistente ma non biografia"));

        sorgenteIntero = 2741616;
        wrapBio = appContext.getBean(QueryBio.class).getWrap(sorgenteIntero);
        assertFalse(wrapBio.isValida());

        System.out.println(VUOTA);
        System.out.println(String.format("Esiste la pagina [[%s]] ma non è una biografia", sorgente));
        printWrapBio(wrapBio);
    }


    @Test
    @Order(7)
    @DisplayName("7- Test tramite pageid per una biografia esistente")
    void getWrapPageids2() {
        System.out.println(("7- Test tramite pageid per una biografia esistente"));

        sorgenteIntero = 4292050;
        wrapBio = appContext.getBean(QueryBio.class).getWrap(sorgenteIntero);
        assertTrue(wrapBio.isValida());

        System.out.println(VUOTA);
        System.out.println(String.format("Trovata la biografia [[%s]] su wikipedia", wrapBio.getTitle()));
        printWrapBio(wrapBio);
    }
    @Test
    @Order(8)
    @DisplayName("8- Test per una pagina di disambigua")
    void getWrapDisambigua() {
        System.out.println(("8- Test per una pagina di disambigua"));

        sorgente = "Francisco Cabral";
        wrapBio = appContext.getBean(QueryBio.class).getWrap(sorgente);
        assertFalse(wrapBio.isValida());
        assertEquals(AETypePage.disambigua,wrapBio.getType());

        System.out.println(VUOTA);
        System.out.println(String.format("Esiste la pagina [[%s]] ma è una disambigua", sorgente));
        printWrapBio(wrapBio);
    }

    @Test
    @Order(9)
    @DisplayName("9- Test per una biografia esistente")
    void getWrapEsistente() {
        System.out.println(("9- Test per una biografia esistente"));

        sorgente = "Francisco Cabral (tennista)";
        wrapBio = appContext.getBean(QueryBio.class).getWrap(sorgente);
        assertTrue(wrapBio.isValida());
        assertEquals(AETypePage.testoConTmpl,wrapBio.getType());

        System.out.println(VUOTA);
        System.out.println(String.format("Esiste la pagina [[%s]] ma è una disambigua", sorgente));
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