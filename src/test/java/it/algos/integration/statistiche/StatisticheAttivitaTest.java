package it.algos.integration.statistiche;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.statistiche.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.junit.jupiter.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Fri, 01-Jul-2022
 * Time: 11:05
 * Unit test di una classe service o backend o query <br>
 * Estende la classe astratta AlgosTest che contiene le regolazioni essenziali <br>
 * Nella superclasse AlgosTest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse AlgosTest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("statistiche")
@DisplayName("Test StatisticheAttivita")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StatisticheAttivitaTest extends WikiTest {


    /**
     * Classe principale di riferimento <br>
     */
    private StatisticheAttivita istanza;


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
    @DisplayName("1- Costruttore base senza parametri")
    void costruttoreBase() {
        istanza = new StatisticheAttivita();
        assertNotNull(istanza);
        System.out.println(("1- Costruttore base senza parametri"));
        System.out.println(VUOTA);
        System.out.println(String.format("Costruttore base senza parametri per un'istanza di %s", istanza.getClass().getSimpleName()));
    }

//    @Test
    @Order(2)
    @DisplayName("2 - Upload col valore 'usaTreAttivita=false'")
    void upload2() {
        System.out.println(("2 - Crea la pagina di test col valore 'usaTreAttivita=false'"));
        boolean oldValue = WPref.usaTreAttivita.is();

        System.out.println(VUOTA);
        WPref.usaTreAttivita.setValue(false);
        ottenutoRisultato = appContext.getBean(StatisticheAttivita.class).upload();
        assertTrue(ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato);

        System.out.println(VUOTA);
        System.out.println(("Ripristino il precedente valore della preferenza 'usaTreAttivita'"));
        WPref.usaTreAttivita.setValue(oldValue);
    }

//    @Test
    @Order(3)
    @DisplayName("3 - Upload col valore 'usaTreAttivita=true'")
    void upload3() {
        System.out.println(("3 - Crea la pagina di test col valore 'usaTreAttivita=true'"));
        boolean oldValue = WPref.usaTreAttivita.is();

        System.out.println(VUOTA);
        WPref.usaTreAttivita.setValue(true);
        ottenutoRisultato = appContext.getBean(StatisticheAttivita.class).upload();
        assertTrue(ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato);

        System.out.println(VUOTA);
        System.out.println(("Ripristino il precedente valore della preferenza 'usaTreAttivita'"));
        WPref.usaTreAttivita.setValue(oldValue);
    }

    @Test
    @Order(4)
    @DisplayName("4 - Upload col valore 'usaLinkStatistiche=true'")
    void upload4() {
        System.out.println(("4 - Crea la pagina di test col valore 'usaLinkStatistiche=true'"));
        boolean oldValue = WPref.usaLinkStatistiche.is();
        boolean oldValue2 = WPref.usaTreAttivita.is();

        System.out.println(VUOTA);
        WPref.usaLinkStatistiche.setValue(true);
        WPref.usaTreAttivita.setValue(false);
        ottenutoRisultato = appContext.getBean(StatisticheAttivita.class).upload();
        assertTrue(ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato);

        System.out.println(VUOTA);
        System.out.println(("Ripristino il precedente valore della preferenza 'usaLinkStatistiche'"));
        WPref.usaLinkStatistiche.setValue(oldValue);
        WPref.usaTreAttivita.setValue(oldValue2);
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