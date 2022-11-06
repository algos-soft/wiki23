package it.algos.integration.backend;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.packages.crono.anno.*;
import it.algos.vaad23.backend.packages.crono.secolo.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import java.util.*;
import java.util.stream.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: dom, 08-mag-2022
 * Time: 14:44
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("production")
@Tag("backend")
@DisplayName("Anno Backend")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnnoBackendTest extends AlgosTest {

    /**
     * The Service.
     */
    @InjectMocks
    private AnnoBackend backend;

    @Autowired
    private AnnoRepository repository;


    private Anno entityBean;


    private List<Anno> listaBeans;


    //--nome
    //--esiste
    protected static Stream<Arguments> ANNI() {
        return Stream.of(
                Arguments.of(VUOTA, false),
                Arguments.of("0", false),
                Arguments.of("24", true),
                Arguments.of("24 a.C.", true),
                Arguments.of("3208", false)
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
    @DisplayName("2 - findAll (entity)")
    void findAll() {
        System.out.println("2 - findAll (entity)");
        String message;

        listaBeans = backend.findAll();
        assertNotNull(listaBeans);
        message = String.format("Ci sono in totale %s entities di %s", textService.format(listaBeans.size()), "Anno");
        System.out.println(message);
        printAnni(listaBeans);
    }

    @Test
    @Order(3)
    @DisplayName("3 - findNomi (nome)")
    void findNomi() {
        System.out.println("3 - findNomi (nome)");
        String message;

        listaStr = backend.findNomi();
        assertNotNull(listaStr);
        message = String.format("Ci sono in totale %s anni", textService.format(listaStr.size()));
        System.out.println(message);
        printNomiAnni(listaStr);
    }



    @ParameterizedTest
    @MethodSource(value = "ANNI")
    @Order(21)
    @DisplayName("21 - findByNome")
    void findByNome(final String nome, final boolean esiste) {
        System.out.println("21 - findByNome");
        entityBean = backend.findByNome(nome);
        assertEquals(esiste, entityBean!=null);
        if (entityBean!=null) {
            System.out.println(String.format("L'anno '%s' esiste", nome));
        }
        else {
            System.out.println(String.format("L'anno '%s' non esiste", nome));
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

    void printAnni(List<Anno> listaAnni) {
        System.out.println(VUOTA);
        int k = 0;

        for (Anno anno : listaAnni) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.print(anno.nome);
            System.out.print(SPAZIO);
            System.out.println(anno.secolo);
        }
    }

    void printNomiAnni(List<String> listaAnni) {
        int k = 0;

        for (String anno : listaAnni) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.println(anno);
        }
    }

}
