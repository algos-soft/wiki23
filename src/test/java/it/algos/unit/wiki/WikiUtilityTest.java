package it.algos.unit.wiki;

import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.service.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Tue, 28-Jun-2022
 * Time: 14:54
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Text Service")
@Tag("quickly")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WikiUtilityTest extends AlgosTest {

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    @InjectMocks
    public WikiUtility service;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();
    }

    /**
     * Inizializzazione dei service <br>
     * Devono essere tutti 'mockati' prima di iniettare i riferimenti incrociati <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void initMocks() {
        assertNotNull(service);
    }
    /**
     * Regola tutti riferimenti incrociati <br>
     * Deve essere fatto dopo aver costruito tutte le referenze 'mockate' <br>
     * Nelle sottoclassi devono essere regolati i riferimenti dei service specifici <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixRiferimentiIncrociati() {
        service.textService = textService;
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
    @DisplayName("1 - Nati nel")
    void nati() {
        for (int k = 1; k <= 20; k++) {
            ottenuto = service.nati(String.valueOf(k));
            assertTrue(textService.isValid(ottenuto));
            System.out.println(ottenuto);
        }

        System.out.println(VUOTA);
        for (int k = 75; k <= 95; k++) {
            ottenuto = service.nati(String.valueOf(k));
            assertTrue(textService.isValid(ottenuto));
            System.out.println(ottenuto);
        }

        System.out.println(VUOTA);
        for (int k = 795; k <= 820; k++) {
            ottenuto = service.nati(String.valueOf(k));
            assertTrue(textService.isValid(ottenuto));
            System.out.println(ottenuto);
        }

        System.out.println(VUOTA);
        for (int k = 895; k <= 905; k++) {
            ottenuto = service.nati(String.valueOf(k));
            assertTrue(textService.isValid(ottenuto));
            System.out.println(ottenuto);
        }

    }

    @Test
    @Order(2)
    @DisplayName("2 - Morti nel")
    void morti() {
        for (int k = 1; k <= 100; k++) {
            ottenuto = service.morti(String.valueOf(k));
            assertTrue(textService.isValid(ottenuto));
            System.out.println(ottenuto);
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
