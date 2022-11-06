package it.algos.unit.query;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.wiki.query.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.boot.test.context.*;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: mar, 17-mag-2022
 * Time: 19:07
 * Unit test di una classe service o backend o query <br>
 * Estende la classe astratta AlgosTest che contiene le regolazioni essenziali <br>
 * Nella superclasse AlgosTest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse AlgosTest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("query")
@DisplayName("Test QueryTimestamp")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QueryTimestampTest extends WikiTest {


    /**
     * Classe principale di riferimento <br>
     */
    private QueryTimestamp istanza;


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
        istanza = new QueryTimestamp();
        assertNotNull(istanza);
        System.out.println(("1 - Costruttore base senza parametri"));
        System.out.println(VUOTA);
        System.out.println(String.format("Costruttore base senza parametri per un'istanza di %s", istanza.getClass().getSimpleName()));
    }

    @Test
    @Order(2)
    @DisplayName("2 - Test per una request errata")
    void errata() {
        System.out.println(("2 - Test per una request errata"));

        ottenutoRisultato = appContext.getBean(QueryTimestamp.class).urlRequest(null);
        assertNotNull(ottenutoRisultato);
        assertFalse(ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato);
    }


    @Test
    @Order(3)
    @DisplayName("3 - Test per un pageIds")
    void pagina() {
        System.out.println(("3 - Test per un pageIds"));
        assertTrue(istanza == null);
        istanza = appContext.getBean(QueryTimestamp.class);
        assertNotNull(istanza);

        listaPageIds = new ArrayList<>();
        listaPageIds.add(132555L);
        ottenutoRisultato = istanza.urlRequest(listaPageIds);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());

        listMiniWrap = istanza.getWrap(listaPageIds);
        assertNotNull(listMiniWrap);

        System.out.println(VUOTA);
        printRisultato(ottenutoRisultato);
        printMiniWrap(listMiniWrap);
    }


    @Test
    @Order(4)
    @DisplayName("4 - Test per due pageIds")
    void duePagine() {
        System.out.println(("4 - Test per due pageIds"));
        assertTrue(istanza == null);
        istanza = appContext.getBean(QueryTimestamp.class);
        assertNotNull(istanza);

        listaPageIds = new ArrayList<>();
        listaPageIds.add(132555L);
        listaPageIds.add(134246L);
        ottenutoRisultato = istanza.urlRequest(listaPageIds);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());

        listMiniWrap = istanza.getWrap(listaPageIds);
        assertNotNull(listMiniWrap);

        System.out.println(VUOTA);
        printRisultato(ottenutoRisultato);
        printMiniWrap(listMiniWrap);
    }


    @Test
    @Order(5)
    @DisplayName("5 - Test per categoria piccola")
    void categoria() {
        System.out.println(("5 - Test per categoria piccola"));
        assertTrue(istanza == null);

        sorgente = CATEGORIA_ESISTENTE_BREVE;
        listaPageIds = queryService.getListaPageIds(sorgente);
        assertNotNull(listaPageIds);

        ottenutoRisultato = appContext.getBean(QueryTimestamp.class).urlRequest(listaPageIds);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());

        listMiniWrap = ottenutoRisultato.getLista();
        assertNotNull(listMiniWrap);

        System.out.println(VUOTA);
        printRisultato(ottenutoRisultato);
        printMiniWrap(listMiniWrap);
    }


    @Test
    @Order(6)
    @DisplayName("6 - Test per categoria media")
    void categoria2() {
        System.out.println(("6 - Test per categoria media"));
        assertTrue(istanza == null);

        sorgente = CATEGORIA_ESISTENTE_MEDIA;
        listaPageIds = queryService.getListaPageIds(sorgente);
        assertNotNull(listaPageIds);

        ottenutoRisultato = appContext.getBean(QueryTimestamp.class).urlRequest(listaPageIds);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());

        listMiniWrap = ottenutoRisultato.getLista();
        assertNotNull(listMiniWrap);

        System.out.println(VUOTA);
        printRisultato(ottenutoRisultato);
        printMiniWrap(listMiniWrap);
    }


    @Test
    @Order(7)
    @DisplayName("7 - Test per categoria grande")
    void categoria3() {
        System.out.println(("7 - Test per categoria grande"));
        assertTrue(istanza == null);

        sorgente = CATEGORIA_ESISTENTE_LUNGA;

        //--si collega come anonymous
        appContext.getBean(QueryLogin.class).urlRequest(AETypeUser.anonymous);
        assertNotNull(botLogin);
        assertFalse(botLogin.isValido());
        assertEquals(botLogin.getUserType(), AETypeUser.anonymous);

        //--collegato come anonymous - nessun valore per la lista pageIds
        listaPageIds = queryService.getListaPageIds(sorgente);
        assertNull(listaPageIds);

        //--si collega come user/admin
        appContext.getBean(QueryLogin.class).urlRequest(AETypeUser.user);
        assertNotNull(botLogin);
        assertTrue(botLogin.isValido());
        assertEquals(botLogin.getUserType(), AETypeUser.user);

        //--collegato come user/admin
        //--la listaPageIds la recupera comunque dalla categoria che ha criteri ''più permissivi''
        listaPageIds = queryService.getListaPageIds(sorgente);
        assertNotNull(listaPageIds);

        //--la queryTimestamp invece ha bisogno di un collegamento come bot anche per valori più bassi di pageIds
        ottenutoRisultato = appContext.getBean(QueryTimestamp.class).urlRequest(listaPageIds);
        assertNotNull(ottenutoRisultato);
        assertFalse(ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato);
    }


    @Test
    @Order(8)
    @DisplayName("8 - Test per categoria grande come bot")
    void categoria4() {
        System.out.println(("8 - Test per categoria grande come bot"));
        assertTrue(istanza == null);

        sorgente = CATEGORIA_ESISTENTE_LUNGA;

        //--si collega come bot
        appContext.getBean(QueryLogin.class).urlRequest(AETypeUser.bot);
        assertNotNull(botLogin);
        assertTrue(botLogin.isValido());
        assertEquals(botLogin.getUserType(), AETypeUser.bot);

        //--collegato come bot
        listaPageIds = queryService.getListaPageIds(sorgente);
        assertNotNull(listaPageIds);

        ottenutoRisultato = appContext.getBean(QueryTimestamp.class).urlRequest(listaPageIds);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato);

        listMiniWrap = ottenutoRisultato.getLista();
        assertNotNull(listMiniWrap);

        System.out.println(VUOTA);
        System.out.println(String.format("Recuperati %s miniWrap",textService.format(listMiniWrap.size())));
        printMiniWrap(listMiniWrap.subList(0, Math.min(10, listMiniWrap.size())));
    }


//    @Test
    @Order(9)
    @DisplayName("9 - Test per categoria BioBot")
    void categoria5() {
        System.out.println(("9 - Test per categoria BioBot"));
        assertTrue(istanza == null);

        sorgente = CATEGORIA_PRINCIPALE_BIOBOT;

        //--si collega come bot
        appContext.getBean(QueryLogin.class).urlRequest(AETypeUser.bot);
        assertNotNull(botLogin);
        assertTrue(botLogin.isValido());
        assertEquals(botLogin.getUserType(), AETypeUser.bot);

        //--collegato come bot
        listaPageIds = queryService.getListaPageIds(sorgente);
        assertNotNull(listaPageIds);

        ottenutoRisultato = appContext.getBean(QueryTimestamp.class).urlRequest(listaPageIds);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato);

        listMiniWrap = ottenutoRisultato.getLista();
        assertNotNull(listMiniWrap);

        System.out.println(VUOTA);
        System.out.println(String.format("Recuperati %s miniWrap",textService.format(listMiniWrap.size())));
        printMiniWrap(listMiniWrap.subList(0, Math.min(10, listMiniWrap.size())));
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