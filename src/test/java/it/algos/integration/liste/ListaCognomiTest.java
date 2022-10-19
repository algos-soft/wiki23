package it.algos.integration.liste;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.liste.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.springframework.boot.test.context.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.test.context.junit.jupiter.*;

import java.util.stream.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Mon, 17-Oct-2022
 * Time: 14:12
 * Unit test di una classe service o backend o query <br>
 * Estende la classe astratta AlgosTest che contiene le regolazioni essenziali <br>
 * Nella superclasse AlgosTest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse AlgosTest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("production")
@Tag("liste")
@DisplayName("Cognomi lista")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ListaCognomiTest extends WikiTest {


    /**
     * Classe principale di riferimento <br>
     */
    private ListaCognomi istanza;

    //--cognome
    protected static Stream<Arguments> COGNOMI_LISTA() {
        return Stream.of(
                Arguments.of(VUOTA),
                Arguments.of("Adam"),
                Arguments.of("Costa"),
                Arguments.of("Thomas"),
                Arguments.of("Brambilla"),
                Arguments.of("Herbert"),
                Arguments.of("Levi")
        );
    }


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
        istanza = new ListaCognomi();
        assertNotNull(istanza);
        System.out.println(("1 - Costruttore base senza parametri"));
        System.out.println(VUOTA);
        System.out.println(String.format("Costruttore base senza parametri per un'istanza di %s", istanza.getClass().getSimpleName()));
    }


    @ParameterizedTest
    @MethodSource(value = "COGNOMI_LISTA")
    @Order(2)
    @DisplayName("2 - Lista bio di vari cognomi")
        //--cognome
    void ListaCognomi(final String cognome) {
        System.out.println("2 - Lista bio di vari cognomi");
        sorgente = cognome;

        listBio = appContext.getBean(ListaCognomi.class,sorgente).listaBio();

        if (listBio != null && listBio.size() > 0) {
            message = String.format("Ci sono %d biografie che implementano il cognome %s", listBio.size(), sorgente);
            System.out.println(message);
            System.out.println(VUOTA);
            printBioLista(listBio);
        }
        else {
            message = "La listBio è nulla";
            System.out.println(message);
        }
    }


    @ParameterizedTest
    @MethodSource(value = "COGNOMI_LISTA")
    @Order(3)
    @DisplayName("3 - Lista wrapLista di vari cognomi")
        //--cognome
    void listaWrap(final String cognome) {
        System.out.println("3 - Lista wrapLista di vari cognomi");
        sorgente = cognome;
        int size;

        listWrapLista = appContext.getBean(ListaCognomi.class,sorgente).listaWrap();

        if (listWrapLista != null && listWrapLista.size() > 0) {
            size = listWrapLista.size();
            message = String.format("Ci sono %d wrapLista che implementano il cognome di %s", listWrapLista.size(), sorgente);
            System.out.println(message);
            System.out.println(VUOTA);
            printWrapLista(listWrapLista);
            printWrapLista(listWrapLista.subList(size - 5, size));
        }
        else {
            message = "La lista è nulla";
            System.out.println(message);
        }
    }



    @ParameterizedTest
    @MethodSource(value = "COGNOMI_LISTA")
    @Order(4)
    @DisplayName("4 - Mappa wrapLista di vari cognomi")
        //--cognome
    void mappaWrapDidascalie(final String cognome) {
        System.out.println("4 - Mappa wrapLista di vari cognomi");
        sorgente = cognome;
        int numVoci;

        mappaWrap = appContext.getBean(ListaCognomi.class,sorgente).mappaWrap();
        if (mappaWrap != null && mappaWrap.size() > 0) {
            numVoci = wikiUtility.getSizeAllWrap(mappaWrap);
            message = String.format("Ci sono %d wrapLista che implementano il cognome %s", numVoci, sorgente);
            System.out.println(message);
            System.out.println(VUOTA);
            printMappaWrap(mappaWrap);
        }
        else {
            message = "La mappa è nulla";
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

}