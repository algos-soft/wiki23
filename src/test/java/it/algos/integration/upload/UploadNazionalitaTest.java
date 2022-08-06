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


    @Test
    @Order(2)
    @DisplayName("2 - Upload test di una nazionalità plurale")
    void uploadMortiTest() {
        System.out.println("2 - Upload test di una nazionalità plurale");
        sorgente = "azeri";
        //        appContext.getBean(UploadNazionalita.class).pagina().uploadTest();
        ottenutoRisultato = appContext.getBean(UploadNazionalita.class).plurale().pagina().test().upload(sorgente);
    }

    //    @Test
    //    @Order(2)
    //    @DisplayName("2 - Upload di una nazionalità plurale")
    //    void getListaDidascalie() {
    //        System.out.println("2 - Upload di una nazionalità plurale");
    //        System.out.println(VUOTA);
    //        sorgente = "azeri";
    //        int oldValue;
    //        int tempValue = 50;
    //
    //        System.out.println("Metto da parte il valore della preferenza");
    //        oldValue = WPref.sogliaSottoPagina.getInt();
    //        System.out.println("Regolo provvisoriamente il valore della preferenza");
    //        WPref.sogliaSottoPagina.setValue(tempValue);
    //
    //        appContext.getBean(UploadNazionalita.class).uploadTest(sorgente);
    //
    //        System.out.println("Ripristino il valore originario della preferenza");
    //        WPref.sogliaSottoPagina.setValue(oldValue);
    //    }

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