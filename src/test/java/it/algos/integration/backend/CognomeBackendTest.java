package it.algos.integration.backend;

import it.algos.*;
import it.algos.base.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.packages.cognome.*;
import org.junit.jupiter.api.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import java.util.*;
import java.util.stream.*;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Mon, 17-Oct-2022
 * Time: 08:30
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("backend")
@DisplayName("Cognome Backend")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CognomeBackendTest extends WikiTest {

    /**
     * Classe principale di riferimento <br>
     */
    private CognomeBackend backend;

    @Autowired
    private CognomeRepository repository;


    private Cognome entityBean;


    private List<Cognome> listaBeans;

    //--cognome
    //--voci bio
    protected static Stream<Arguments> LISTE_COGNOMI() {
        return Stream.of(
                Arguments.of(VUOTA, 0),
                Arguments.of("Abe", 47),
                Arguments.of("Elliot", 17),
                Arguments.of("Elliott", 57),
                Arguments.of("Borbone", 29),
                Arguments.of("Da Silva", 9),
                Arguments.of("Di Borbone", 49),
                Arguments.of("Di Savoia", 49),
                Arguments.of("Dos Santos", 49),
                Arguments.of("Diaz", 49),
                Arguments.of("Fernandez", 49),
                Arguments.of("Gomez", 49),
                Arguments.of("Gonzales", 49),
                Arguments.of("Hernandez", 49),
                Arguments.of("Ito", 49),
                Arguments.of("Muller", 49),
                Arguments.of("Sanchez", 49),
                Arguments.of("Muller", 49),
                Arguments.of("da Silva", 49),
                Arguments.of("di Borbone", 49),
                Arguments.of("dos Santos", 49)
        );
    }

    /**
     * Qui passa una volta sola <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();

        backend = cognomeBackend;

        backend.repository = repository;
        backend.crudRepository = repository;
        backend.arrayService = arrayService;
        backend.reflectionService = reflectionService;
    }


    /**
     * Inizializzazione dei service <br>
     * Devono essere tutti 'mockati' prima di iniettare i riferimenti incrociati <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void initMocks() {
        super.initMocks();
    }


    /**
     * Regola tutti riferimenti incrociati <br>
     * Deve essere fatto dopo aver costruito tutte le referenze 'mockate' <br>
     * Nelle sottoclassi devono essere regolati i riferimenti dei service specifici <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixRiferimentiIncrociati() {
        super.fixRiferimentiIncrociati();
    }

    @BeforeEach
    protected void setUpEach() {
        super.setUpEach();

        this.entityBean = null;
        this.listaBeans = null;
    }


    @Test
    @Order(1)
    @DisplayName("1 - count")
    void count() {
        System.out.println("1 - count");
        String message;

        ottenutoIntero = backend.count();
        assertTrue(ottenutoIntero > 0);
        message = String.format("Ci sono in totale %s entities nel database mongoDB", textService.format(ottenutoIntero));
        System.out.println(message);
    }


    @Test
    @Order(2)
    @DisplayName("2 - findAll")
    void findAll() {
        System.out.println("2 - findAll");
        String message;
        int totBio = bioBackend.count();
        message = String.format("Ci sono %s biografie", textService.format(totBio));
        System.out.println(VUOTA);
        System.out.println(message);

        listaStr = bioBackend.findAllCognomiDistinti();
        message = String.format("Nelle %s biografie ci sono %s cognomi distinti ordinati alfabeticamente", textService.format(totBio), textService.format(listaStr.size()));
        System.out.println(VUOTA);
        System.out.println(message);

        ottenutoIntero = backend.count();
        assertTrue(ottenutoIntero > 0);
        message = String.format("Ci sono in totale %s entities nel database mongoDB", ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(message);

        listaBeans = backend.findAllStampabileSortNumBio();
        assertNotNull(listaBeans);
        message = String.format("Nel database mongoDB ci sono %s cognomi superiori o uguali a %s voci (vanno sul server) - metodo senza parametro", textService.format(listaBeans.size()), WPref.sogliaCognomiWiki.getInt());
        System.out.println(VUOTA);
        System.out.println(message);

        sorgenteIntero = WPref.sogliaCognomiWiki.getInt();
        listaBeans = backend.findAllStampabileSortNumBio(sorgenteIntero);
        assertNotNull(listaBeans);
        message = String.format("Nel database mongoDB ci sono %s cognomi superiori o uguali a %s voci (vanno sul server) - valore della preferenza", textService.format(listaBeans.size()), WPref.sogliaCognomiWiki.getInt());
        System.out.println(VUOTA);
        System.out.println(message);

        sorgenteIntero = 40;
        listaBeans = backend.findAllStampabileSortNumBio(sorgenteIntero);
        assertNotNull(listaBeans);
        message = String.format("Nel database mongoDB ci sono %s cognomi superiori o uguali a %s voci - valore ad hoc", textService.format(listaBeans.size()), sorgenteIntero);
        System.out.println(VUOTA);
        System.out.println(message);

        sorgenteIntero = WPref.sogliaCognomiMongo.getInt();
        listaBeans = backend.findAllStampabileSortNumBio(sorgenteIntero);
        assertNotNull(listaBeans);
        message = String.format("Nel database mongoDB ci sono %s cognomi superiori o uguali a %s voci - valore minimo (dovrebbe essere uguale a %s)", textService.format(listaBeans.size()), sorgenteIntero, backend.count());
        System.out.println(VUOTA);
        System.out.println(message);

        listaBeans = backend.findAllEccessiviMongo();
        assertNotNull(listaBeans);
        message = String.format("%s cognomi minori di %s biografie che non dovrebbero esserci", textService.format(listaBeans.size()), WPref.sogliaCognomiMongo.getInt());
        System.out.println(VUOTA);
        System.out.println(message);
        System.out.println(VUOTA);
        printNumBio(listaBeans);
    }

    @Test
    @Order(3)
    @DisplayName("3 - findAllSortNumBio")
    void findAllSortNumBio() {
        System.out.println("3 - findAllSortNumBio");
        String message;
        int totBio = bioBackend.count();

        listaBeans = backend.findAllSortNumBio();
        assertNotNull(listaBeans);
        message = String.format("Nelle %s biografie ci sono %s cognomi distinti ordinati per numBio", textService.format(totBio), textService.format(listaBeans.size()));
        System.out.println(message);
        printNumBio(listaBeans);
    }


    @Test
    @Order(4)
    @DisplayName("4 - findCognomi")
    void findCognomi() {
        System.out.println("4 - findCognomi");
        String message;
        int totBio = bioBackend.count();

        listaStr = backend.findCognomi();
        assertNotNull(listaStr);
        message = String.format("Nelle %s biografie ci sono %s cognomi.cognomi distinti ordinati alfabeticamente", textService.format(totBio), textService.format(listaStr.size()));
        System.out.println(message);
        System.out.println(VUOTA);
        print(listaStr);
    }

    @Test
    @Order(5)
    @DisplayName("5 - findCognomiSortNumBio")
    void findCognomiSortNumBio() {
        System.out.println("5 - findCognomiSortNumBio");
        String message;
        int totBio = bioBackend.count();

        listaStr = backend.findCognomiSortNumBio();
        assertNotNull(listaStr);
        message = String.format("Nelle %s biografie ci sono %s cognomi.cognomi distinti ordinati per numBio", textService.format(totBio), textService.format(listaStr.size()));
        System.out.println(message);
        System.out.println(VUOTA);
        print(listaStr);
    }

    @Test
    @Order(6)
    @DisplayName("6 - findAllStampabile")
    void findAllStampabile() {
        System.out.println("6 - findAllStampabile");
        String message;

        listaBeans = backend.findAllStampabili();
        assertNotNull(listaBeans);
        message = String.format("Ci sono %s cognomi validi ordinati alfabeticamente (mostro solo i primi 10)", textService.format(listaBeans.size()));
        System.out.println(VUOTA);
        System.out.println(message);
        printNumBio(listaBeans.subList(0, 10));

        listaStr = backend.findCognomiStampabili();
        assertNotNull(listaStr);
        message = String.format("Ci sono %s cognomi.cognome validi ordinati alfabeticamente (mostro solo i primi 10)", textService.format(listaStr.size()));
        System.out.println(VUOTA);
        System.out.println(message);
        print(listaStr.subList(0, 10));
    }


    @Test
    @Order(7)
    @DisplayName("7 - findAllEccessiviServer")
    void findAllEccessiviServer() {
        System.out.println("7 - findAllEccessiviServer");
        String message;

        listaBeans = backend.findAllEccessiviServer();
        assertNotNull(listaBeans);
        message = String.format("Ci sono %s cognomi da cancellare dal server (se ci sono)", textService.format(listaBeans.size()));
        System.out.println(message);
        System.out.println(VUOTA);
        printNumBio(listaBeans);
    }


    @ParameterizedTest
    @MethodSource(value = "LISTE_COGNOMI")
    @Order(20)
    @DisplayName("20 - countCognome")
        //--cognome
        //--voci bio
    void countCognome(String cognome, int numVoci) {
        System.out.println("20 - countCognome");
        String message;
        sorgente = cognome;
        previstoIntero = numVoci;

        ottenutoIntero = bioBackend.countCognome(sorgente);
        if (ottenutoIntero == previstoIntero) {
            message = String.format("Nel cognome %s ci sono le %s biografie previste", sorgente, previstoIntero);
        }
        else {
            message = String.format("Nel cognome %s NON ci sono le %s biografie previste, ma ce ne sono %s", sorgente, previstoIntero, ottenutoIntero);
        }
        System.out.println(VUOTA);
        System.out.println(message);
        assertEquals(previstoIntero, ottenutoIntero);
    }


    @ParameterizedTest
    @MethodSource(value = "LISTE_COGNOMI")
    @Order(21)
    @DisplayName("21 - findCognome")
        //--cognome
        //--voci bio
    void findCognome(String cognome, int numVoci) {
        System.out.println("21 - findCognome");
        List<Bio> listaBio;
        String message;
        sorgente = cognome;
        previstoIntero = numVoci;

        listaBio = bioBackend.findCognome(sorgente);
        assertNotNull(listaBio);
        ottenutoIntero = listaBio.size();
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di cognome %s", ottenutoIntero, sorgente));
        printBio(listaBio);
    }


    //    @Test
    @Order(31)
    @DisplayName("31 - elabora")
    void elabora() {
        System.out.println("31 - elabora");
        String message;
        int totCognomi = backend.count();

        long inizio = System.currentTimeMillis();
        backend.elabora();
        message = String.format("Elaborate le numBio per ognuno dei %s cognomi in %s", textService.format(totCognomi), dateService.deltaText(inizio));
        System.out.println(message);
        System.out.println(VUOTA);
        print(listaStr);
    }


    protected void printNumBio(List<Cognome> lista) {
        String message;
        System.out.println(VUOTA);
        for (Cognome cognome : lista) {
            message = String.format("%s%s%s", cognome.cognome, SEP, cognome.numBio);
            System.out.println(message);
        }
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

    void printBeans(List<Cognome> listaBeans) {
        System.out.println(VUOTA);
        int k = 0;

        for (Cognome bean : listaBeans) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.println(bean);
        }
    }

}
