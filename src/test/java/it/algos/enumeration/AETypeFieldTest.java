package it.algos.enumeration;

import it.algos.base.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.enumeration.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

/**
 * Project vaad24
 * Created by Algos
 * User: gac
 * Date: Mon, 12-Dec-2022
 * Time: 22:16
 * Unit test di una enumeration <br>
 * Estende la classe astratta AlgosTest che contiene costanti e metodi base <br>
 * Nella superclasse AlgosTest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse AlgosTest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("quickly")
@Tag("enums")
@DisplayName("Enumeration AETypeField")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AETypeFieldTest extends AlgosUnitTest {


    private AETypeField type;

    private AETypeField[] matrice;

    private List<AETypeField> listaEnum;

    private List<String> listaTag;


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
        matrice = null;
        listaEnum = null;
        listaTag = null;
    }


    @Test
    @Order(1)
    @DisplayName("1 - matrice dei valori")
    void matrice() {
        matrice = AETypeField.values();
        assertNotNull(matrice);

        System.out.println("Tutti i valori della enumeration come matrice []");
        System.out.println(VUOTA);
        System.out.println(String.format("Ci sono %d elementi nella Enumeration", matrice.length));
        System.out.println(VUOTA);
        for (AETypeField valore : matrice) {
            System.out.println(valore);
        }
    }


    @Test
    @Order(2)
    @DisplayName("2 - lista dei valori metodo forEach")
    void forEach() {
        listaEnum = AETypeField.getAllEnums();
        assertNotNull(listaEnum);

        System.out.println("Tutte le occorrenze della enumeration come ArrayList(), metodo forEach");
        System.out.println(VUOTA);
        System.out.println(String.format("Ci sono %d elementi nella Enumeration, recuperati in %s", listaEnum.size(), dateService.deltaTextEsatto(inizio)));
        System.out.println(VUOTA);
        listaEnum.forEach(System.out::println);
        System.out.println(VUOTA);
    }


    @Test
    @Order(3)
    @DisplayName("3 - lista dei valori metodo stream")
    void stream() {
        listaEnum = AETypeField.getAllEnums();
        assertNotNull(listaEnum);

        System.out.println("Tutte le occorrenze della enumeration come ArrayList(), metodo stream");
        System.out.println(VUOTA);
        System.out.println(String.format("Ci sono %d elementi nella Enumeration, recuperati in %s", listaEnum.size(), dateService.deltaTextEsatto(inizio)));
        System.out.println(VUOTA);
        listaEnum.stream().forEach(System.out::println);
        System.out.println(VUOTA);
    }


    @Test
    @Order(4)
    @DisplayName("4 - column e field")
    void tag() {
        listaEnum = AETypeField.getAllEnums();
        assertNotNull(listaEnum);

        System.out.println("column e field di ogni singola enumeration");
        System.out.println(VUOTA);
        System.out.println(String.format("Ci sono %d elementi nella Enumeration, recuperati in %s", listaEnum.size(), dateService.deltaTextEsatto(inizio)));
        System.out.println(VUOTA);
        listaEnum.forEach(e -> {
            System.out.print(e);
            System.out.print(FORWARD);
            System.out.print(e.getWidthColumn());
            System.out.print(SEP);
            System.out.println(e.getWidthField());
        });
        System.out.println(VUOTA);
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