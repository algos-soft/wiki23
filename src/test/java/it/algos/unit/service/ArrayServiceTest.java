package it.algos.unit.service;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.html.*;
import it.algos.test.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.service.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: sab, 12-mar-2022
 * Time: 17:44
 * <p>
 * Unit test di una classe di servizio (di norma) <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("quickly")
@DisplayName("Array service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ArrayServiceTest extends ATest {


    /**
     * The constant ARRAY_STRING.
     */
    protected static final String[] ARRAY_STRING = {"primo", "secondo", "quarto", "quinto", "1Ad", "terzo", "a10"};

    /**
     * The constant LIST_STRING.
     */
    protected static final List<String> LIST_STRING = new ArrayList(Arrays.asList(ARRAY_STRING));


    /**
     * The constant ARRAY_OBJECT.
     */
    protected static final Object[] ARRAY_OBJECT = {new Label("Alfa"), new Button()};

    /**
     * The constant LIST_OBJECT.
     */
    protected static final List<Object> LIST_OBJECT = new ArrayList(Arrays.asList(ARRAY_OBJECT));

    /**
     * The constant ARRAY_LONG.
     */
    protected static final Long[] ARRAY_LONG = {234L, 85L, 151099L, 123500L, 3L, 456772L};

    /**
     * The constant LIST_LONG.
     */
    protected static final ArrayList<Long> LIST_LONG = new ArrayList(Arrays.asList(ARRAY_LONG));


    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private ArrayService service;

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
        service = arrayService;
    }


    /**
     * Qui passa prima di ogni test <br>
     * Invocare PRIMA il metodo setUpEach() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    protected void setUpEach() {
        super.setUpEach();
    }


    @Test
    @Order(1)
    @DisplayName("1 - isAllValid (since Java 11) array")
    void isAllValid() {
        listaStr = new ArrayList<>();

        ottenutoBooleano = service.isAllValid((List) null);
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = service.isAllValid((Map) null);
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = service.isAllValid((new ArrayList()));
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = service.isAllValid((listaStr));
        assertFalse(ottenutoBooleano);

        listaStr.add(null);
        ottenutoBooleano = service.isAllValid(listaStr);
        assertFalse(ottenutoBooleano);

        listaStr = new ArrayList<>();
        listaStr.add(VUOTA);
        ottenutoBooleano = service.isAllValid(listaStr);
        assertFalse(ottenutoBooleano);

        listaStr = new ArrayList<>();
        listaStr.add(PIENA);
        ottenutoBooleano = service.isAllValid(listaStr);
        assertTrue(ottenutoBooleano);

        listaStr.add("Mario");
        ottenutoBooleano = service.isAllValid(listaStr);
        assertTrue(ottenutoBooleano);

        listaStr.add(null);
        ottenutoBooleano = service.isAllValid(listaStr);
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = service.isAllValid(LIST_STRING);
        assertTrue(ottenutoBooleano);

        ottenutoBooleano = service.isAllValid(LIST_OBJECT);
        assertTrue(ottenutoBooleano);

        ottenutoBooleano = service.isAllValid(LIST_LONG);
        assertTrue(ottenutoBooleano);
    }


    @Test
    @Order(2)
    @DisplayName("2 - isAllValid (since Java 11) matrice")
    void isAllValid2() {
        sorgenteMatrice = null;
        ottenutoBooleano = service.isAllValid((String[]) null);
        assertFalse(ottenutoBooleano);

        sorgenteMatrice = null;
        ottenutoBooleano = service.isAllValid(sorgenteMatrice);
        assertFalse(ottenutoBooleano);

        sorgenteMatrice = new String[]{"Codice", "Regioni"};
        ottenutoBooleano = service.isAllValid(sorgenteMatrice);
        assertTrue(ottenutoBooleano);

        sorgenteMatrice = new String[]{VUOTA, "Regioni"};
        ottenutoBooleano = service.isAllValid(sorgenteMatrice);
        assertFalse(ottenutoBooleano);

        sorgenteMatrice = new String[]{VUOTA};
        ottenutoBooleano = service.isAllValid(sorgenteMatrice);
        assertFalse(ottenutoBooleano);

        sorgenteMatrice = new String[]{"Mario", VUOTA, "Regioni"};
        ottenutoBooleano = service.isAllValid(sorgenteMatrice);
        assertFalse(ottenutoBooleano);

        sorgenteMatrice = new String[]{VUOTA, VUOTA, VUOTA};
        ottenutoBooleano = service.isAllValid(sorgenteMatrice);
        assertFalse(ottenutoBooleano);
    }


    @Test
    @Order(3)
    @DisplayName("3 - isAllValid (since Java 11) map")
    void isAllValid3() {
        mappaSorgente = null;
        ottenutoBooleano = service.isAllValid((LinkedHashMap) null);
        assertFalse(ottenutoBooleano);

        mappaSorgente = null;
        ottenutoBooleano = service.isAllValid(mappaSorgente);
        assertFalse(ottenutoBooleano);

        mappaSorgente = new HashMap();
        ottenutoBooleano = service.isAllValid(mappaSorgente);
        assertFalse(ottenutoBooleano);

        mappaSorgente = new LinkedHashMap();
        ottenutoBooleano = service.isAllValid(mappaSorgente);
        assertFalse(ottenutoBooleano);

        mappaSorgente = new LinkedHashMap();
        mappaSorgente.put("beta", "irrilevante");
        mappaSorgente.put(null, "irrilevante2");
        mappaSorgente.put("delta", "irrilevante3");
        ottenutoBooleano = service.isAllValid(mappaSorgente);
        assertFalse(ottenutoBooleano);

        mappaSorgente = new LinkedHashMap();
        mappaSorgente.put("beta", "irrilevante");
        mappaSorgente.put(VUOTA, "irrilevante2");
        mappaSorgente.put("delta", "irrilevante3");
        ottenutoBooleano = service.isAllValid(mappaSorgente);
        assertFalse(ottenutoBooleano);

        mappaSorgente = new LinkedHashMap();
        mappaSorgente.put("beta", "irrilevante");
        mappaSorgente.put("alfa", null);
        mappaSorgente.put("delta", "irrilevante3");
        ottenutoBooleano = service.isAllValid(mappaSorgente);
        assertTrue(ottenutoBooleano);

        mappaSorgente = new LinkedHashMap();
        mappaSorgente.put("beta", "irrilevante");
        mappaSorgente.put("alfa", VUOTA);
        mappaSorgente.put("delta", "irrilevante3");
        ottenutoBooleano = service.isAllValid(mappaSorgente);
        assertTrue(ottenutoBooleano);
    }

    @Test
    @Order(4)
    @DisplayName("4 - isEmpty (since Java 11) array")
    void isEmpty() {
        listaStr = new ArrayList<>();

        ottenutoBooleano = service.isEmpty((List) null);
        assertTrue(ottenutoBooleano);

        ottenutoBooleano = service.isEmpty((Map) null);
        assertTrue(ottenutoBooleano);

        ottenutoBooleano = service.isEmpty((new ArrayList()));
        assertTrue(ottenutoBooleano);

        ottenutoBooleano = service.isEmpty((listaStr));
        assertTrue(ottenutoBooleano);

        listaStr.add(null);
        ottenutoBooleano = service.isEmpty(listaStr);
        assertTrue(ottenutoBooleano);

        listaStr = new ArrayList<>();
        listaStr.add(VUOTA);
        ottenutoBooleano = service.isEmpty(listaStr);
        assertTrue(ottenutoBooleano);

        listaStr = new ArrayList<>();
        listaStr.add(PIENA);
        ottenutoBooleano = service.isEmpty(listaStr);
        assertFalse(ottenutoBooleano);

        listaStr.add("Mario");
        ottenutoBooleano = service.isEmpty(listaStr);
        assertFalse(ottenutoBooleano);

        listaStr.add(null);
        ottenutoBooleano = service.isEmpty(listaStr);
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = service.isEmpty(LIST_STRING);
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = service.isEmpty(LIST_OBJECT);
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = service.isEmpty(LIST_LONG);
        assertFalse(ottenutoBooleano);
    }


    @Test
    @Order(5)
    @DisplayName("5 - isEmpty (since Java 11) matrice")
    void isEmpty2() {
        sorgenteMatrice = null;
        ottenutoBooleano = service.isEmpty((String[]) null);
        assertTrue(ottenutoBooleano);

        sorgenteMatrice = null;
        ottenutoBooleano = service.isEmpty(sorgenteMatrice);
        assertTrue(ottenutoBooleano);

        sorgenteMatrice = new String[]{"Codice", "Regioni"};
        ottenutoBooleano = service.isEmpty(sorgenteMatrice);
        assertFalse(ottenutoBooleano);

        sorgenteMatrice = new String[]{VUOTA, "Regioni"};
        ottenutoBooleano = service.isEmpty(sorgenteMatrice);
        assertFalse(ottenutoBooleano);

        sorgenteMatrice = new String[]{VUOTA};
        ottenutoBooleano = service.isEmpty(sorgenteMatrice);
        assertTrue(ottenutoBooleano);

        sorgenteMatrice = new String[]{"Mario", VUOTA, "Regioni"};
        ottenutoBooleano = service.isEmpty(sorgenteMatrice);
        assertFalse(ottenutoBooleano);

        sorgenteMatrice = new String[]{VUOTA, VUOTA, VUOTA};
        ottenutoBooleano = service.isEmpty(sorgenteMatrice);
        assertTrue(ottenutoBooleano);
    }


    @Test
    @Order(6)
    @DisplayName("6 - isEmpty (since Java 11) map")
    void isEmpty3() {
        mappaSorgente = null;
        ottenutoBooleano = service.isEmpty((LinkedHashMap) null);
        assertTrue(ottenutoBooleano);

        mappaSorgente = null;
        ottenutoBooleano = service.isEmpty(mappaSorgente);
        assertTrue(ottenutoBooleano);

        mappaSorgente = new HashMap();
        ottenutoBooleano = service.isEmpty(mappaSorgente);
        assertTrue(ottenutoBooleano);

        mappaSorgente = new LinkedHashMap();
        ottenutoBooleano = service.isEmpty(mappaSorgente);
        assertTrue(ottenutoBooleano);

        mappaSorgente = new LinkedHashMap();
        mappaSorgente.put("", "irrilevante");
        mappaSorgente.put(null, "irrilevante2");
        ottenutoBooleano = service.isEmpty(mappaSorgente);
        assertTrue(ottenutoBooleano);

        mappaSorgente = new LinkedHashMap();
        mappaSorgente.put("beta", "irrilevante");
        mappaSorgente.put(VUOTA, "irrilevante2");
        mappaSorgente.put("delta", "irrilevante3");
        ottenutoBooleano = service.isEmpty(mappaSorgente);
        assertFalse(ottenutoBooleano);

        mappaSorgente = new LinkedHashMap();
        mappaSorgente.put("beta", "irrilevante");
        mappaSorgente.put(null, "irrilevante2");
        mappaSorgente.put("delta", "irrilevante3");
        ottenutoBooleano = service.isEmpty(mappaSorgente);
        assertFalse(ottenutoBooleano);

        mappaSorgente = new LinkedHashMap();
        mappaSorgente.put("", "irrilevante");
        mappaSorgente.put(null, "irrilevante2");
        mappaSorgente.put("delta", "irrilevante3");
        ottenutoBooleano = service.isEmpty(mappaSorgente);
        assertFalse(ottenutoBooleano);

        mappaSorgente = new LinkedHashMap();
        mappaSorgente.put("beta", "irrilevante");
        mappaSorgente.put("alfa", null);
        mappaSorgente.put("delta", "irrilevante3");
        ottenutoBooleano = service.isEmpty(mappaSorgente);
        assertFalse(ottenutoBooleano);

        mappaSorgente = new LinkedHashMap();
        mappaSorgente.put("beta", "irrilevante");
        mappaSorgente.put("alfa", VUOTA);
        mappaSorgente.put("delta", "irrilevante3");
        ottenutoBooleano = service.isEmpty(mappaSorgente);
        assertFalse(ottenutoBooleano);

        mappaSorgente = new LinkedHashMap();
        mappaSorgente.put("beta", null);
        mappaSorgente.put("alfa", VUOTA);
        ottenutoBooleano = service.isEmpty(mappaSorgente);
        assertFalse(ottenutoBooleano);
    }

    @Test
    @Order(7)
    @DisplayName("7 - isMappaSemplificabile (since Java 11)")
    void isMappaSemplificabile() {
        ottenutoBooleano = service.isMappaSemplificabile(null);
        Assertions.assertFalse(ottenutoBooleano);

        Map<String, List<String>> multiParametersMap = null;
        ottenutoBooleano = service.isMappaSemplificabile(multiParametersMap);
        Assertions.assertFalse(ottenutoBooleano);

        multiParametersMap = new HashMap<>();
        ottenutoBooleano = service.isMappaSemplificabile(multiParametersMap);
        Assertions.assertFalse(ottenutoBooleano);

        multiParametersMap = new HashMap<>();
        multiParametersMap.put("uno", LIST_STRING);
        multiParametersMap.put("due", LIST_SHORT_STRING);
        ottenutoBooleano = service.isMappaSemplificabile(multiParametersMap);
        Assertions.assertFalse(ottenutoBooleano);

        multiParametersMap = new HashMap<>();
        multiParametersMap.put("", LIST_SHORT_STRING);
        multiParametersMap.put("due", LIST_SHORT_STRING);
        ottenutoBooleano = service.isMappaSemplificabile(multiParametersMap);
        assertTrue(ottenutoBooleano);

        multiParametersMap = new HashMap<>();
        multiParametersMap.put("uno", LIST_SHORT_STRING);
        multiParametersMap.put("due", LIST_SHORT_STRING);
        ottenutoBooleano = service.isMappaSemplificabile(multiParametersMap);
        assertTrue(ottenutoBooleano);
    }

    @Test
    @Order(8)
    @DisplayName("8 - semplificaMappa (since Java 11)")
    void semplificaMappa() {
        Map<String, List<String>> multiParametersMap = null;

        mappaOttenuta = service.semplificaMappa(null);
        Assertions.assertNull(mappaOttenuta);

        mappaOttenuta = service.semplificaMappa(multiParametersMap);
        Assertions.assertNull(mappaOttenuta);

        multiParametersMap = new HashMap<>();
        mappaOttenuta = service.semplificaMappa(multiParametersMap);
        Assertions.assertNull(mappaOttenuta);

        multiParametersMap = new HashMap<>();
        multiParametersMap.put("uno", LIST_STRING);
        multiParametersMap.put("due", LIST_SHORT_STRING);
        mappaOttenuta = service.semplificaMappa(multiParametersMap);
        Assertions.assertNull(mappaOttenuta);

        multiParametersMap = new HashMap<>();
        multiParametersMap.put("uno", LIST_SHORT_STRING);
        multiParametersMap.put("due", LIST_SHORT_STRING_DUE);
        mappaPrevista = new HashMap<>();
        mappaPrevista.put("uno", CONTENUTO);
        mappaPrevista.put("due", CONTENUTO_DUE);
        mappaOttenuta = service.semplificaMappa(multiParametersMap);
        Assertions.assertNotNull(mappaOttenuta);
        Assertions.assertEquals(mappaPrevista, mappaOttenuta);
    }


    @Test
    @Order(9)
    @DisplayName("9 - creaArraySingolo (since Java 11)")
    void creaArraySingolo() {
        sorgente = "valore";
        previstoArray = new ArrayList<>();

        ottenutoArray = service.creaArraySingolo(null);
        Assertions.assertNotNull(ottenutoArray);
        Assertions.assertEquals(previstoArray, ottenutoArray);

        previstoArray.add(sorgente);
        ottenutoArray = service.creaArraySingolo(sorgente);
        Assertions.assertNotNull(ottenutoArray);
        Assertions.assertEquals(previstoArray, ottenutoArray);
    }

    @Test
    @Order(10)
    @DisplayName("10 - creaMappaSingola (since Java 11)")
    void creaMappaSingola() {
        sorgente = "chiave";
        sorgente2 = "valore";
        mappaPrevista = new HashMap<>();

        mappaOttenuta = service.creaMappaSingola(null, null);
        Assertions.assertNotNull(mappaOttenuta);
        Assertions.assertEquals(mappaPrevista, mappaOttenuta);

        mappaOttenuta = service.creaMappaSingola(VUOTA, VUOTA);
        Assertions.assertNotNull(mappaOttenuta);
        Assertions.assertEquals(mappaPrevista, mappaOttenuta);

        mappaOttenuta = service.creaMappaSingola(VUOTA, null);
        Assertions.assertNotNull(mappaOttenuta);
        Assertions.assertEquals(mappaPrevista, mappaOttenuta);

        mappaOttenuta = service.creaMappaSingola(null, VUOTA);
        Assertions.assertNotNull(mappaOttenuta);
        Assertions.assertEquals(mappaPrevista, mappaOttenuta);

        mappaOttenuta = service.creaMappaSingola(sorgente, VUOTA);
        Assertions.assertNotNull(mappaOttenuta);
        Assertions.assertEquals(mappaPrevista, mappaOttenuta);

        mappaOttenuta = service.creaMappaSingola(VUOTA, sorgente2);
        Assertions.assertNotNull(mappaOttenuta);
        Assertions.assertEquals(mappaPrevista, mappaOttenuta);

        mappaPrevista.put(sorgente, sorgente2);
        mappaOttenuta = service.creaMappaSingola(sorgente, sorgente2);
        Assertions.assertNotNull(mappaOttenuta);
        Assertions.assertEquals(mappaPrevista, mappaOttenuta);
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