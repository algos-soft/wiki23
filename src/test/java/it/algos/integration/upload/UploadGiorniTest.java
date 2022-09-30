package it.algos.integration.upload;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.upload.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.junit.jupiter.api.extension.*;
import org.springframework.boot.test.context.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.test.context.junit.jupiter.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Tue, 26-Jul-2022
 * Time: 08:49
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
@Tag("upload")
@DisplayName("Giorni upload")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UploadGiorniTest extends WikiTest {


    /**
     * Classe principale di riferimento <br>
     */
    private UploadGiorni istanza;


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
        istanza = new UploadGiorni();
        assertNotNull(istanza);
        System.out.println(("1 - Costruttore base senza parametri"));
        System.out.println(VUOTA);
        System.out.println(String.format("Costruttore base senza parametri per un'istanza di %s", istanza.getClass().getSimpleName()));
    }

//    @Test
    @Order(2)
    @DisplayName("2 - Upload test di un giorno morto con e senza paragrafi")
    void uploadTestNato() {
        System.out.println("2 - Upload test di un giorno morto senza paragrafi");
        sorgente = "25 settembre";
        appContext.getBean(UploadGiorni.class).senzaParagrafi().morte().test().upload(sorgente);
        appContext.getBean(UploadGiorni.class).conParagrafi().morte().test().upload(sorgente);
    }

//    @Test
    @Order(3)
    @DisplayName("3 - Upload con e senza usaSottoGiorniAnni")
    void usaSottoGiorniAnni() {
        System.out.println("3 - Upload con e senza usaSottoGiorniAnni");
        sorgente = "25 settembre";
        boolean usaSottoGiorniAnni = WPref.usaSottoGiorniAnni.is();

        WPref.usaSottoGiorniAnni.setValue(false);
        appContext.getBean(UploadGiorni.class).morte().test().upload(sorgente);

        WPref.usaSottoGiorniAnni.setValue(true);
        appContext.getBean(UploadGiorni.class).morte().test().upload(sorgente);

        WPref.usaSottoGiorniAnni.setValue(usaSottoGiorniAnni);
    }

    //            @Test
    @Order(4)
    @DisplayName("4 - Upload reale di un giorno nato standard")
    void uploadReale() {
        System.out.println("4 - Upload reale di un giorno nato standard");
        sorgente = "25 settembre";
        appContext.getBean(UploadGiorni.class).nascita().upload(sorgente);
    }


//    @Test
    @Order(5)
    @DisplayName("5 - Upload all")
    void uploadAll() {
        System.out.println("5 - Upload all");
        appContext.getBean(UploadGiorni.class).uploadAll();
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