package it.algos.integration.backend;

import it.algos.*;
import it.algos.base.*;
import it.algos.wiki23.backend.packages.cognome.*;
import org.junit.jupiter.api.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
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
 * Date: Mon, 17-Oct-2022
 * Time: 08:30
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("backend")
@DisplayName("Cognome Backend")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CognomeBackendTest extends WikiTest {

    /**
     * Classe principale di riferimento <br>
     */
    private CognomeBackend backend;

    @Autowired
    private CognomeRepository repository;


    private Cognome entityBean;


    private List<Cognome> listaBeans;

    /**
     * Qui passa una volta sola <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();

        backend = cognomeBackend;

        backend.repository = repository;
        backend.crudRepository = repository;
        backend.arrayService = arrayService;
        backend.reflectionService = reflectionService;
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
        int totBio = bioBackend.count();

        listaBeans = backend.findAll();
        assertNotNull(listaBeans);
        message = String.format("Nelle %s biografie ci sono %s cognomi distinti ordinati alfabeticamente", textService.format(totBio), textService.format(listaBeans.size()));
        System.out.println(message);
        printNumBio(listaBeans);
    }

    @Test
    @Order(3)
    @DisplayName("3 - findAllSortNumBio")
    void findAllSortNumBio() {
        System.out.println("3 - findAllSortNumBio");
        String message;
        int totBio = bioBackend.count();

        listaBeans = backend.findAllSortNumBio();
        assertNotNull(listaBeans);
        message = String.format("Nelle %s biografie ci sono %s cognomi distinti ordinati per numBio", textService.format(totBio), textService.format(listaBeans.size()));
        System.out.println(message);
        printNumBio(listaBeans);
    }


    @Test
    @Order(4)
    @DisplayName("4 - findCognomi")
    void findCognomi() {
        System.out.println("4 - findCognomi");
        String message;
        int totBio = bioBackend.count();

        listaStr = backend.findCognomi();
        assertNotNull(listaStr);
        message = String.format("Nelle %s biografie ci sono %s cognomi.cognomi distinti ordinati alfabeticamente", textService.format(totBio), textService.format(listaStr.size()));
        System.out.println(message);
        System.out.println(VUOTA);
        print(listaStr);
    }

    @Test
    @Order(5)
    @DisplayName("5 - findCognomiSortNumBio")
    void findCognomiSortNumBio() {
        System.out.println("5 - findCognomiSortNumBio");
        String message;
        int totBio = bioBackend.count();

        listaStr = backend.findCognomiSortNumBio();
        assertNotNull(listaStr);
        message = String.format("Nelle %s biografie ci sono %s cognomi.cognomi distinti ordinati per numBio", textService.format(totBio), textService.format(listaStr.size()));
        System.out.println(message);
        System.out.println(VUOTA);
        print(listaStr);
    }

//    @Test
    @Order(6)
    @DisplayName("6 - elabora")
    void elabora() {
        System.out.println("6 - elabora");
        String message;
        int totCognomi = backend.count();

        long inizio = System.currentTimeMillis();
        backend.elabora();
        message = String.format("Elaborate le numBio per ognuno dei %s cognomi in %s", textService.format(totCognomi), dateService.deltaText(inizio));
        System.out.println(message);
        System.out.println(VUOTA);
        print(listaStr);
    }


    protected void printNumBio(List<Cognome> lista) {
        String message;
        System.out.println(VUOTA);
        for (Cognome cognome : lista) {
            message = String.format("%s%s%s", cognome.cognome, SEP, cognome.numBio);
            System.out.println(message);
        }
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

    void printBeans(List<Cognome> listaBeans) {
        System.out.println(VUOTA);
        int k = 0;

        for (Cognome bean : listaBeans) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.println(bean);
        }
    }

}
