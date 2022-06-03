package it.algos.unit.wiki;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.service.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Mon, 30-May-2022
 * Time: 06:51
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Text Service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BioServiceTest extends WikiTest {

    /**
     * The Service.
     */
    @InjectMocks
    BioService service;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();

        //--reindirizzo l'istanza della superclasse
        service = bioService;
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

    public static Function<Bio, String> cognome = bio -> bio.getCognome()!=null?bio.getCognome():VUOTA;

    @Test
    @Order(1)
    @DisplayName("1 - lista attivita singole da singolo")
    void fetchAttivitaSingolo() {
        System.out.println("1 - lista attivita singole da singolo");

        sorgente = "attore";
        listaStr = Arrays.asList(sorgente);
        listBio = service.fetchAttivita(listaStr);
        printBio(listBio);
    }

    @Test
    @Order(2)
    @DisplayName("2 - lista attivita singole da plurale")
    void fetchAttivitaPlurale() {
        System.out.println("2 - lista attivita singole da plurale");

        sorgente = "cantanti";
        listaStr = attivitaBackend.findSingolariByPlurale(sorgente);
        listBio = service.fetchAttivita(listaStr);
        assertNotNull(listBio);
        printBio(listBio, sorgente + " ordinate");
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
