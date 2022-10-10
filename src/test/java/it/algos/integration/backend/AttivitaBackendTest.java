package it.algos.integration.backend;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.packages.attivita.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.data.domain.*;

import java.util.*;
import java.util.stream.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: mar, 03-mag-2022
 * Time: 10:10
 * <p>
 * Unit test di una classe di servizio (di norma) <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("production")
@Tag("backend")
@DisplayName("Attivita backend")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AttivitaBackendTest extends WikiTest {


    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    @InjectMocks
    private AttivitaBackend backend;

    @Autowired
    private AttivitaRepository repository;

    protected List<Attivita> listaBeans;


    private Attivita entityBean;





    /**
     * Execute only once before running all tests <br>
     * Esegue una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpAll() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();

        backend.repository = repository;
        backend.crudRepository = repository;
        backend.arrayService = arrayService;
        backend.reflectionService = reflectionService;
    }


    /**
     * Qui passa prima di ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUpEach() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    protected void setUpEach() {
        super.setUpEach();

        listaBeans = null;
        listaStr = null;
        mappa = null;
        entityBean = null;
    }


    @Test
    @Order(1)
    @DisplayName("1 - count")
    void count() {
        System.out.println("1 - count");
        String message;

        ottenutoIntero = backend.count();
        assertTrue(ottenutoIntero > 0);
        message = String.format("Ci sono in totale %s attività", textService.format(ottenutoIntero));
        System.out.println(message);
    }

    @Test
    @Order(2)
    @DisplayName("2 - findAll")
    void findAll() {
        System.out.println("2 - findAll");
        String message;
        int num = 10;

        listaBeans = backend.findAll();
        assertNotNull(listaBeans);
        message = String.format("Ci sono in totale %s attività", textService.format(listaBeans.size()));
        System.out.println(message);
        System.out.println(VUOTA);
        message = String.format("Le prime %s attività", num);
        System.out.println(message);
        printSingolari(listaBeans.subList(0, num));
    }

    @Test
    @Order(3)
    @DisplayName("3 - findAll sort singolari")
    void findAllSortSingolari() {
        System.out.println("3 - findAll sort singolari");
        int num = 10;
        Sort sort;

        sort = Sort.by(Sort.Direction.ASC, SINGOLARE);
        listaBeans = backend.findAll(sort);
        assertNotNull(listaBeans);
        message = String.format("Ci sono in totale %s attività", textService.format(listaBeans.size()));
        System.out.println(message);

        System.out.println(VUOTA);
        System.out.println(String.format("Le prime %s attività ordinate per '%s' %s", num, Sort.Direction.ASC, SINGOLARE));
        printSingolari(listaBeans.subList(0, num));

        System.out.println(VUOTA);
        sort = Sort.by(Sort.Direction.DESC, SINGOLARE);
        listaBeans = backend.findAll(sort);
        assertNotNull(listaBeans);
        System.out.println(String.format("Le prime %s attività ordinate per '%s' %s", num, Sort.Direction.DESC, SINGOLARE));
        printSingolari(listaBeans.subList(0, num));
    }


    @Test
    @Order(4)
    @DisplayName("4 - findAll (attività) plurali")
    void findAttivitaDistinctByPluraliSortPlurali() {
        System.out.println("4 - findAll (attività) plurali");
        int num = 10;

        listaBeans = backend.findAttivitaDistinctByPluraliSortPlurali();
        assertNotNull(listaBeans);
        message = String.format("Ci sono in totale %s attività distinte", textService.format(listaBeans.size()));
        System.out.println(message);

        System.out.println(VUOTA);
        System.out.println(String.format("Le prime %s attività distinte ordinate per '%s' %s", num, Sort.Direction.ASC, PLURALE));
        printPlurali(listaBeans.subList(0, num));
    }


    @Test
    @Order(5)
    @DisplayName("5 - findAll (stringa) plurali")
    void allPlurali() {
        System.out.println("5 - findAll (stringa) plurali");
        int num = 10;

        listaStr = backend.findAllPlurali();
        assertNotNull(listaStr);
        System.out.println(VUOTA);
        System.out.println(String.format("I primi %s '%s'' distinti ordinati per '%s' %s", num, PLURALE, Sort.Direction.ASC, PLURALE));
        print(listaStr.subList(0, num));
    }

    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_SINGOLARE")
    @Order(6)
    @DisplayName("6 - isExistSingolare")
        //--nome singolare
        //--esiste
    void isExistSingolare(final String singolare, final boolean esiste) {
        System.out.println("6 - isExist");
        ottenutoBooleano = backend.isExistSingolare(singolare);
        assertEquals(esiste, ottenutoBooleano);
        if (ottenutoBooleano) {
            System.out.println(String.format("L'attività singolare '%s' esiste", singolare));
        }
        else {
            System.out.println(String.format("L'attività singolare '%s' non esiste", singolare));
        }
    }

    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_PLURALI")
    @Order(7)
    @DisplayName("7 - isExistPlurale")
        //--nome plurale
        //--esiste
    void isExistPlurale(final String plurale, final boolean esiste) {
        System.out.println("7 - isExistPlurale");
        ottenutoBooleano = backend.isExistPlurale(plurale);
        assertEquals(esiste, ottenutoBooleano);
        if (ottenutoBooleano) {
            System.out.println(String.format("L'attività plurale '%s' esiste", plurale));
        }
        else {
            System.out.println(String.format("L'attività plurale '%s' non esiste", plurale));
        }
    }

    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_TRUE")
    @Order(8)
    @DisplayName("8 - isExist")
        //--nome singolarePlurale
        //--esiste
    void isExist(final String singolarePlurale, final boolean esiste) {
        System.out.println("8 - isExist");
        ottenutoBooleano = backend.isExist(singolarePlurale);
        assertEquals(esiste, ottenutoBooleano);
        if (ottenutoBooleano) {
            System.out.println(String.format("L'attività singolare/plurale'%s' esiste", singolarePlurale));
        }
        else {
            System.out.println(String.format("L'attività singolare/plurale '%s' non esiste", singolarePlurale));
        }
    }


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_SINGOLARE")
    @Order(9)
    @DisplayName("9 - findFirstBySingolare")
        //--nome singolare
        //--esiste
    void findFirstBySingolare(String singolare, boolean esiste) {
        System.out.println("9 - findBySingolare");
        entityBean = backend.findFirstBySingolare(singolare);
        assertEquals(entityBean != null, esiste);
        if (esiste) {
            System.out.println(String.format("L'attività singolare '%s' esiste", singolare));
        }
        else {
            System.out.println(String.format("L'attività singolare '%s' non esiste", singolare));
        }
    }

    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_PLURALI")
    @Order(10)
    @DisplayName("10 - findFirstByPluraleLista")
        //--nome plurale
        //--esiste
    void findFirstByPluraleLista(String plurale, boolean esiste) {
        System.out.println("10 - findFirstByPluraleLista");
        entityBean = backend.findFirstByPluraleLista(plurale);
        assertEquals(entityBean != null, esiste);
        if (esiste) {
            System.out.println(String.format("L'attività plurale '%s' esiste", plurale));
        }
        else {
            System.out.println(String.format("L'attività plurale '%s' non esiste", plurale));
        }
    }


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_TRUE")
    @Order(11)
    @DisplayName("11 - findFirst")
        //--nome singolarePlurale
        //--esiste
    void findFirst(final String singolarePlurale, final boolean esiste) {
        System.out.println("11 - findFirst");
        entityBean = backend.findFirst(singolarePlurale);
        assertEquals(entityBean != null, esiste);
        if (esiste) {
            System.out.println(String.format("L'attività singolare/plurale '%s' esiste", singolarePlurale));
        }
        else {
            System.out.println(String.format("L'attività singolare/plurale '%s' non esiste", singolarePlurale));
        }
    }


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_SINGOLARE")
    @Order(12)
    @DisplayName("12 - findAllBySingolare e trova tutte le 'attività singolari'")
        //--nome singolare
        //--esiste
    void findAllBySingolare(String singolare, boolean esiste) {
        System.out.println("12 - findAllBySingolare e trova tutte le 'attività singolari'");
        entityBean = backend.findFirstBySingolare(singolare);
        assertEquals(entityBean != null, esiste);

        if (esiste) {
            System.out.println(String.format("L'attività singolare '%s' esiste", singolare));
            listaBeans = backend.findAllBySingolare(singolare);
            assertNotNull(listaBeans);
            assertEquals(listaBeans.size() > 0, esiste);
            printSingolariAttivita(singolare, listaBeans);
        }
        else {
            System.out.println(String.format("L'attività singolare '%s' non esiste", singolare));
        }
    }

    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_PLURALI")
    @Order(13)
    @DisplayName("13 - findAllByPlurale e trova tutte le 'attività singolari'")
        //--nome plurale
        //--esiste
    void findAllByPlurale(String plurale, boolean esiste) {
        System.out.println("13 - findAllByPlurale e trova tutte le 'attività singolari'");
        entityBean = backend.findFirstByPluraleLista(plurale);
        assertEquals(entityBean != null, esiste);

        if (esiste) {
            System.out.println(String.format("L'attività plurale '%s' esiste", plurale));
            listaBeans = backend.findAllByPlurale(plurale);
            assertNotNull(listaBeans);
            assertEquals(listaBeans.size() > 0, esiste);
            printSingolariAttivita(plurale, listaBeans);
        }
        else {
            System.out.println(String.format("L'attività plurale '%s' non esiste", plurale));
        }
    }


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_TRUE")
    @Order(14)
    @DisplayName("14 - findAllBySingolarePlurale e trova tutte le 'attività singolari'")
        //--nome singolarePlurale
        //--esiste
    void findAllBySingolarePlurale(String singolarePlurale, boolean esiste) {
        System.out.println("14 - findAllBySingolarePlurale e trova tutte le 'attività singolari'");
        entityBean = backend.findFirst(singolarePlurale);
        assertEquals(entityBean != null, esiste);

        if (esiste) {
            System.out.println(String.format("L'attività singolarePlurale '%s' esiste", singolarePlurale));
            listaBeans = backend.findAllBySingolarePlurale(singolarePlurale);
            assertNotNull(listaBeans);
            assertEquals(listaBeans.size() > 0, esiste);
            printSingolariAttivita(singolarePlurale, listaBeans);
        }
        else {
            System.out.println(String.format("L'attività singolarePlurale '%s' non esiste", singolarePlurale));
        }
    }

    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_SINGOLARE")
    @Order(15)
    @DisplayName("15 - findAllSingolariBySingolare e trova tutti i 'nomi singolari'")
        //--nome singolare
        //--esiste
    void findAllSingolariBySingolare(String singolare, boolean esiste) {
        System.out.println("15 - findAllSingolariBySingolare e trova tutti i 'nomi singolari'");
        entityBean = backend.findFirstBySingolare(singolare);
        assertEquals(entityBean != null, esiste);

        if (esiste) {
            System.out.println(String.format("L'attività singolare '%s' esiste", singolare));
            listaStr = backend.findAllSingolariBySingolare(singolare);
            assertNotNull(listaStr);
            assertEquals(listaStr.size() > 0, esiste);
            System.out.println(String.format("Ci sono %d attività singolari collegate a %s", listaStr.size(), singolare));
            System.out.println(VUOTA);
            for (String nome : listaStr) {
                System.out.println(nome);
            }
        }
        else {
            System.out.println(String.format("L'attività singolare '%s' non esiste", singolare));
        }
    }

    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_PLURALI")
    @Order(16)
    @DisplayName("16 - findAllSingolariByPlurale e trova tutti i 'nomi singolari'")
        //--nome plurale
        //--esiste
    void findAllSingolariByPlurale(String plurale, boolean esiste) {
        System.out.println("16 - findAllSingolariByPlurale e trova tutti i 'nomi singolari'");
        entityBean = backend.findFirstByPluraleLista(plurale);
        assertEquals(entityBean != null, esiste);

        if (esiste) {
            System.out.println(String.format("L'attività plurale '%s' esiste", plurale));
            listaStr = backend.findAllSingolariByPlurale(plurale);
            assertNotNull(listaStr);
            assertEquals(listaStr.size() > 0, esiste);
            System.out.println(String.format("Ci sono %d attività singolari collegate a %s", listaStr.size(), plurale));
            System.out.println(VUOTA);
            for (String nome : listaStr) {
                System.out.println(nome);
            }
        }
        else {
            System.out.println(String.format("L'attività plurale '%s' non esiste", plurale));
        }
    }


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_TRUE")
    @Order(17)
    @DisplayName("17 - findAllSingolari e trova tutti i 'nomi singolari'")
        //--nome singolarePlurale
        //--esiste
    void findAllSingolari(String singolarePlurale, boolean esiste) {
        System.out.println("17 - findAllSingolari e trova tutti i 'nomi singolari'");
        entityBean = backend.findFirst(singolarePlurale);
        assertEquals(entityBean != null, esiste);

        if (esiste) {
            System.out.println(String.format("L'attività plurale '%s' esiste", singolarePlurale));
            listaStr = backend.findAllSingolari(singolarePlurale);
            assertNotNull(listaStr);
            assertEquals(listaStr.size() > 0, esiste);
            System.out.println(String.format("Ci sono %d attività singolarePlurale collegate a %s", listaStr.size(), singolarePlurale));
            System.out.println(VUOTA);
            for (String nome : listaStr) {
                System.out.println(nome);
            }
        }
        else {
            System.out.println(String.format("L'attività singolarePlurale '%s' non esiste", singolarePlurale));
        }
    }


    @Test
    @Order(41)
    @DisplayName("41 - findMappaSingolariByPluraleLista")
    void findMappaSingolariByPluraleLista() {
        System.out.println("41 - findMappaSingolariByPluraleLista");
        mappa = backend.findMappaSingolariByPluraleLista();
        assertNotNull(mappa);
        printMappa(mappa, "di attività distinte per 'pluraleLista' con le relative attività singolari");
    }

    @Test
    @Order(42)
    @DisplayName("42 - findMappaSingolariByPluraleParagrafo")
    void findMappaSingolariByPluraleParagrafo() {
        System.out.println("42 - findMappaSingolariByPluraleParagrafo");
        mappa = backend.findMappaSingolariByPluraleParagrafo();
        assertNotNull(mappa);
        printMappa(mappa, "di attività distinte per 'pluraleParagrafo' con le relative attività singolari");
    }


    @Test
    @Order(43)
    @DisplayName("43 - findMappaSingolariByLinkPagina")
    void findMappaSingolariByLinkPagina() {
        System.out.println("43 - findMappaSingolariByLinkPagina");
        mappa = backend.findMappaSingolariByLinkPagina();
        assertNotNull(mappa);
        printMappa(mappa, "di attività distinte per 'linkPagina' con le relative attività singolari");
    }

    @Test
    @Order(61)
    @DisplayName("61 - attivitaNazionalita")
    void attivitaNazionalita() {
        System.out.println("61 - attivitaNazionalita");

        sorgente = "altista";
        sorgente2 = "australiano";
        ottenutoIntero = bioBackend.countAttivitaNazionalita(sorgente, sorgente2);
        System.out.println(VUOTA);
        System.out.println(String.format("L'attività '%s' contiene %d voci biografiche di %s", sorgente, ottenutoIntero, sorgente2));

        listBio = bioBackend.findAllAttivitaNazionalita(sorgente, sorgente2);
        System.out.println(VUOTA);
        printBio(listBio);

        System.out.println(VUOTA);

        sorgente = "altista";
        sorgente2 = "australiana";
        ottenutoIntero = bioBackend.countAttivitaNazionalita(sorgente, sorgente2);
        System.out.println(VUOTA);
        System.out.println(String.format("L'attività '%s' contiene %d voci biografiche di %s", sorgente, ottenutoIntero, sorgente2));

        listBio = bioBackend.findAllAttivitaNazionalita(sorgente, sorgente2);
        System.out.println(VUOTA);
        printBio(listBio);

        System.out.println(VUOTA);

        sorgente = "ex altista";
        sorgente2 = "australiano";
        ottenutoIntero = bioBackend.countAttivitaNazionalita(sorgente, sorgente2);
        System.out.println(VUOTA);
        System.out.println(String.format("L'attività '%s' contiene %d voci biografiche di %s", sorgente, ottenutoIntero, sorgente2));

        listBio = bioBackend.findAllAttivitaNazionalita(sorgente, sorgente2);
        System.out.println(VUOTA);
        printBio(listBio);

        System.out.println(VUOTA);

        sorgente = "ex altista";
        sorgente2 = "australiana";
        ottenutoIntero = bioBackend.countAttivitaNazionalita(sorgente, sorgente2);
        System.out.println(VUOTA);
        System.out.println(String.format("L'attività '%s' contiene %d voci biografiche di %s", sorgente, ottenutoIntero, sorgente2));

        listBio = bioBackend.findAllAttivitaNazionalita(sorgente, sorgente2);
        System.out.println(VUOTA);
        printBio(listBio);
    }


    @Test
    @Order(63)
    @DisplayName("63 - attivitaNazionalitaAll")
    void attivitaNazionalitaAll() {
        System.out.println("53 - attivitaNazionalitaAll");
        int somma = 0;
        String attivitaPlurale;
        String nazionalitaPlurale;

        sorgente = "altista";
        attivitaPlurale = backend.findFirstBySingolare(sorgente).pluraleLista;
        listaStr = backend.findAllSingolariBySingolare(sorgente);
        System.out.println(VUOTA);
        System.out.println(String.format("L'attività %s contiene %d attività singolari:", sorgente, listaStr.size()));
        System.out.println(listaStr);
        System.out.println(String.format("L'attività %s appartiene all'attività plurale: [%s]", sorgente, attivitaPlurale));

        sorgente = "australiano";
        nazionalitaPlurale = nazionalitaBackend.findFirstBySingolare(sorgente).pluraleLista;
        listaStr = nazionalitaBackend.findAllSingolariBySingolare(sorgente);
        System.out.println(VUOTA);
        System.out.println(String.format("La nazionalità %s contiene %d nazionalità singolari:", sorgente, listaStr.size()));
        System.out.println(listaStr);
        System.out.println(String.format("La nazionalità %s appartiene alla nazionalità plurale: [%s]", sorgente, nazionalitaPlurale));

        sorgente = "altista";
        sorgente2 = "australiano";
        previstoIntero = 9;
        ottenutoIntero = bioBackend.countAttivitaNazionalitaAll(sorgente, sorgente2);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Ci sono %d voci di %s %s", ottenutoIntero, sorgente, sorgente2));

        sorgente = "altista";
        sorgente2 = "australiano";
        listBio = bioBackend.findAllAttivitaNazionalita(sorgente, sorgente2);
        System.out.println(VUOTA);
        printBio(listBio);

        sorgente = attivitaPlurale;
        sorgente2 = nazionalitaPlurale;
        previstoIntero = 9;
        ottenutoIntero = bioBackend.countAttivitaNazionalitaAll(sorgente, sorgente2);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Ci sono %d voci di %s %s", ottenutoIntero, sorgente, sorgente2));
    }


    @Test
    @Order(64)
    @DisplayName("64 - findAllAttivitaNazionalita con sottoSottoPagina")
    void findAllAttivitaNazionalita() {
        System.out.println("64 - findAllAttivitaNazionalita con sottoSottoPagina");
        int somma = 0;

        sorgente = "nobili";
        sorgente2 = "Tedeschi";
        sorgente3 = "C";
        previstoIntero = 61;
        //        ottenutoIntero = bioBackend.findAllAttivitaNazionalita(sorgente, sorgente2, sorgente3);
        //        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Ci sono %d voci di %s %s %s", ottenutoIntero, sorgente, sorgente2, sorgente3));

    }


    @Test
    @Order(65)
    @DisplayName("65 - countAttivitaNazionalitaAll")
    void countAttivitaNazionalitaAll() {
        System.out.println("65 - countAttivitaNazionalitaAll");
        sorgente = "nobili";
        sorgente2 = "Altre...";
        sorgente3 = "G";
        previstoIntero = 53;
        ottenutoIntero = bioBackend.countAttivitaNazionalitaAll(sorgente, sorgente2, sorgente3);
        //        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Ci sono %d voci di %s %s %s", ottenutoIntero, sorgente, sorgente2, sorgente3));
    }


    @Test
    @Order(66)
    @DisplayName("66 - pluraleBySingolarePlurale")
    void pluraleBySingolarePlurale() {
        System.out.println("66 - pluraleBySingolarePlurale");
        previsto = "abati e badesse";

        sorgente = "abati e badesse";
        ottenuto = backend.pluraleBySingolarePlurale(sorgente);
        assertEquals(previsto, ottenuto);

        sorgente = "abate";
        ottenuto = backend.pluraleBySingolarePlurale(sorgente);
        assertEquals(previsto, ottenuto);

        sorgente = "badessa";
        ottenuto = backend.pluraleBySingolarePlurale(sorgente);
        assertEquals(previsto, ottenuto);
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

    void printSingolariAttivita(String plurale, List<Attivita> listaAttivita) {
        System.out.println(String.format("Ci sono %d attività singolari per %s", listaAttivita.size(), plurale));
        System.out.println(VUOTA);

        for (Attivita attivita : listaAttivita) {
            System.out.println(attivita.singolare);
        }
    }


    void printPlurali(List<Attivita> listaAttivita) {
        int k = 0;

        for (Attivita attivita : listaAttivita) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.println(attivita.pluraleLista);
        }
    }

    void printSingolari(List<Attivita> listaAttivita) {
        int k = 0;

        for (Attivita attivita : listaAttivita) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.println(attivita.singolare);
        }
    }

}