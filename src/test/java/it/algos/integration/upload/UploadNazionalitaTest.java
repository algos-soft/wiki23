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
 * Date: Tue, 14-Jun-2022
 * Time: 18:48
 * Unit test di una classe service o backend o query <br>
 * Estende la classe astratta AlgosTest che contiene le regolazioni essenziali <br>
 * Nella superclasse AlgosTest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse AlgosTest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("upload")
@DisplayName("Nazionalità upload")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UploadNazionalitaTest extends WikiTest {


    /**
     * Classe principale di riferimento <br>
     */
    private UploadNazionalita istanza;


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
        istanza = new UploadNazionalita();
        assertNotNull(istanza);
        System.out.println(("1- Costruttore base senza parametri"));
        System.out.println(VUOTA);
        System.out.println(String.format("Costruttore base senza parametri per un'istanza di %s", istanza.getClass().getSimpleName()));
    }


//    @Test
    @Order(2)
    @DisplayName("2 - Upload test di una nazionalità con e senza TOC")
    void uploadToc() {
        System.out.println("2 - Upload test di una nazionalità con e senza TOC");
        sorgente = "afghani";
        appContext.getBean(UploadNazionalita.class).forceToc().test().upload(sorgente);
        appContext.getBean(UploadNazionalita.class).noToc().test().upload(sorgente);
    }

//    @Test
    @Order(3)
    @DisplayName("3 - Upload test di una nazionalità plurale con TOC e sottopagine")
    void upload3() {
        System.out.println("3 - Upload test di una nazionalità plurale con TOC e sottopagine");
        sorgente = "azeri";
        appContext.getBean(UploadNazionalita.class).forceToc().test().upload(sorgente);
    }

//    @Test
    @Order(4)
    @DisplayName("4 - Upload test di una nazionalità plurale")
    void upload4() {
        System.out.println("4 - Upload test di una nazionalità plurale");
        sorgente = "assiri";
        appContext.getBean(UploadNazionalita.class).test().upload(sorgente);
    }

//    @Test
    @Order(5)
    @DisplayName("5 - Upload test di una nazionalità plurale")
    void upload5() {
        System.out.println("5 - Upload test di una nazionalità plurale");
        sorgente = "austro-ungarici";
        appContext.getBean(UploadNazionalita.class).test().upload(sorgente);
    }

//    @Test
    @Order(6)
    @DisplayName("6 - Upload test di una nazionalità plurale")
    void upload6() {
        System.out.println("6 - Upload test di una nazionalità plurale");
        sorgente = "capoverdiani";
        appContext.getBean(UploadNazionalita.class).test().upload(sorgente);
    }

//    @Test
    @Order(7)
    @DisplayName("7 - Upload test di una nazionalità plurale")
    void upload7() {
        System.out.println("7 - Upload test di una nazionalità plurale");
        sorgente = "algerini";
        appContext.getBean(UploadNazionalita.class).test().upload(sorgente);
    }
    @Test
    @Order(8)
    @DisplayName("8 - Upload test di una nazionalità plurale")
    void upload8() {
        System.out.println("7 - Upload test di una nazionalità plurale");
        sorgente = "albanesi";
        appContext.getBean(UploadNazionalita.class).test().upload(sorgente);
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