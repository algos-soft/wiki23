package it.algos.unit.enumeration;

import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.enumeration.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: sab, 26-mar-2022
 * Time: 10:50
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("quickly")
@Tag("enums")
@DisplayName("Enumeration AELineHeight")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AELineHeightTest extends AlgosTest {


    private AELineHeight type;

    private List<AELineHeight> listaEnum;

    private List<String> listaTag;

    private AELineHeight[] matrice;


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

        type = null;
        listaEnum = null;
        listaTag = null;
        matrice = null;
    }


    @Test
    @Order(1)
    @DisplayName("1 - matrice dei valori")
    void matrice() {
        matrice = AELineHeight.values();
        assertNotNull(matrice);

        System.out.println("Tutti i valori della enumeration come matrice []");
        System.out.println(VUOTA);
        System.out.println(String.format("Ci sono %d elementi nella Enumeration", matrice.length));
        System.out.println(VUOTA);
        for (AELineHeight valore : matrice) {
            System.out.println(valore);
        }
    }

    @Test
    @Order(2)
    @DisplayName("2 - lista dei valori")
    void lista() {
        listaEnum = AELineHeight.getAllEnums();
        assertNotNull(listaEnum);

        System.out.println("Tutte le occorrenze della enumeration come ArrayList()");
        System.out.println(VUOTA);
        System.out.println(String.format("Ci sono %d elementi nella Enumeration", listaEnum.size()));
        System.out.println(VUOTA);
        listaEnum.forEach(System.out::println);
        System.out.println(VUOTA);
    }

    @Test
    @Order(3)
    @DisplayName("3 - lista come stringa")
    void listaString() {
        ottenutoArray = AELineHeight.getAllStringValues();
        assertNotNull(ottenutoArray);

        System.out.println("Tutte le occorrenze della enumeration sotto forma di stringa");
        System.out.println(VUOTA);
        System.out.println(String.format("Ci sono %d elementi nella Enumeration", ottenutoArray.size()));
        System.out.println(VUOTA);
        ottenutoArray.forEach(System.out::println);
        System.out.println(VUOTA);
    }

    @Test
    @Order(4)
    @DisplayName("4 - lista come tag")
    void listaTag() {
        listaTag = AELineHeight.getAllTags();
        assertNotNull(listaTag);

        System.out.println("Tutti i valori 'tag' della enumeration");
        System.out.println(VUOTA);
        System.out.println(String.format("Ci sono %d elementi nella Enumeration", listaTag.size()));
        System.out.println(VUOTA);
        listaTag.forEach(System.out::println);
        System.out.println(VUOTA);
    }


    @Test
    @Order(5)
    @DisplayName("5 - lista come string -> tag")
    void listaStringTag() {
        listaEnum = AELineHeight.getAllEnums();
        assertNotNull(listaEnum);

        System.out.println(String.format("Tutti i valori 'string -> tag' della enumeration (%s valori)", listaEnum.size()));
        System.out.println(VUOTA);
        listaEnum.forEach(enumTag -> printTag(enumTag));
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