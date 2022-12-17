package it.algos.unit.service;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.service.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.junit.jupiter.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: Sat, 27-Aug-2022
 * Time: 17:04
 */
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Text Service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegexServiceTest extends AlgosTest {

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private RegexService service;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();

        //--reindirizzo l'istanza della superclasse
        service = regexService;
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
    @DisplayName("1 - controllo esistenza true senza regex")
    void isEsiste1() {
        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n|Sesso=\n|LuogoNascita = Polla\n";
        sorgente2 = "\n|Sesso=\n";

        ottenutoBooleano = service.isEsiste(sorgente, sorgente2);
        assertTrue(ottenutoBooleano);
    }


    @Test
    @Order(2)
    @DisplayName("2 - controllo esistenza true e false")
    void isEsiste2() {
        sorgente2 = "\n*\\| *Sesso *= *\n*\\|";

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n| Sesxso = \n|LuogoNascita = Polla\n";
        ottenutoBooleano = service.isEsiste(sorgente, sorgente2);
        assertFalse(ottenutoBooleano);

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\nSesso = \n|LuogoNascita = Polla\n";
        ottenutoBooleano = service.isEsiste(sorgente, sorgente2);
        assertFalse(ottenutoBooleano);

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n|Sesso=\n|LuogoNascita = Polla\n";
        ottenutoBooleano = service.isEsiste(sorgente, sorgente2);
        assertTrue(ottenutoBooleano);

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n|Sesso =\n|LuogoNascita = Polla\n";
        ottenutoBooleano = service.isEsiste(sorgente, sorgente2);
        assertTrue(ottenutoBooleano);

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n|Sesso= \n|LuogoNascita = Polla\n";
        ottenutoBooleano = service.isEsiste(sorgente, sorgente2);
        assertTrue(ottenutoBooleano);

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n|Sesso = \n|LuogoNascita = Polla\n";
        ottenutoBooleano = service.isEsiste(sorgente, sorgente2);
        assertTrue(ottenutoBooleano);

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n| Sesso=\n|LuogoNascita = Polla\n";
        ottenutoBooleano = service.isEsiste(sorgente, sorgente2);
        assertTrue(ottenutoBooleano);

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n| Sesso= \n|LuogoNascita = Polla\n";
        ottenutoBooleano = service.isEsiste(sorgente, sorgente2);
        assertTrue(ottenutoBooleano);

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n| Sesso =\n|LuogoNascita = Polla\n";
        ottenutoBooleano = service.isEsiste(sorgente, sorgente2);
        assertTrue(ottenutoBooleano);

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n| Sesso = \n|LuogoNascita = Polla\n";
        ottenutoBooleano = service.isEsiste(sorgente, sorgente2);
        assertTrue(ottenutoBooleano);
    }

    @Test
    @Order(3)
    @DisplayName("3 - valore reale esistente")
    void getReal() {
        sorgente2 = "\n*\\| *Sesso *= *\n*\\|";

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n| Sesxso = \n|LuogoNascita = Polla\n";
        previsto = VUOTA;
        ottenuto = service.getReal(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\nSesso = \n|LuogoNascita = Polla\n";
        previsto = VUOTA;
        ottenuto = service.getReal(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n|Sesso=\n|LuogoNascita = Polla\n";
        previsto = "\n|Sesso=\n|";
        ottenuto = service.getReal(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(String.format("Ottenuto %s", ottenuto));

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n|Sesso =\n|LuogoNascita = Polla\n";
        previsto = "\n|Sesso =\n|";
        ottenuto = service.getReal(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(String.format("Ottenuto %s", ottenuto));

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n|Sesso= \n|LuogoNascita = Polla\n";
        previsto = "\n|Sesso= \n|";
        ottenuto = service.getReal(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(String.format("Ottenuto %s", ottenuto));

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n|Sesso = \n|LuogoNascita = Polla\n";
        previsto = "\n|Sesso = \n|";
        ottenuto = service.getReal(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(String.format("Ottenuto %s", ottenuto));

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n| Sesso=\n|LuogoNascita = Polla\n";
        previsto = "\n| Sesso=\n|";
        ottenuto = service.getReal(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(String.format("Ottenuto %s", ottenuto));

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n| Sesso= \n|LuogoNascita = Polla\n";
        previsto = "\n| Sesso= \n|";
        ottenuto = service.getReal(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(String.format("Ottenuto %s", ottenuto));

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n| Sesso =\n|LuogoNascita = Polla\n";
        previsto = "\n| Sesso =\n|";
        ottenuto = service.getReal(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(String.format("Ottenuto %s", ottenuto));

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n| Sesso = \n|LuogoNascita = Polla\n";
        previsto = "\n| Sesso = \n|";
        ottenuto = service.getReal(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(String.format("Ottenuto %s", ottenuto));
    }

    @Test
    @Order(4)
    @DisplayName("4 - replaceFirst")
    void replaceFirst() {
        sorgente2 = "\n*\\| *Sesso *= *\n*\\|";
        sorgente3 = "\n|Sesso = M\n|";

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n| Sesxso = \n|LuogoNascita = Polla\n";
        previsto = sorgente;
        ottenuto = service.replaceFirst(sorgente, sorgente2, sorgente3);
        assertEquals(previsto, ottenuto);

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\nSesso = \n|LuogoNascita = Polla\n";
        previsto = sorgente;
        ottenuto = service.replaceFirst(sorgente, sorgente2, sorgente3);
        assertEquals(previsto, ottenuto);

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n|Sesso=\n|LuogoNascita = Polla\n";
        previsto = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n|Sesso = M\n|LuogoNascita = Polla\n";
        ottenuto = service.replaceFirst(sorgente, sorgente2, sorgente3);
        assertEquals(previsto, ottenuto);

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n|Sesso =\n|LuogoNascita = Polla\n";
        ottenuto = service.replaceFirst(sorgente, sorgente2, sorgente3);
        assertEquals(previsto, ottenuto);

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n|Sesso= \n|LuogoNascita = Polla\n";
        ottenuto = service.replaceFirst(sorgente, sorgente2, sorgente3);
        assertEquals(previsto, ottenuto);

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n|Sesso = \n|LuogoNascita = Polla\n";
        ottenuto = service.replaceFirst(sorgente, sorgente2, sorgente3);
        assertEquals(previsto, ottenuto);

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n| Sesso=\n|LuogoNascita = Polla\n";
        ottenuto = service.replaceFirst(sorgente, sorgente2, sorgente3);
        assertEquals(previsto, ottenuto);

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n| Sesso= \n|LuogoNascita = Polla\n";
        ottenuto = service.replaceFirst(sorgente, sorgente2, sorgente3);
        assertEquals(previsto, ottenuto);

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n| Sesso =\n|LuogoNascita = Polla\n";
        ottenuto = service.replaceFirst(sorgente, sorgente2, sorgente3);
        assertEquals(previsto, ottenuto);

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n| Sesso = \n|LuogoNascita = Polla\n";
        ottenuto = service.replaceFirst(sorgente, sorgente2, sorgente3);
        assertEquals(previsto, ottenuto);
    }

    @Test
    @Order(5)
    @DisplayName("5 - count")
    void count() {
        sorgente2 = "\n*\\| *Sesso *= *[MF]*\n*\\|";

        sorgente = "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n|Sesso =\n|LuogoNascita = Polla\n";
        previstoIntero = 1;
        ottenutoIntero = service.count(sorgente, sorgente2);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "{{Sportivo\n|Nome = Paulo André\n|Immagine = Paulo Andre.jpg\n|Sesso = M\n|CodiceNazione = {{BRA}}\n";
        previstoIntero = 1;
        ottenutoIntero = service.count(sorgente, sorgente2);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "{{Sportivo\n|Nome = Paulo André\n|Immagine = Paulo Andre.jpg\n|Sesso = M\n|CodiceNazione = {{BRA}}\n" +
                "{{Bio\n|Nome = Michela\n|Cognome = Rostan\n|Sesso =\n|LuogoNascita = Polla\n";
        previstoIntero = 2;
        ottenutoIntero = service.count(sorgente, sorgente2);
        assertEquals(previstoIntero, ottenutoIntero);
    }


    @Test
    @Order(6)
    @DisplayName("6 - estrae mese")
    void countz() {
        sorgente2 = "(gennaio|febbraio|marzo|aprile|maggio|giugno|luglio|agosto|settembre|ottobre|novembre|dicembre)$";

        sorgente = "3 agosto";
        previsto = "agosto";
        ottenuto = service.getReal(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);

        sorgente = "25 settembre";
        previsto = "settembre";
        ottenuto = service.getReal(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
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
