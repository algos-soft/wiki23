package it.algos.backend;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.packages.crono.giorno.*;
import it.algos.vaad24.backend.packages.crono.mese.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import java.util.*;
import java.util.stream.*;

/**
 * Project vaad24
 * Created by Algos
 * User: gac
 * Date: Tue, 13-Dec-2022
 * Time: 09:35
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("backend")
@DisplayName("Giorno Backend")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GiornoBackendTest extends AlgosUnitTest {

    /**
     * The Service.
     */
    @InjectMocks
    private GiornoBackend backend;

    @Autowired
    private GiornoRepository repository;

    private Giorno entityBean;

    private List<Giorno> listaBeans;

    private String dbName = "Giorno";

    private String backendName = "GiornoBackend";

    @InjectMocks
    private MeseBackend meseBackend;

    @Autowired
    private MeseRepository meseRepository;

    private Class entityClazz = Giorno.class;


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

        Assertions.assertNotNull(backend);

        backend.repository = repository;
        backend.crudRepository = repository;
        backend.arrayService = arrayService;
        backend.dateService = dateService;
        backend.textService = textService;
        backend.resourceService = resourceService;
        backend.reflectionService = reflectionService;
        backend.mongoService = mongoService;
        backend.meseBackend = meseBackend;
        backend.meseBackend.repository = meseRepository;
        backend.meseBackend.crudRepository = meseRepository;
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
        if (ottenutoIntero > 0) {
            message = String.format("Ci sono in totale %s entities di '%s' nel database mongoDB", textService.format(ottenutoIntero), dbName);
        }
        else {
            if (reflectionService.isEsisteMetodo(backend.getClass(), TAG_RESET_ONLY)) {
                message = String.format("La collection '%s' è ancora vuota. Usa il metodo %s.%s()", dbName, backendName, TAG_RESET_ONLY);
            }
            else {
                message = String.format("Nel database mongoDB la collection '%s' è ancora vuota", dbName);
            }
        }
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
        message = String.format("Ci sono in totale %s entities di %s", textService.format(listaBeans.size()), "Giorno");
        System.out.println(message);
        printGiorni(listaBeans);
    }

    @Test
    @Order(3)
    @DisplayName("3 - findNomi (nome)")
    void findNomi() {
        System.out.println("3 - findNomi (nome)");
        String message;

        listaStr = backend.findNomi();
        assertNotNull(listaStr);
        message = String.format("Ci sono in totale %s giorni", textService.format(listaStr.size()));
        System.out.println(message);
        printNomiGiorni(listaStr);
    }

    @Test
    @Order(4)
    @DisplayName("4 - findAllByMese (entity)")
    void findAllByMese() {
        System.out.println("4 - findAllByMese (entity)");

        for (Mese sorgente : meseBackend.findAll()) {
            listaBeans = backend.findAllByMese(sorgente);
            assertNotNull(listaBeans);
            message = String.format("Nel mese di %s ci sono %s giorni", sorgente, textService.format(listaBeans.size()));
            System.out.println(VUOTA);
            System.out.println(message);
            printGiorni(listaBeans);
        }
    }

    @Test
    @Order(5)
    @DisplayName("5 - findNomiByMese (nome)")
    void findNomiByMese() {
        System.out.println("5 - findNomiByMese (nome)");

        for (String sorgente : meseBackend.findNomi()) {
            listaStr = backend.findNomiByMese(sorgente);
            assertNotNull(listaStr);
            message = String.format("Nel mese di %s ci sono %s giorni", sorgente, textService.format(listaStr.size()));
            System.out.println(VUOTA);
            System.out.println(message);
            printNomiGiorni(listaStr);
        }
    }


    @Test
    @Order(11)
    @DisplayName("11 - giorni esistenti")
    void isEsisteCiclo() {
        System.out.println("11 - giorni esistenti");

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
            System.out.println(String.format("Il giorno %s esiste", sorgente));
        }
        else {
            System.out.println(String.format("Il giorno %s non esiste", sorgente));
        }
        System.out.println(VUOTA);
    }

    //    @Test
    @Order(91)
    @DisplayName("91 - resetOnlyEmpty pieno")
    void resetOnlyEmptyPieno() {
        System.out.println("91 - resetOnlyEmpty pieno");
        String message;

        ottenutoRisultato = backend.resetOnlyEmpty();
        printRisultato(ottenutoRisultato);

        listaBeans = backend.findAll();
        assertNotNull(listaBeans);
        System.out.println(VUOTA);
        message = String.format("Ci sono in totale %s entities di %s", textService.format(listaBeans.size()), dbName);
        System.out.println(message);
        printSubLista(listaBeans);
    }


    //    @Test
    @Order(92)
    @DisplayName("92 - resetOnlyEmpty vuoto")
    void resetOnlyEmptyVuoto() {
        System.out.println("92 - resetOnlyEmpty vuoto");
        String message;

        mongoService.deleteAll(entityClazz);
        ottenutoRisultato = backend.resetOnlyEmpty();
        printRisultato(ottenutoRisultato);

        listaBeans = backend.findAll();
        assertNotNull(listaBeans);
        System.out.println(VUOTA);
        message = String.format("Ci sono in totale %s entities di %s", textService.format(listaBeans.size()), dbName);
        System.out.println(message);
        printSubLista(listaBeans);
    }

    //    @Test
    @Order(93)
    @DisplayName("93 - resetForcing pieno")
    void resetForcingPieno() {
        System.out.println("93 - resetForcing pieno");
        String message;

        ottenutoRisultato = backend.resetForcing();
        printRisultato(ottenutoRisultato);

        listaBeans = backend.findAll();
        assertNotNull(listaBeans);
        System.out.println(VUOTA);
        message = String.format("Ci sono in totale %s entities di %s", textService.format(listaBeans.size()), dbName);
        System.out.println(message);
        printSubLista(listaBeans);
    }


    //    @Test
    @Order(94)
    @DisplayName("94 - resetForcing vuoto")
    void resetForcingVuoto() {
        System.out.println("94 - resetForcing vuoto");
        String message;

        mongoService.deleteAll(entityClazz);
        ottenutoRisultato = backend.resetForcing();
        printRisultato(ottenutoRisultato);

        listaBeans = backend.findAll();
        assertNotNull(listaBeans);
        System.out.println(VUOTA);
        message = String.format("Ci sono in totale %s entities di %s", textService.format(listaBeans.size()), dbName);
        System.out.println(message);
        printSubLista(listaBeans);
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


    void printGiorni(List<Giorno> listaGiorni) {
        int k = 0;

        for (Giorno giorno : listaGiorni) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.print(giorno.nome);
            System.out.print(SPAZIO);
            System.out.print(giorno.trascorsi);
            System.out.print(SPAZIO);
            System.out.println(giorno.mancanti);
        }
    }

    void printNomiGiorni(List<String> listaGiorni) {
        int k = 0;

        for (String giorno : listaGiorni) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.println(giorno);
        }
    }

    void printBeans(List<Giorno> listaBeans) {
        System.out.println(VUOTA);
        int k = 0;

        for (Giorno bean : listaBeans) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.println(bean);
        }
    }

}