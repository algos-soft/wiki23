package it.algos.integration.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.upload.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.springframework.boot.test.context.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.test.context.junit.jupiter.*;

import java.util.stream.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Fri, 22-Jul-2022
 * Time: 10:22
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("production")
@Tag("upload")
@DisplayName("Anni upload")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UploadAnniTest extends WikiTest {


    /**
     * Classe principale di riferimento <br>
     */
    private UploadAnni istanza;


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
        istanza = new UploadAnni();
        assertNotNull(istanza);
        System.out.println(("1- Costruttore base senza parametri"));
        System.out.println(VUOTA);
        System.out.println(String.format("Costruttore base senza parametri per un'istanza di %s", istanza.getClass().getSimpleName()));
    }

    //    @Test
    @Order(2)
    @DisplayName("2 - Upload test di un anno morto senza indicare con/senza paragrafi ma li mette di default")
    void uploadMortiTest() {
        System.out.println("2 - Upload test di un anno morto senza indicare con/senza paragrafi ma li mette di default");
        sorgente = "1957";
        appContext.getBean(UploadAnni.class).morte().test().upload(sorgente);
    }

    //    @Test
    @Order(3)
    @DisplayName("3 - Upload test di un anno morto con paragrafi ma non li mette perché troppe poche voci")
    void uploadMortiTest2() {
        System.out.println("3 - Upload test di un anno morto con paragrafi ma non li mette perché troppe poche voci");
        sorgente = "214 a.C.";
        appContext.getBean(UploadAnni.class).conParagrafi().morte().test().upload(sorgente);
    }


    //    @Test
    @Order(4)
    @DisplayName("4 - Upload test di un anno nato con e senza paragrafi")
    void uploadTestNato() {
        System.out.println("4 - Upload test di un anno nato senza paragrafi");
        sorgente = "1785";
        ottenutoRisultato = appContext.getBean(UploadAnni.class).nascita().test().upload(sorgente);
        printRisultato(ottenutoRisultato);
        //        appContext.getBean(UploadAnni.class).conParagrafi().nascita().test().upload(sorgente);
    }

    //    @Test
    @Order(5)
    @DisplayName("5 - Upload reale di un anno morto con paragrafi")
    void uploadNatiTest() {
        System.out.println("5 - Upload reale di un anno morto con paragrafi");
        sorgente = "214 a.C.";
        appContext.getBean(UploadAnni.class).morte().upload(sorgente);
    }


    //        @Test
    @Order(6)
    @DisplayName("6 - Upload all")
    void uploadAll() {
        System.out.println("6 - Upload all");
        appContext.getBean(UploadAnni.class).uploadAll();
    }


//    @ParameterizedTest
    @MethodSource(value = "ANNI")
    @DisplayName("7 - Titolo pagine")
    void uploadVari(String nomeAnno, final AETypeLista type) {
        sorgente = nomeAnno;
        appContext.getBean(UploadAnni.class).morte().upload(sorgente);
    }


    //    @Test
    @Order(8)
    @DisplayName("8 - Upload test di un anno anno morto con paragrafi")
    void uploadTestMorto() {
        System.out.println("8 - Upload test di un anno anno morto con paragrafi");
        sorgente = "1168";
        ottenutoRisultato = appContext.getBean(UploadAnni.class).conParagrafi().morte().test().upload(sorgente);
        printRisultato(ottenutoRisultato);
    }

    //    @Test
    @Order(8)
    @DisplayName("8 - Upload test di un anno anno morto con paragrafi")
    void uploadTestMorto2() {
        System.out.println("8 - Upload test di un anno anno morto con paragrafi");
        sorgente = "130 a.C.";
        ottenutoRisultato = appContext.getBean(UploadAnni.class).conParagrafi().morte().test().upload(sorgente);
        printRisultato(ottenutoRisultato);
    }

    //    @Test
    @Order(9)
    @DisplayName("9 - Upload test di un anno anno morto con paragrafi")
    void uploadTestMorto3() {
        System.out.println("9 - Upload test di un anno anno morto con paragrafi");
        sorgente = "1745";
        ottenutoRisultato = appContext.getBean(UploadAnni.class).nascita().test().upload(sorgente);
        printRisultato(ottenutoRisultato);
    }

    //    @Test
    @Order(10)
    @DisplayName("10 - Upload test di un anno anno nato con paragrafi")
    void uploadTestMorto4() {
        System.out.println("10 - Upload test di un anno anno nato con paragrafi");
        sorgente = "866";
        ottenutoRisultato = appContext.getBean(UploadAnni.class).morte().test().upload(sorgente);
        printRisultato(ottenutoRisultato);
    }

    //    @Test
    @Order(11)
    @DisplayName("11 - Upload test di un anno anno nato con paragrafi")
    void uploadTestMorto5() {
        System.out.println("11 - Upload test di un anno anno nato con paragrafi");
        sorgente = "1524";
        ottenutoRisultato = appContext.getBean(UploadAnni.class).morte().test().upload(sorgente);
        printRisultato(ottenutoRisultato);
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
