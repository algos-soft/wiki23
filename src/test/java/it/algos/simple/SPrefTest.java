package it.algos.simple;

import it.algos.*;
import it.algos.base.*;
import it.algos.simple.backend.enumeration.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.interfaces.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.*;

import java.util.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: sab, 07-mag-2022
 * Time: 17:35
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@SpringBootTest(classes = {SimpleApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@DisplayName("Enumeration SPref")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SPrefTest extends AlgosTest {


    private SPref type;

    private List<SPref> listaEnum;

    private List<String> listaTag;

    private SPref[] matrice;


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
        matrice = SPref.values();
        assertNotNull(matrice);

        System.out.println("Tutti i valori della enumeration come matrice []");
        System.out.println(VUOTA);
        System.out.println(String.format("Ci sono %d elementi nella Enumeration", matrice.length));
        System.out.println(VUOTA);
        for (SPref valore : matrice) {
            System.out.println(valore);
        }
    }

    @Test
    @Order(2)
    @DisplayName("2 - lista dei valori")
    void lista() {
        listaEnum = SPref.getAllEnums();
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
    @DisplayName("3 - lista come string")
    void listaStringTag() {
        listaEnum = SPref.getAllEnums();
        assertNotNull(listaEnum);

        System.out.println(String.format("KeyCode della enumeration (%s valori)", listaEnum.size()));
        System.out.println(VUOTA);
        listaEnum.forEach(enumPref -> System.out.println(enumPref.toString()));
    }


    @Test
    @Order(4)
    @DisplayName("4 - descrizione preferenza")
    void descrizione() {
        listaEnum = SPref.getAllEnums();
        assertNotNull(listaEnum);

        System.out.println(String.format("KeyCode e descrizione della enumeration (%s valori)", listaEnum.size()));
        System.out.println(VUOTA);
        listaEnum.forEach(pref -> System.out.println(String.format("%s%s%s", pref.getKeyCode(), FORWARD, pref.getDescrizione())));
    }

    @Test
    @Order(5)
    @DisplayName("5 - prova tipo stringa")
    void stringa() {
        System.out.println("5 - prova tipo stringa");
        String oldValue = VUOTA;
        String newValue = "pippo";
        String currentValue;

        oldValue = SPref.string.getStr();
        assertNotNull(oldValue);
        System.out.println(VUOTA);
        System.out.println(String.format("Valore iniziale %s%s", FORWARD, oldValue));

        SPref.string.setValue(newValue);
        currentValue = SPref.string.getStr();
        System.out.println(VUOTA);
        System.out.println(String.format("Valore temporaneo %s%s", FORWARD, currentValue));

        SPref.string.setValue(oldValue);
        currentValue = SPref.string.getStr();
        System.out.println(VUOTA);
        System.out.println(String.format("Valore originale %s%s", FORWARD, currentValue));
    }


    @Test
    @Order(6)
    @DisplayName("6 - prova tipo boolean")
    void booleano() {
        System.out.println("6 - prova tipo boolean");
        boolean oldValue;
        boolean newValue = true;
        boolean currentValue;

        oldValue = SPref.bool.is();
        assertNotNull(oldValue);
        System.out.println(VUOTA);
        System.out.println(String.format("Valore iniziale %s%s", FORWARD, oldValue));

        SPref.bool.setValue(newValue);
        currentValue = SPref.bool.is();
        System.out.println(VUOTA);
        System.out.println(String.format("Valore temporaneo %s%s", FORWARD, currentValue));

        SPref.bool.setValue(oldValue);
        currentValue = SPref.bool.is();
        System.out.println(VUOTA);
        System.out.println(String.format("Valore originale %s%s", FORWARD, currentValue));
    }


    @Test
    @Order(7)
    @DisplayName("7 - prova tipo intero")
    void intero() {
        System.out.println("7 - prova tipo intero");
        int oldValue;
        int newValue = 17;
        int currentValue;

        oldValue = SPref.integer.getInt();
        assertNotNull(oldValue);
        System.out.println(VUOTA);
        System.out.println(String.format("Valore iniziale %s%s", FORWARD, oldValue));

        SPref.integer.setValue(newValue);
        currentValue = SPref.integer.getInt();
        System.out.println(VUOTA);
        System.out.println(String.format("Valore temporaneo %s%s", FORWARD, currentValue));

        SPref.integer.setValue(oldValue);
        currentValue = SPref.integer.getInt();
        System.out.println(VUOTA);
        System.out.println(String.format("Valore originale %s%s", FORWARD, currentValue));
    }


    @Test
    @Order(8)
    @DisplayName("8 - prova tipo lungo")
    void lungo() {
        System.out.println("8 - prova tipo lungo");
    }

    @Test
    @Order(9)
    @DisplayName("9 - prova tipo localDateTime")
    void localDateTime() {
        System.out.println("9 - prova tipo localDateTime");
    }

    @Test
    @Order(10)
    @DisplayName("10 - prova tipo localDate")
    void localDate() {
        System.out.println("10 - prova tipo localDate");
    }

    @Test
    @Order(11)
    @DisplayName("11 - prova tipo localTime")
    void localTime() {
        System.out.println("11 - prova tipo localTime");
    }

    @Test
    @Order(12)
    @DisplayName("12 - prova tipo email")
    void email() {
        System.out.println("12 - prova tipo email");
    }

    @Test
    @Order(13)
    @DisplayName("13 - prova tipo enumerationType")
    void enumerationType() {
        System.out.println("13 - prova tipo enumerationType");
        AITypePref enumValue = null;
        AITypePref oldValue = null;
        AITypePref newValue = AELogLevel.warn;
        AITypePref currentValue;

        enumValue = (AITypePref) SPref.enumerationType.getDefaultValue();
        assertNotNull(enumValue);
        System.out.println(VUOTA);
        System.out.println(String.format("Valore di default di typeEnum della enumeration %s%s", FORWARD, enumValue));

        oldValue = SPref.enumerationType.getEnumCurrentObj();
        assertNotNull(oldValue);
        System.out.println(VUOTA);
        System.out.println(String.format("Vecchio valore typeEnum della preferenza %s%s", FORWARD, oldValue));

        SPref.enumerationType.setEnumCurrentObj(newValue);
        System.out.println(VUOTA);
        System.out.println(String.format("Modificata la preferenza col nuovo typeEnum %s%s", FORWARD, newValue));

        enumValue = SPref.enumerationType.getEnumCurrentObj();
        assertNotNull(enumValue);
        System.out.println(VUOTA);
        System.out.println(String.format("Valore corrente modificato di typeEnum per la preferenza %s%s", FORWARD, enumValue));

        SPref.enumerationType.setEnumCurrentObj(oldValue);
        System.out.println(VUOTA);
        System.out.println(String.format("Modificata la preferenza col vecchio typeEnum originario %s%s", FORWARD, oldValue));

        enumValue = SPref.enumerationType.getEnumCurrentObj();
        assertNotNull(enumValue);
        System.out.println(VUOTA);
        System.out.println(String.format("Valore corrente originario di typeEnum per la preferenza %s%s", FORWARD, enumValue));
    }

    @Test
    @Order(14)
    @DisplayName("14 - prova tipo enumerationString")
    void enumerationString() {
        System.out.println("14 - prova tipo enumerationString");
        String allEnumSelection;
        String oldValue = VUOTA;
        String newValue = "gamma";
        String currentValue;

        allEnumSelection = (String) SPref.enumerationString.getDefaultValue();
        assertNotNull(allEnumSelection);
        System.out.println(VUOTA);
        System.out.println(String.format("Valore di default della enumeration %s%s", FORWARD, allEnumSelection));

        allEnumSelection = SPref.enumerationString.getEnumAll();
        assertNotNull(allEnumSelection);
        System.out.println(VUOTA);
        System.out.println(String.format("Valore completo old della preferenza %s%s", FORWARD, allEnumSelection));

        oldValue = SPref.enumerationString.getEnumCurrent();
        System.out.println(VUOTA);
        System.out.println(String.format("Valore selezionato old della preferenza %s%s", FORWARD, oldValue));

        SPref.enumerationString.setEnumCurrent(newValue);
        System.out.println(VUOTA);
        System.out.println(String.format("Modifica effettuata col nuovo valore %s%s", FORWARD, newValue));

        allEnumSelection = SPref.enumerationString.getEnumAll();
        assertNotNull(allEnumSelection);
        System.out.println(VUOTA);
        System.out.println(String.format("Valore completo modificato della preferenza %s%s", FORWARD, allEnumSelection));

        currentValue = SPref.enumerationString.getEnumCurrent();
        System.out.println(VUOTA);
        System.out.println(String.format("Valore selezionato modificato della preferenza %s%s", FORWARD, currentValue));

        SPref.enumerationString.setEnumCurrent(oldValue);

        allEnumSelection = SPref.enumerationString.getEnumAll();
        assertNotNull(allEnumSelection);
        System.out.println(VUOTA);
        System.out.println(String.format("Valore completo originario della preferenza %s%s", FORWARD, allEnumSelection));

        oldValue = SPref.enumerationString.getEnumCurrent();
        System.out.println(VUOTA);
        System.out.println(String.format("Valore selezionato originario della preferenza %s%s", FORWARD, oldValue));
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