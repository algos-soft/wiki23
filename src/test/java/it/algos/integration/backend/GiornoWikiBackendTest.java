package it.algos.integration.backend;

import it.algos.*;
import it.algos.base.*;
import it.algos.wiki23.backend.packages.giorno.*;
import org.junit.jupiter.api.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import java.util.*;
import java.util.stream.*;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Tue, 30-Aug-2022
 * Time: 18:23
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("production")
@Tag("production")
@Tag("backend")
@DisplayName("GiornoWiki Backend")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GiornoWikiBackendTest extends WikiTest {

    /**
     * The Service.
     */
    @InjectMocks
    private GiornoWikiBackend backend;

    @Autowired
    private GiornoWikiRepository repository;


    private GiornoWiki entityBean;


    private List<GiornoWiki> listaBeans;

    //--giorno
    //--esistente
    protected static Stream<Arguments> GIORNI() {
        return Stream.of(
                Arguments.of(null, false),
                Arguments.of(VUOTA, false),
                Arguments.of("23 febbraio", true),
                Arguments.of("43 marzo", false),
                Arguments.of("19 dicembra", false),
                Arguments.of("4 gennaio", true)
                );
    }

    /**
     * Qui passa una volta sola <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();

        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(backend);
        Assertions.assertNotNull(backend);

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

        listaBeans = backend.findAll();
        assertNotNull(listaBeans);
        message = String.format("Ci sono in totale %s entities di %s", textService.format(listaBeans.size()), "GiornoWiki");
        System.out.println(message);
    }

    @Test
    @Order(3)
    @DisplayName("3 - giorni esistenti")
    void isEsisteCiclo() {
        System.out.println("3 - giorni esistenti");

        //--giorno
        //--esistente
        System.out.println(VUOTA);
        GIORNI().forEach(this::isEsisteBase);
    }




    //--giorno
    //--esistente
    void isEsisteBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        previstoBooleano = (boolean) mat[1];

        ottenutoBooleano = backend.isEsiste(sorgente);
        assertEquals(previstoBooleano, ottenutoBooleano);
        if (ottenutoBooleano) {
            System.out.println(String.format("Il giorno %s esiste",sorgente));
        }
        else {
            System.out.println(String.format("Il giorno %s non esiste",sorgente));
        }
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

    void printBeans(List<GiornoWiki> listaBeans) {
        System.out.println(VUOTA);
        int k = 0;

        for (GiornoWiki bean : listaBeans) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.println(bean);
        }
    }

}
