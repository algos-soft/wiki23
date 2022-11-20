package it.algos.integration.backend;

import it.algos.*;
import it.algos.base.*;
import it.algos.simple.backend.enumeration.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.packages.utility.preferenza.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
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
 * Date: sab, 07-mag-2022
 * Time: 14:48
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("backend")
@DisplayName("Preferenza Backend")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PreferenzaBackendTest extends AlgosTest {

    /**
     * The Service.
     */
    @InjectMocks
    private PreferenzaBackend backend;

    @Autowired
    private PreferenzaRepository repository;

    private Preferenza entityBean;

    //--enum
    protected static Stream<Arguments> KEY_PREFERENZE() {
        return Stream.of(
                Arguments.of(Pref.debug),
                Arguments.of(Pref.usaNonBreaking),
                Arguments.of(Pref.durataAvviso)
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
    @DisplayName("2 - findByKeyCode")
    void findByKeyCode() {
        System.out.println("2 - findByKeyCode");
        System.out.println(VUOTA);
        System.out.println("Preferenze create dalla Enumeration di Vaadin23");
        int k = 0;

        List<Pref> lista = Pref.getAllEnums();
        for (Pref enumPref : lista) {
            sorgente = enumPref.getKeyCode();
            entityBean = backend.findByKeyCode(sorgente);
            assertNotNull(entityBean);
            System.out.println(String.format("%d) %s%s%s", ++k, entityBean, FORWARD, entityBean.descrizione));
        }
        System.out.println(VUOTA);
        System.out.println(String.format("Tempo totale per le %d preferenze: %s", lista.size(), dateService.deltaTextEsatto(inizio)));
    }


    @Test
    @Order(3)
    @DisplayName("3 - findByKeyCode di Simple")
    void findByKeyCode2() {
        System.out.println("2 - findByKeyCode di Simple");
        System.out.println(VUOTA);
        System.out.println("Preferenze create dalla Enumeration di Vaadin23+Simple");
        int k = 0;

        List<Pref> lista = Pref.getAllEnums();
        for (Pref enumPref : lista) {
            sorgente = enumPref.getKeyCode();
            entityBean = backend.findByKeyCode(sorgente);
            assertNotNull(entityBean);
            System.out.println(String.format("%d) %s%s%s", ++k, entityBean, FORWARD, entityBean.descrizione));
        }
        List<SPref> lista2 = SPref.getAllEnums();
        for (SPref enumPref : lista2) {
            sorgente = enumPref.getKeyCode();
            entityBean = backend.findByKeyCode(sorgente);
            assertNotNull(entityBean);
            System.out.println(String.format("%d) %s%s%s", ++k, entityBean, FORWARD, entityBean.descrizione));
        }
        System.out.println(VUOTA);
        System.out.println(String.format("Tempo totale per le %d preferenze: %s", lista.size(), dateService.deltaTextEsatto(inizio)));
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
