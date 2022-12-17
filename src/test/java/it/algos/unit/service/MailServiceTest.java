package it.algos.unit.service;

import it.algos.base.*;
import it.algos.vaad24.backend.service.*;
import org.junit.jupiter.api.*;

import java.time.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: mar, 08-mar-2022
 * Time: 11:33
 * <p>
 * Unit test di una classe di servizio (di norma) <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("slowly")
@DisplayName("Mail service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MailServiceTest extends AlgosTest {


    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private MailService service;

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
        service = mailService;
    }


    /**
     * Qui passa prima di ogni test <br>
     * Invocare PRIMA il metodo setUpEach() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    protected void setUpEach() {
        super.setUpEach();
    }


    /**
     * Spedizione standard senza mittente e senza destinatario
     */
    @Test
    @Order(1)
    @DisplayName("Primo test")
    public void send() {
        sorgente = "Prova";
        sorgente2 = String.format("Spedizione standard senza mittente e senza destinatario effettuato alle %s", LocalDateTime.now());

        service.send(sorgente, sorgente2);
    }


    /**
     * Spedizione standard senza mittente e con destinatario
     */
    @Test
    @Order(2)
    @DisplayName("Secondo test")
    public void send2() {
        sorgente = "Prova2";
        sorgente2 = String.format("Spedizione standard col destinatario effettuato alle %s", LocalDateTime.now());

        service.send("pippoz@algos.it", sorgente, sorgente2);
    }

    /**
     * Spedizione standard senza mittente e con destinatario
     */
    @Test
    @Order(3)
    @DisplayName("Terzo test")
    public void send3() {
        sorgente = "Prova3";
        sorgente2 = String.format("Spedizione standard col destinatario effettuato alle %s", LocalDateTime.now());

        service.send(sorgente, sorgente2);
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