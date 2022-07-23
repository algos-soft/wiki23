package it.algos.unit.wiki;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.service.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Tue, 28-Jun-2022
 * Time: 14:54
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Text Service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WikiUtilityTest extends WikiTest {

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
    @DisplayName("1 - WikiTitle di tutti i 366 giorni di nascita")
    void wikiTitleNatiGiorno() {
        System.out.println("1 - WikiTitle di tutti i 366 giorni di nascita");
        listaStr = giornoBackend.findAll();

        for (String giorno : listaStr) {
            ottenuto = service.wikiTitleNatiGiorno(giorno);
            System.out.println(String.format("%s%s%s", giorno, FORWARD, ottenuto));
        }
    }

    @Test
    @Order(3)
    @DisplayName("3 - Nati nel")
    void nati() {
        for (int k = 1; k <= 20; k++) {
            ottenuto = service.wikiTitleNatiAnno(String.valueOf(k));
            assertTrue(textService.isValid(ottenuto));
            System.out.println(ottenuto);
        }

        System.out.println(VUOTA);
        for (int k = 75; k <= 95; k++) {
            ottenuto = service.wikiTitleNatiAnno(String.valueOf(k));
            assertTrue(textService.isValid(ottenuto));
            System.out.println(ottenuto);
        }

        System.out.println(VUOTA);
        for (int k = 795; k <= 820; k++) {
            ottenuto = service.wikiTitleNatiAnno(String.valueOf(k));
            assertTrue(textService.isValid(ottenuto));
            System.out.println(ottenuto);
        }

        System.out.println(VUOTA);
        for (int k = 895; k <= 905; k++) {
            ottenuto = service.wikiTitleNatiAnno(String.valueOf(k));
            assertTrue(textService.isValid(ottenuto));
            System.out.println(ottenuto);
        }

    }

    @Test
    @Order(4)
    @DisplayName("4 - Morti nel")
    void morti() {
        for (int k = 1; k <= 100; k++) {
            ottenuto = service.wikiTitleMortiAnno(String.valueOf(k));
            assertTrue(textService.isValid(ottenuto));
            System.out.println(ottenuto);
        }
    }

    @Test
    @Order(5)
    @DisplayName("5 - Link giorno nato")
    void linkGiornoNato() {
        System.out.println("5 - Link giorno nato usato nelle didascalie (prima e dopo)");
        System.out.println(VUOTA);
        sorgente = PAGINA_OTTO;
        bio = queryService.getBio(sorgente);

        previsto = "(n.[[Nati il 20 ottobre|20 ottobre]])";
        ottenuto = service.linkGiornoNato(bio, true);
        assertEquals(previsto, ottenuto);
        System.out.println("Con icona e parentesi (usato in coda alle didascalie)");
        System.out.println(String.format("%s (nato il %s)%s%s", bio.getWikiTitle(), bio.giornoNato, FORWARD, ottenuto));
        System.out.println(VUOTA);

        previsto = "[[Nati il 20 ottobre|20 ottobre]]";
        ottenuto = service.linkGiornoNato(bio, false);
        assertEquals(previsto, ottenuto);
        System.out.println("Senza icona e senza parentesi (usato prima della didascalia in anni)");
        System.out.println(String.format("%s (nato il %s)%s%s", bio.getWikiTitle(), bio.giornoNato, FORWARD, ottenuto));
    }

    @Test
    @Order(7)
    @DisplayName("7 - Link anno nato")
    void linkAnnoNato() {
        System.out.println("7 - Link anno nato usato nelle didascalie (prima e dopo)");
        System.out.println(VUOTA);
        sorgente = PAGINA_OTTO;
        bio = queryService.getBio(sorgente);

        previsto = "(n.[[Nati nel 1792|1792]])";
        ottenuto = service.linkAnnoNato(bio, true);
        assertEquals(previsto, ottenuto);
        System.out.println("Con icona e parentesi (usato in coda alle didascalie)");
        System.out.println(String.format("%s (nato nel %s)%s%s", bio.getWikiTitle(), bio.annoNato, FORWARD, ottenuto));
        System.out.println(VUOTA);

        previsto = "[[Nati nel 1792|1792]]";
        ottenuto = service.linkAnnoNato(bio, false);
        assertEquals(previsto, ottenuto);
        System.out.println("Senza icona e senza parentesi (usato prima della didascalia in giorni)");
        System.out.println(String.format("%s (nato nel %s)%s%s", bio.getWikiTitle(), bio.annoNato, FORWARD, ottenuto));
    }


    @Test
    @Order(8)
    @DisplayName("8 - Link anno morto")
    void linkAnnoMorto() {
        System.out.println("8 - Link anno morto usato nelle didascalie (prima e dopo)");
        System.out.println(VUOTA);
        sorgente = PAGINA_OTTO;
        bio = queryService.getBio(sorgente);

        previsto = "(†[[Morti nel 1863|1863]])";
        ottenuto = service.linkAnnoMorto(bio, true);
        assertEquals(previsto, ottenuto);
        System.out.println("Con icona e parentesi (usato in coda alle didascalie)");
        System.out.println(String.format("%s (morto nel %s)%s%s", bio.getWikiTitle(), bio.annoMorto, FORWARD, ottenuto));
        System.out.println(VUOTA);

        previsto = "[[Morti nel 1863|1863]]";
        ottenuto = service.linkAnnoMorto(bio, false);
        assertEquals(previsto, ottenuto);
        System.out.println("Senza icona e senza parentesi (usato prima della didascalia in giorni)");
        System.out.println(String.format("%s (morto nel %s)%s%s", bio.getWikiTitle(), bio.annoMorto, FORWARD, ottenuto));
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
