package it.algos.unit.service;

import com.vaadin.flow.router.*;
import it.algos.base.*;
import it.algos.vaad24.backend.boot.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.packages.utility.versione.*;
import it.algos.vaad24.backend.service.*;
import it.algos.vaad24.ui.views.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.mongodb.core.mapping.*;

import javax.validation.constraints.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: mer, 09-mar-2022
 * Time: 20:57
 * <p>
 * Unit test di una classe di servizio (di norma) <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("quickly")
@DisplayName("Annotation service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnnotationServiceTest extends AlgosTest {


    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private AnnotationService service;

    private Route route;

    private Qualifier qualifier;

    private Document document;

    private PageTitle pageTitle;

    private NotNull notNull;

    /**
     * Execute only once before running all tests <br>
     * Esegue una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpAll() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();

        //--reindirizzo l'istanza della superclasse
        service = annotationService;
    }


    /**
     * Qui passa prima di ogni test <br>
     * Invocare PRIMA il metodo setUpEach() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    protected void setUpEach() {
        super.setUpEach();

        route = null;
        qualifier = null;
        document = null;
        pageTitle = null;
    }


    @Test
    @Order(1)
    @DisplayName("getRoute")
    void getRoute() {
        sorgenteClasse = TextService.class;
        route = service.getRoute(sorgenteClasse);
        assertNull(route);
        System.out.println(String.format("Non esiste la @Route per la classe %s", sorgenteClasse.getSimpleName()));

//        System.out.println(VUOTA);
//        sorgenteClasse = HelloWorldView.class;
//        route = service.getRoute(sorgenteClasse);
//        assertNotNull(route);
//        System.out.println(String.format("La @Route per la classe %s è '%s'", sorgenteClasse.getSimpleName(), route.value()));
    }

    @Test
    @Order(2)
    @DisplayName("getQualifier")
    void getQualifier() {
//        sorgenteClasse = HelloWorldView.class;
//        qualifier = service.getQualifier(sorgenteClasse);
//        assertNull(qualifier);
//        System.out.println(String.format("Non esiste un @Qualifier per la classe %s", sorgenteClasse.getSimpleName()));

        System.out.println(VUOTA);
        sorgenteClasse = VersioneBackend.class;
        qualifier = service.getQualifier(sorgenteClasse);
        assertNotNull(qualifier);
        System.out.println(String.format("Il @Qualifier per la classe %s è '%s'", sorgenteClasse.getSimpleName(), qualifier.value()));
    }


    @Test
    @Order(3)
    @DisplayName("getDocument")
    void getDocument() {
//        sorgenteClasse = HelloWorldView.class;
//        document = service.getDocument(sorgenteClasse);
//        assertNull(document);
//        System.out.println(String.format("Non esiste un @Document per la classe %s", sorgenteClasse.getSimpleName()));

        //        System.out.println(VUOTA);
        //        sorgenteClasse = Versione.class;
        //        document = service.getDocument(sorgenteClasse);
        //        assertNotNull(document);
        //        System.out.println(String.format("Il @Document per la classe %s è '%s'", sorgenteClasse.getSimpleName(), document.value()));
    }

    @Test
    @Order(3)
    @DisplayName("getPageTitle")
    void getPageTitle() {
        sorgenteClasse = VaadCost.class;
        pageTitle = service.getPageTitle(sorgenteClasse);
        assertNull(pageTitle);
        System.out.println(String.format("Non esiste un @PageTitle per la classe %s", sorgenteClasse.getSimpleName()));

//        sorgenteClasse = HelloWorldView.class;
//        pageTitle = service.getPageTitle(sorgenteClasse);
//        assertNotNull(pageTitle);
//        System.out.println(String.format("Il @PageTitle per la classe %s è '%s'", sorgenteClasse.getSimpleName(), pageTitle.value()));
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