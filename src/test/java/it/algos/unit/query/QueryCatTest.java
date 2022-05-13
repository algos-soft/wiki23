package it.algos.unit.query;


import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.wiki.query.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.springframework.boot.test.context.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: mer, 11-mag-2022
 * Time: 17:42
 * Unit test di una classe service o backend o query <br>
 * Estende la classe astratta AlgosTest che contiene le regolazioni essenziali <br>
 * Nella superclasse AlgosTest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse AlgosTest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("query")
@DisplayName("Test QueryCat")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QueryCatTest extends WikiTest {


    /**
     * Classe principale di riferimento <br>
     */
    private QueryCat istanza;


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
    @DisplayName("1 - Costruttore base senza parametri")
    void costruttoreBase() {
        istanza = new QueryCat();
        assertNotNull(istanza);
        System.out.println(("1 - Costruttore base senza parametri"));
        System.out.println(VUOTA);
        System.out.println(String.format("Costruttore base senza parametri per un'istanza di %s", istanza.getClass().getSimpleName()));
    }

    //    @Test
    @Order(2)
    @DisplayName("2 - Test per una categoria inesistente")
    void nonEsiste() {
        System.out.println(("2 - Test per una categoria inesistente"));
        assertTrue(istanza == null);
        istanza = appContext.getBean(QueryCat.class);
        assertNotNull(istanza);

        sorgente = CATEGORIA_INESISTENTE;
        ottenutoRisultato = istanza.urlRequest(sorgente);
        assertNotNull(ottenutoRisultato);
        assertFalse(ottenutoRisultato.isValido());

        System.out.println(VUOTA);
        System.out.println(String.format("La categoria [[%s]] non esiste su wikipedia", sorgente));
        printRisultato(ottenutoRisultato);
    }

    //    @Test
    @Order(3)
    @DisplayName("3 - Categoria esistente senza login")
    void esiste3() {
        System.out.println(("3 - Categoria esistente senza login"));
        assertTrue(istanza == null);
        istanza = appContext.getBean(QueryCat.class);
        assertNotNull(istanza);

        sorgente = CATEGORIA_ESISTENTE_UNO;
        ottenutoRisultato = istanza.urlRequest(sorgente);
        assertNotNull(ottenutoRisultato);
        assertFalse(ottenutoRisultato.isValido());

        System.out.println(VUOTA);
        System.out.println(String.format("Trovata la categoria [[%s]] su wikipedia", sorgente));
        printRisultato(ottenutoRisultato);
    }

    //    @Test
    @Order(4)
    @DisplayName("4 - Categoria esistente login come user")
    void esisteUser() {
        System.out.println(("4 - Categoria esistente login come user"));
        appContext.getBean(QueryLogin.class).urlRequest(AETypeUser.user);

        sorgente = CATEGORIA_ESISTENTE_UNO;
        ottenutoRisultato = appContext.getBean(QueryCat.class).urlRequest(sorgente);
        assertNotNull(ottenutoRisultato);
        //        assertTrue(ottenutoRisultato.isValido());

        System.out.println(VUOTA);
        System.out.println(String.format("Trovata la categoria [[%s]] su wikipedia", sorgente));
        printRisultato(ottenutoRisultato);
    }

    @Test
    @Order(30)
    @DisplayName("30 - Obbligatorio PRIMA del 31 per regolare il botLogin")
    void nonCollegato() {
        botLogin.reset();

        assertNotNull(botLogin);
        assertTrue(botLogin.getUserType().name().equals(AETypeUser.anonymous.name()));
    }

    @ParameterizedTest
    @MethodSource(value = "CATEGORIE")
    @Order(31)
    @DisplayName("31- Test per categorie senza collegamento")
        //--categoria
        //--esiste
    void esisteNonCollegato(final String wikiCategoria, final boolean categoriaEsistente) {
        System.out.println("31 - Test per categorie senza collegamento");
        System.out.println("Il botLogin è stato regolato nel test '30'");

        ottenutoRisultato = appContext.getBean(QueryCat.class).urlRequest(wikiCategoria);
        assertNotNull(ottenutoRisultato);
        assertEquals(categoriaEsistente, ottenutoRisultato.isValido());

        System.out.println(VUOTA);
        System.out.println(String.format("Esamino la categoria [[%s]] in collegamento come anonymous", wikiCategoria));
        System.out.println(VUOTA);
        printRisultato(ottenutoRisultato);
    }


    @Test
    @Order(40)
    @DisplayName("40 - Obbligatorio PRIMA del 41 per regolare il botLogin")
    void collegatoUser() {
        botLogin.reset();

        appContext.getBean(QueryLogin.class).urlRequest(AETypeUser.user);
        assertNotNull(botLogin);
        assertTrue(botLogin.isValido());
        assertEquals(botLogin.getUserType(), AETypeUser.user);
    }


    @ParameterizedTest
    @MethodSource(value = "CATEGORIE")
    @Order(41)
    @DisplayName("41- Test per categorie collegamento user")
        //--categoria
        //--esiste
    void esisteCollegatoUser(final String wikiCategoria, final boolean categoriaEsistente) {
        System.out.println("41 - Test per categorie collegamento user");
        System.out.println("Il botLogin è stato regolato nel test '40'");

        ottenutoRisultato = appContext.getBean(QueryCat.class).urlRequest(wikiCategoria);
        assertNotNull(ottenutoRisultato);
        assertEquals(categoriaEsistente, ottenutoRisultato.isValido());

        System.out.println(VUOTA);
        System.out.println(String.format("Esamino la categoria [[%s]] in collegamento come user", wikiCategoria));
        System.out.println(VUOTA);
        printRisultato(ottenutoRisultato);
    }

    @Test
    @Order(50)
    @DisplayName("50 - Obbligatorio PRIMA del 51 per regolare il botLogin")
    void collegatoAdmin() {
        botLogin.reset();

        appContext.getBean(QueryLogin.class).urlRequest(AETypeUser.admin);
        assertNotNull(botLogin);
        assertTrue(botLogin.isValido());
        assertEquals(botLogin.getUserType(), AETypeUser.admin);
    }


    @ParameterizedTest
    @MethodSource(value = "CATEGORIE")
    @Order(51)
    @DisplayName("51- Test per categorie collegamento admin")
        //--categoria
        //--esiste
    void esisteCollegatoAdmin(final String wikiCategoria, final boolean categoriaEsistente) {
        System.out.println("51 - Test per categorie collegamento admin");
        System.out.println("Il botLogin è stato regolato nel test '50'");

        ottenutoRisultato = appContext.getBean(QueryCat.class).urlRequest(wikiCategoria);
        assertNotNull(ottenutoRisultato);
        assertEquals(categoriaEsistente, ottenutoRisultato.isValido());

        System.out.println(VUOTA);
        System.out.println(String.format("Esamino la categoria [[%s]] in collegamento come admin", wikiCategoria));
        System.out.println(VUOTA);
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