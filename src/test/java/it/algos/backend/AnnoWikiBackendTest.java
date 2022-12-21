package it.algos.backend;

import it.algos.*;
import it.algos.base.*;
import it.algos.wiki23.backend.packages.anno.*;
import org.junit.jupiter.api.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import java.util.*;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Tue, 08-Nov-2022
 * Time: 11:51
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("backend")
@DisplayName("AnnoWiki Backend")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnnoWikiBackendTest extends WikiTest {

    /**
     * The Service.
     */
    @InjectMocks
    private AnnoWikiBackend backend;

    @Autowired
    private AnnoWikiRepository repository;


    private AnnoWiki entityBean;


    private List<AnnoWiki> listaBeans;

    /**
     * Qui passa una volta sola <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();

        Assertions.assertNotNull(backend);

        backend.repository = repository;
        backend.crudRepository = repository;
        backend.arrayService = arrayService;
        backend.reflectionService = reflectionService;
        backend.mongoService = mongoService;
        backend.annoBackend = annoBackend;
        backend.wikiUtility = wikiUtility;
    }


    /**
     * Inizializzazione dei service <br>
     * Devono essere tutti 'mockati' prima di iniettare i riferimenti incrociati <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void initMocks() {
        super.initMocks();
    }


    /**
     * Regola tutti riferimenti incrociati <br>
     * Deve essere fatto dopo aver costruito tutte le referenze 'mockate' <br>
     * Nelle sottoclassi devono essere regolati i riferimenti dei service specifici <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixRiferimentiIncrociati() {
        super.fixRiferimentiIncrociati();
    }

    @BeforeEach
    protected void setUpEach() {
        super.setUpEach();

        this.entityBean = null;
        this.listaBeans = null;
    }


    @Test
    @Order(1)
    @DisplayName("1 - count")
    void count() {
        System.out.println("1 - count");
        String message;

        ottenutoIntero = backend.count();
        assertTrue(ottenutoIntero > 0);
        message = String.format("Ci sono in totale %s entities nel database mongoDB", textService.format(ottenutoIntero));
        System.out.println(message);
    }


    @Test
    @Order(2)
    @DisplayName("2 - findAll")
    void findAll() {
        System.out.println("2 - findAll");
        String message;

        listaBeans = backend.findAll();
        assertNotNull(listaBeans);
        message = String.format("Ci sono in totale %s entities di %s", textService.format(listaBeans.size()), "AnnoWiki");
        System.out.println(message);
    }

//    @Test
    @Order(6)
    @DisplayName("6 - reset")
    void reset() {
        System.out.println("6 - reset");
        String message;

//        ottenutoBooleano = backend.resetOnlyEmpty();
//        assertTrue(ottenutoBooleano);
//        listaBeans = backend.findAll();
//        assertNotNull(listaBeans);
//        message = String.format("Ci sono in totale %s entities di %s", textService.format(listaBeans.size()), "AnnoWiki");
//        System.out.println(message);
//        printBeans(listaBeans);
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

    void printBeans(List<AnnoWiki> listaBeans) {
        System.out.println(VUOTA);
        int k = 0;

        for (AnnoWiki anno : listaBeans) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.print(anno.nome);
            System.out.print(SPAZIO);
            System.out.print(anno.ordine);
            System.out.print(SPAZIO);
            System.out.println(anno.secolo);
        }
    }
}