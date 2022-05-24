package it.algos.unit.service;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.service.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: mer, 18-mag-2022
 * Time: 11:25
 * <p>
 * Unit test di una classe di servizio (di norma) <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("service")
@Tag("query")
@DisplayName("Query service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QueryServiceTest extends WikiTest {


    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private QueryService service;

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
        service = queryService;
    }


    /**
     * Qui passa prima di ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUpEach() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    protected void setUpEach() {
        super.setUpEach();
    }


    @Test
    @Order(1)
    @DisplayName("1 - Esistenza del login")
    void login() {
        ottenutoBooleano = service.esisteLogin();
        assertTrue(ottenutoBooleano);
    }

    @Test
    @Order(2)
    @DisplayName("2 - Esistenza del botLogin collegato come bot")
    void botLogin() {
        //--non collegato come bot - controllo
        ottenutoBooleano = service.isBotLogin();
        assertFalse(ottenutoBooleano);

        //--si collega come bot
        ottenutoBooleano = service.logAsBot();
        assertTrue(ottenutoBooleano);

        //--collegato come bot - controllo
        ottenutoBooleano = service.isBotLogin();
        assertTrue(ottenutoBooleano);
    }

    @Test
    @Order(3)
    @DisplayName("3 - Type attuale del collegamento")
    void getLoginUserType() {
        typeUser = service.getLoginUserType();
        assertNotNull(typeUser);
        System.out.println(typeUser);
    }


    @Test
    @Order(4)
    @DisplayName("4 - Si collega come ''user'")
    void logAsUser() {
        //--si collega come user
        ottenutoBooleano = service.logAsUser();
        assertTrue(ottenutoBooleano);

        //--collegato come user - controllo
        typeUser = service.getLoginUserType();
        assertNotNull(typeUser);
        assertEquals(AETypeUser.user, typeUser);
    }

    @Test
    @Order(5)
    @DisplayName("5 - Si collega come ''admin'")
    void logAsAdmin() {
        //--si collega come admin
        ottenutoBooleano = service.logAsAdmin();
        assertTrue(ottenutoBooleano);

        //--collegato come admin - controllo
        typeUser = service.getLoginUserType();
        assertNotNull(typeUser);
        assertEquals(AETypeUser.admin, typeUser);
    }

    @Test
    @Order(6)
    @DisplayName("6 - Si collega come ''bot'")
    void logAsBot() {
        //--si collega come bot
        ottenutoBooleano = service.logAsBot();
        assertTrue(ottenutoBooleano);

        //--collegato come bot - controllo
        typeUser = service.getLoginUserType();
        assertNotNull(typeUser);
        assertEquals(AETypeUser.bot, typeUser);
    }

    @Test
    @Order(7)
    @DisplayName("7 - Si collega secondo il type passato")
    void logAsType() {
        //--collegato come anonymous - controllo
        typeUser = service.getLoginUserType();
        assertNotNull(typeUser);
        assertEquals(AETypeUser.anonymous, typeUser);

        //--si collega
        typeUser = service.logAsType(AETypeUser.user);
        assertNotNull(typeUser);
        assertEquals(AETypeUser.user, typeUser);

        //--collegato come user - controllo
        typeUser = service.getLoginUserType();
        assertNotNull(typeUser);
        assertEquals(AETypeUser.user, typeUser);

        //--si collega
        typeUser = service.logAsType(AETypeUser.admin);
        assertNotNull(typeUser);
        assertEquals(AETypeUser.admin, typeUser);

        //--collegato come admin - controllo
        typeUser = service.getLoginUserType();
        assertNotNull(typeUser);
        assertEquals(AETypeUser.admin, typeUser);

        //--si collega
        typeUser = service.logAsType(AETypeUser.bot);
        assertNotNull(typeUser);
        assertEquals(AETypeUser.bot, typeUser);

        //--collegato come admin - controllo
        typeUser = service.getLoginUserType();
        assertNotNull(typeUser);
        assertEquals(AETypeUser.bot, typeUser);
    }


    @Test
    @Order(8)
    @DisplayName("8 - Controlla di essere collegato come ''bot'")
    void assertBot() {
        //--si collega come bot
        ottenutoBooleano = service.logAsBot();
        assertTrue(ottenutoBooleano);

        //--collegato come bot - controllo
        ottenutoBooleano = service.assertBot();
        assertTrue(ottenutoBooleano);
    }

    @Test
    @Order(9)
    @DisplayName("9 - Size della categoria")
    void getSizeCat() {
        sorgente = CATEGORIA_PRINCIPALE_BIOBOT;
        ottenutoIntero = service.getSizeCat(sorgente);
        assertTrue(ottenutoIntero > 0);
        System.out.println(String.format("La categoria '%s' contiene %s pagine", sorgente, textService.format(ottenutoIntero)));
    }

    @Test
    @Order(10)
    @DisplayName("10 - pageIds della categoria")
    void getListaPageIds() {
        String message;
        sorgente = CATEGORIA_ESISTENTE_BREVE;
        listaPageIds = service.getListaPageIds(sorgente);
        message = String.format("La categoria [[%s]] contiene %d elementi. Ne stampo SOLO i primi 10 (se ci sono)", sorgente, listaPageIds.size());
        System.out.println(message);
        printLista(listaPageIds.subList(0, Math.min(10, listaPageIds.size())));

        System.out.println(VUOTA);
        sorgente = CATEGORIA_ESISTENTE_MEDIA;
        listaPageIds = service.getListaPageIds(sorgente);
        message = String.format("La categoria [[%s]] contiene %d elementi. Ne stampo SOLO i primi 10 (se ci sono)", sorgente, listaPageIds.size());
        System.out.println(message);
        printLista(listaPageIds.subList(0, Math.min(10, listaPageIds.size())));
    }

    @Test
    @Order(11)
    @DisplayName("11 - miniWrap dai pageIds")
    void getMiniWrap() {
        String message;
        sorgente = CATEGORIA_ESISTENTE_MEDIA;
        listaPageIds = service.getListaPageIds(sorgente);
        listMiniWrap = service.getMiniWrap(listaPageIds);
        assertNotNull(listMiniWrap);
        printMiniWrap(listMiniWrap);
        message = String.format("La categoria [[%s]] contiene %d elementi. Ne stampo SOLO i primi 10 (se ci sono)", sorgente, listMiniWrap.size());
        System.out.println(message);
    }

    @Test
    @Order(12)
    @DisplayName("12 - bioWrap dai pageIds")
    void getBioWrap() {
        String message;
        sorgente = CATEGORIA_ESISTENTE_MEDIA;
        listaPageIds = service.getListaPageIds(sorgente);
        listWrapBio = service.getBioWrap(listaPageIds);
        assertNotNull(listWrapBio);
        message = String.format("La categoria [[%s]] contiene %d elementi. Ne stampo SOLO i primi 10 (se ci sono)", sorgente, listWrapBio.size());
        System.out.println(message);
        printWrapBio(listWrapBio);
    }

    @Test
    @Order(13)
    @DisplayName("13 - bioWrap di una singola pagina")
    void getBioWrapSinglePage() {
        sorgente = PAGINA_ESISTENTE_UNO;
        wrapBio = service.getBioWrap(sorgente);
        assertNotNull(wrapBio);
        System.out.println(String.format("Trovata la biografia [[%s]] su wikipedia", wrapBio.getTitle()));
        printWrapBio(wrapBio);
    }

    @Test
    @Order(14)
    @DisplayName("14 - tmpl di una singola biografia")
    void getBioTmpl() {
        sorgente = PAGINA_ESISTENTE_DUE;
        ottenuto = service.getBioTmpl(sorgente);
        assertTrue(textService.isValid(ottenuto));
        System.out.println(String.format("Trovata la biografia [[%s]] su wikipedia", sorgente));
        System.out.println(String.format("templBio: %s", getMax(ottenuto)));
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