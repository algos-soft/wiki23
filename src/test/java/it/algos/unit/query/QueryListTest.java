package it.algos.unit.query;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.wiki.query.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Wed, 21-Sep-2022
 * Time: 19:22
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("query")
@DisplayName("Test QueryList")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QueryListTest extends WikiTest {


    /**
     * Classe principale di riferimento <br>
     */
    private QueryList istanza;


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
        istanza = new QueryList();
        assertNotNull(istanza);
        System.out.println(("1 - Costruttore base senza parametri"));
        System.out.println(VUOTA);
        System.out.println(String.format("Costruttore base senza parametri per un'istanza di %s", istanza.getClass().getSimpleName()));
    }


//    @Test
    @Order(2)
    @DisplayName("2 - Request errata. Manca il tag iniziale")
    void manca() {
        System.out.println(("2 - Request errata. Manca il tag iniziale"));
        System.out.println(VUOTA);

        ottenutoRisultato = appContext.getBean(QueryList.class).urlRequest(sorgente);
        assertNotNull(ottenutoRisultato);
        assertFalse(ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato);
    }

//    @Test
    @Order(3)
    @DisplayName("3 - Request errata. Non esiste nessuna pagina")
    void inesistente() {
        System.out.println(("3 - Request errata. Non esiste nessuna pagina"));
        System.out.println(VUOTA);

        sorgente = "Pipponebelloz";
        ottenutoRisultato = appContext.getBean(QueryList.class).urlRequest(sorgente);
        assertNotNull(ottenutoRisultato);
        assertFalse(ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato);
    }

//    @Test
    @Order(4)
    @DisplayName("4 - Request valida")
    void valida() {
        System.out.println(("4 - Request valida"));
        System.out.println(VUOTA);

        sorgente = "Pippo";
        ottenutoRisultato = appContext.getBean(QueryList.class).urlRequest(sorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato);
    }

//    @Test
    @Order(5)
    @DisplayName("5 - Request namespace errato")
    void errataUser() {
        System.out.println(("5 - namespace errato"));
        System.out.println(VUOTA);

        sorgente = "Utente:Biobot/2";
        ottenutoRisultato = appContext.getBean(QueryList.class).urlRequest(sorgente);
        assertNotNull(ottenutoRisultato);
        assertFalse(ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato);
    }

//    @Test
    @Order(6)
    @DisplayName("6 - Request namespace corretto")
    void validaUser6() {
        System.out.println(("6 - namespace corretto"));
        System.out.println(VUOTA);

        sorgente = "Biobot/2";
        sorgenteIntero = 2;
        ottenutoRisultato = appContext.getBean(QueryList.class).nameSpace(sorgenteIntero).urlRequest(sorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato);
    }

//    @Test
    @Order(7)
    @DisplayName("7 - Request namespace corretto")
    void validaUser7() {
        System.out.println(("7 - namespace corretto"));
        System.out.println(VUOTA);

        sorgente = "Biobot/Nati";
        sorgenteIntero = 2;
        ottenutoRisultato = appContext.getBean(QueryList.class).nameSpace(sorgenteIntero).urlRequest(sorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato);
        System.out.println(VUOTA);
        System.out.println(String.format("Lettere iniziali: %s",sorgente));
        printString((List<String>)ottenutoRisultato.getLista());
    }


//    @Test
    @Order(8)
    @DisplayName("8 - Request namespace utente")
    void validaUser8() {
        System.out.println(("8 - namespace utente"));
        System.out.println(VUOTA);

        sorgente = "Biobot/Morti";
        sorgenteIntero = 2;
        listaStr = appContext.getBean(QueryList.class).nameSpace(sorgenteIntero).getLista(sorgente);
        assertNotNull(listaStr);
        System.out.println(VUOTA);
        System.out.println(String.format("Lettere iniziali: %s",sorgente));
        printString(listaStr);
    }

//    @Test
    @Order(9)
    @DisplayName("9 - Request namespace progetto")
    void validaUser9() {
        System.out.println(("9 - namespace progetto"));
        System.out.println(VUOTA);

        sorgente = "Biografie/Attività/Ba";
        sorgenteIntero = 102;
        listaStr = appContext.getBean(QueryList.class).nameSpace(sorgenteIntero).getLista(sorgente);
        assertNotNull(listaStr);
        System.out.println(VUOTA);
        System.out.println(String.format("Lettere iniziali: %s",sorgente));
        printString(listaStr);
    }


    @Test
    @Order(10)
    @DisplayName("10 - Request lista 'lunga' (oltre le 500)")
    void validaUser10() {
        System.out.println(("10 - Request lista 'lunga' (oltre le 500)"));

        sorgente = "Biografie/Attività/";
        sorgenteIntero = 102;
        listaStr = appContext.getBean(QueryList.class).nameSpace(sorgenteIntero).getLista(sorgente);
        assertNotNull(listaStr);
        System.out.println(VUOTA);
        System.out.println(String.format("Lettere iniziali: %s",sorgente));
        printString(listaStr);
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
