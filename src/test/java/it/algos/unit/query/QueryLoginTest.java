package it.algos.unit.query;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.login.*;
import it.algos.wiki23.wiki.query.*;
import static it.algos.wiki23.wiki.query.QueryLogin.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.core.env.*;

import java.util.*;


/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: mer, 11-mag-2022
 * Time: 06:52
 * Unit test di una classe service o backend o query <br>
 * Estende la classe astratta AlgosTest che contiene le regolazioni essenziali <br>
 * Nella superclasse AlgosTest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse AlgosTest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("query")
@DisplayName("Test QueryLogin")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QueryLoginTest extends WikiTest {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata dal framework SpringBoot/Vaadin usando il metodo setter() <br>
     * al termine del ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public Environment environment;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public BotLogin botLogin;

    /**
     * Classe principale di riferimento <br>
     */
    private QueryLogin istanza;


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
        botLogin.reset();
    }


    @Test
    @Order(1)
    @DisplayName("1- Costruttore base senza parametri")
    void costruttoreBase() {
        assertTrue(istanza == null);
        istanza = new QueryLogin();
        assertNotNull(istanza);
        System.out.println(("1- Costruttore base senza parametri"));
        System.out.println(VUOTA);
        System.out.println(String.format("Costruttore base senza parametri per un'istanza di %s", istanza.getClass().getSimpleName()));
        printBotLogin();
    }


    @Test
    @Order(2)
    @DisplayName("2 - urlRequest di queryLogin con loginName errato")
    void urlRequestErrata2() {
        System.out.println("2 - Valore errato di loginName");
        System.out.println("Per usare questo test occorre 'oscurare' la property loginName in application.properties");

        //        assertTrue(istanza == null);
        //        istanza = appContext.getBean(QueryLogin.class);
        //        assertNotNull(istanza);
        //        assertFalse(istanza.isValido());
        //
        //        previsto = JSON_FAILED;
        //        ottenutoRisultato = istanza.urlRequest();
        //        assertFalse(ottenutoRisultato.isValido());
        //        assertEquals(PROPERTY_LOGIN_NAME, ottenutoRisultato.getErrorCode());
        //        assertEquals(VUOTA, ottenutoRisultato.getErrorMessage());
        //        printRisultato(ottenutoRisultato);
    }


    @Test
    @Order(3)
    @DisplayName("3 - urlRequest di queryLogin con loginPassword errato")
    void urlRequestErrata3() {
        System.out.println("3 - Valore errato di loginPassword");
        System.out.println("Per usare questo test occorre 'oscurare' la property  loginPassword in application.properties");

        //        assertTrue(istanza == null);
        //        istanza = appContext.getBean(QueryLogin.class);
        //        assertNotNull(istanza);
        //        assertFalse(istanza.isValido());
        //
        //        previsto = JSON_FAILED;
        //        ottenutoRisultato = istanza.urlRequest();
        //        assertFalse(ottenutoRisultato.isValido());
        //        assertEquals(PROPERTY_LOGIN_PASSWORD, ottenutoRisultato.getErrorCode());
        //        assertEquals(VUOTA, ottenutoRisultato.getErrorMessage());
        //        printRisultato(ottenutoRisultato);
    }


    @Test
    @Order(4)
    @DisplayName("4 - urlRequest di queryLogin con loginName/loginPassword errato")
    void urlRequestErrata4() {
        System.out.println("4 - Valore errato di loginName/loginPassword");
        System.out.println("Per usare questo test occorre 'taroccare' (valore errato) una delle due");
        System.out.println("property loginName o loginPassword in application.properties");

        //        assertTrue(istanza == null);
        //        istanza = appContext.getBean(QueryLogin.class);
        //        assertNotNull(istanza);
        //        assertFalse(istanza.isValido());
        //
        //        ottenutoRisultato = istanza.urlRequest();
        //        assertFalse(ottenutoRisultato.isValido());
        //        assertEquals(JSON_FAILED, ottenutoRisultato.getErrorCode());
        //        printRisultato(ottenutoRisultato);
    }

    @Test
    @Order(5)
    @DisplayName("5 - istanza di queryLogin 'Biobot' (valida)")
    void urlRequest() {
        System.out.println("5 - istanza di queryLogin (valida)");

        assertTrue(istanza == null);
        istanza = appContext.getBean(QueryLogin.class);
        assertNotNull(istanza);
        assertFalse(istanza.isValido());

        previsto = JSON_SUCCESS;
        ottenutoRisultato = istanza.urlRequest();
        assertTrue(ottenutoRisultato.isValido());
        assertEquals(previsto, ottenutoRisultato.getCodeMessage());
        assertEquals("lguserid: 124123, lgusername: Biobot", ottenutoRisultato.getValidMessage());
        printRisultato(ottenutoRisultato);
    }

    @Test
    @Order(6)
    @DisplayName("6 - urlRequest di queryLogin 'Biobot' (valida)")
    void urlRequest2() {
        System.out.println("6 - urlRequest di queryLogin (valida) che registra i valori in botLogin");

        previsto = JSON_SUCCESS;
        ottenutoRisultato = appContext.getBean(QueryLogin.class).urlRequest();
        assertTrue(ottenutoRisultato.isValido());
        assertEquals(previsto, ottenutoRisultato.getCodeMessage());
        assertEquals(SUCCESS_LOGIN_RESPONSE, ottenutoRisultato.getMessage());
        assertTrue(botLogin.isBot());
        printRisultato(ottenutoRisultato);
        printBotLogin();
    }


    @Test
    @Order(7)
    @DisplayName("7 - urlRequest di queryLogin 'hamed' - user (valida)")
    void urlRequestUser() {
        System.out.println("7 - urlRequest di queryLogin 'hamed' - user (valida) che registra i valori in botLogin");

        previsto = JSON_SUCCESS;
        ottenutoRisultato = appContext.getBean(QueryLogin.class).urlRequest(AETypeUser.user);
        assertTrue(ottenutoRisultato.isValido());
        assertEquals(previsto, ottenutoRisultato.getCodeMessage());
        assertEquals("lguserid: 1985, lgusername: Hamed", ottenutoRisultato.getMessage());
        assertFalse(botLogin.isBot());
        printRisultato(ottenutoRisultato);
        printBotLogin();
    }

    @Test
    @Order(8)
    @DisplayName("8 - urlRequest di queryLogin 'gac' - admin (valida)")
    void urlRequestAdmin() {
        System.out.println("8 - urlRequest di queryLogin 'gac' - admin (valida) che registra i valori in botLogin");

        previsto = JSON_SUCCESS;
        ottenutoRisultato = appContext.getBean(QueryLogin.class).urlRequest(AETypeUser.admin);
        assertTrue(ottenutoRisultato.isValido());
        assertEquals(previsto, ottenutoRisultato.getCodeMessage());
        assertEquals("lguserid: 399, lgusername: Gac", ottenutoRisultato.getMessage());
        assertFalse(botLogin.isBot());
        printRisultato(ottenutoRisultato);
        printBotLogin();
    }

    protected void printBotLogin() {
        System.out.println(VUOTA);
        System.out.println("Valori attuali del singleton BotLogin");
        System.out.println(String.format("Valido: %s", botLogin.isValido() ? "true" : "false"));
        System.out.println(String.format("Bot: %s", botLogin.isBot() ? "true" : "false"));
        System.out.println(String.format("Userid: %d", botLogin.getUserid()));
        System.out.println(String.format("Username: %s", botLogin.getUsername()));
        System.out.println(String.format("UserType: %s", botLogin.getUserType()));
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