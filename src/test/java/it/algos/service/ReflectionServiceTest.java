package it.algos.service;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.packages.crono.secolo.*;
import it.algos.vaad24.backend.service.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.junit.jupiter.*;

/**
 * Project vaad24
 * Created by Algos
 * User: gac
 * Date: Tue, 20-Dec-2022
 * Time: 20:49
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("service")
@DisplayName("Reflection Service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReflectionServiceTest extends AlgosIntegrationTest {

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private ReflectionService service;

    @Autowired
    private SecoloRepository secoloRepository;

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();

        //--reindirizzo l'istanza della superclasse
        service = reflectionService;
    }


    /**
     * Qui passa a ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    protected void setUpEach() {
        super.setUpEach();
    }


    @Test
    @Order(1)
    @DisplayName("1 - field semplice a livello entity base")
    void getField() {
        System.out.println("1 - field semplice a livello entity base");
        System.out.println(VUOTA);

        clazz = Secolo.class;
        sorgente = FIELD_NAME_NOME;
        ottenutoField = service.getField(clazz, sorgente);
        assertNotNull(ottenutoField);
        System.out.println(ottenutoField.getName());
    }

    @Test
    @Order(2)
    @DisplayName("2 - field a livello entity superiore")
    void getLabelHost() {
        System.out.println("2 - field a livello entity superiore");
        System.out.println(VUOTA);

        clazz = Secolo.class;
        sorgente = FIELD_NAME_NOTE;
        ottenutoField = service.getField(clazz, sorgente);
//        assertNull(ottenutoField);

        try {
            ottenutoField =   clazz.getField(sorgente);
            System.out.println(ottenutoField.getName());
        } catch (Exception unErrore) {
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

}