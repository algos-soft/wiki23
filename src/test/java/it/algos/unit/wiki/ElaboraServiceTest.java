package it.algos.unit.wiki;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.packages.crono.anno.*;
import it.algos.vaad23.backend.packages.crono.giorno.*;
import it.algos.wiki23.backend.packages.anno.*;
import it.algos.wiki23.backend.packages.attivita.*;
import it.algos.wiki23.backend.packages.giorno.*;
import it.algos.wiki23.backend.packages.nazionalita.*;
import it.algos.wiki23.backend.service.*;
import it.algos.wiki23.wiki.query.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.springframework.boot.test.context.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 31-lug-2021
 * Time: 17:09
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("wiki")
@DisplayName("ElaboraService - Elaborazione delle biografie.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ElaboraServiceTest extends WikiTest {




    protected static final String ATT_1 = "";

    protected static final String ATT_2 = "errata";

    protected static final String ATT_3 = "medico";

    protected static final String ATT_4 = "badessa";

    protected static final String ATT_5 = "Medico";

    protected static final String ATT_6 = "ex ciclista";

    protected static final String ATT_7 = "ex medico";

    protected static final String ATT_8 = "medico <ref>Da levare</ref>";

    protected static final String ATT_9 = "[[medico]]";

    protected static final String ATT_10 = "medico<!--eh eh eh-->";

    protected static final String ATT_11 = "professoressa universitaria";

    protected static final String ATT_12 = "ex [[ciclista]]";

    protected static final String ATT_13 = "ex Ciclista";

    protected static final String NAZ_1 = "";

    protected static final String NAZ_2 = "errata";

    protected static final String NAZ_3 = "giapponese";

    protected static final String NAZ_4 = "Giapponese";

    protected static final String NAZ_5 = "Giapponese<!--eh eh eh-->";

    protected static final String NAZ_6 = "Giapponese<ref>Da levare</ref>";

    protected static final String NAZ_7 = "[[Giapponese]]";

    protected static final String NAZ_8 = "[[Giappone]]";

    protected static final String NAZ_9 = "italiano";

    protected static final String NAZ_10 = "italiana";

    protected static final String NAZ_11 = "ceca";

    protected Giorno previstoGiorno;

    protected Giorno ottenutoGiorno;

    protected Anno previstoAnno;

    protected Anno ottenutoAnno;

    protected Attivita ottenutoAttivita;

    protected Nazionalita ottenutoNazionalita;

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    @InjectMocks
    private ElaboraService service;



    public static String[] ATTIVITA_ELABORA() {
        return new String[]{ATT_1, ATT_2, ATT_3, ATT_4, ATT_5, ATT_6, ATT_7, ATT_8, ATT_9, ATT_10, ATT_11, ATT_12, ATT_13};
    }

    public static String[] NAZIONALITA_ELABORA() {
        return new String[]{NAZ_1, NAZ_2, NAZ_3, NAZ_4, NAZ_5, NAZ_6, NAZ_7, NAZ_8, NAZ_9, NAZ_10};
    }


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();

        //--reindirizzo l'istanza della superclasse
        service = elaboraService;
    }


    /**
     * Qui passa a ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    protected void setUpEach() {
        super.setUpEach();

        previstoGiorno = null;
        previstoAnno = null;
    }


    @ParameterizedTest
    @MethodSource(value = "NOMI")
    @Order(1)
    @DisplayName("1 - fixNome (come stringa)")
        //--valore grezzo
        //--valore valido
    void fixNome(String grezzo, String valido) {
        System.out.println("1 - fixNome (come stringa)");

        sorgente = grezzo;
        previsto = valido;
        ottenuto = service.fixNome(sorgente);
        assertEquals(previsto, ottenuto);
        printNome(sorgente, ottenuto);
    }


    @ParameterizedTest
    @MethodSource(value = "COGNOMI")
    @Order(2)
    @DisplayName("2 - fixCognome (come stringa)")
        //--valore grezzo
        //--valore valido
    void fixCognome(String grezzo, String valido) {
        System.out.println("2 - fixCognome (come stringa)");

        sorgente = grezzo;
        previsto = valido;
        ottenuto = service.fixCognome(sorgente);
        assertEquals(previsto, ottenuto);
        printNome(sorgente, ottenuto);
    }

//    @Test
    @Order(3)
    @DisplayName("3 - fixSesso (come stringa)")
    void fixSesso() {
        System.out.println("3 - fixSesso (come stringa)");
        System.out.println(VUOTA);

        System.out.println("**********");
        for (String tagInput : MASCHI) {
            ottenuto = service.fixSesso(tagInput);
            printSesso(tagInput, ottenuto);
        }

        System.out.println("**********");
        for (String tagInput : FEMMINE) {
            ottenuto = service.fixSesso(tagInput);
            printSesso(tagInput, ottenuto);
        }

        System.out.println("**********");
        for (String tagInput : TRANS) {
            ottenuto = service.fixSesso(tagInput);
            printSesso(tagInput, ottenuto);
        }
    }


    @ParameterizedTest
    @MethodSource(value = "GIORNI")
    @Order(4)
    @DisplayName("4 - fixGiorno (come stringa)")
    void fixGiorno(String grezzo, String valido) {
        System.out.println("4 - fixGiorno (come stringa)");

        sorgente = grezzo;
        previsto = valido;
        ottenuto = service.fixGiorno(sorgente);
        assertEquals(previsto, ottenuto);
        printNome(sorgente, ottenuto);
    }


//    @ParameterizedTest
    @MethodSource(value = "GIORNI")
    @Order(5)
    @DisplayName("5 - fixGiornoLink (come Giorno esistente)")
    void fixGiornoLink(String giorno) {
        System.out.println("5 - fixGiornoLink (come Giorno esistente)");

        sorgente = giorno;
        GiornoWiki ottenutoGiorno = null;
        try {
            ottenutoGiorno = service.fixGiornoLink(sorgente);
        } catch (Exception unErrore) {
        }
        printGiorno(sorgente, ottenutoGiorno);
    }

    @ParameterizedTest
    @MethodSource(value = "GIORNI")
    @Order(6)
    @DisplayName("6 - fixGiornoValido (come stringa)")
    void fixGiornoValido(String giorno) {
        System.out.println("6 - fixGiornoValido (come stringa)");

        sorgente = giorno;
        ottenuto = service.fixGiornoValido(sorgente);
        printNome(sorgente, ottenuto);
    }


    @ParameterizedTest
    @MethodSource(value = "ANNI")
    @Order(7)
    @DisplayName("7 - fixAnno (come stringa)")
    void fixAnno(String grezzo, String valido) {
        System.out.println("7 - fixAnno (come stringa)");

        sorgente = grezzo;
        previsto = valido;
        ottenuto = service.fixAnno(sorgente);
        assertEquals(previsto, ottenuto);
        printNome(sorgente, ottenuto);
    }


    @ParameterizedTest
    @MethodSource(value = "ANNI")
    @Order(8)
    @DisplayName("8 - fixAnnoLink (come Anno esistente)")
    void fixAnnoLink(String anno) {
        System.out.println("8 - fixAnnoLink (come Anno esistente)");

        sorgente = anno;
        AnnoWiki ottenutoAnno = null;
        try {
            ottenutoAnno = service.fixAnnoLink(sorgente);
        } catch (Exception unErrore) {
        }
        printAnno(sorgente, ottenutoAnno);
    }

    //    @ParameterizedTest
    //    @MethodSource(value = "ANNI")
    //    @Order(9)
    //    @DisplayName("9 - fixAnnoValido (come stringa)")
    //    void fixAnno(String anno) {
    //        System.out.println("9 - fixAnnoValido (come stringa)");
    //
    //        sorgente = anno;
    //        ottenuto = service.fixAnno(sorgente);
    //        printNome(sorgente, ottenuto);
    //    }

    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_ELABORA")
    @Order(10)
    @DisplayName("10 - fixAttivita (come stringa)")
    void fixAttivita(String attivita) {
        System.out.println("10 - fixAttivita (come stringa)");

        sorgente = attivita;
        ottenuto = service.fixAttivita(sorgente);
        printNome(sorgente, ottenuto);
    }

    //    @ParameterizedTest
    //    @MethodSource(value = "ATTIVITA_ELABORA")
    //    @Order(11)
    //    @DisplayName("11 - fixAttivitaLink (come Attivita esistente)")
    //    void testWithStringParameterAttivitaLink(String attivita) {
    //        System.out.println("11 - fixAttivitaLink (come Attivita esistente)");
    //
    //        sorgente = attivita;
    //        ottenutoAttivita = null;
    //        try {
    //            ottenutoAttivita = service.fixAttivitaLink(sorgente);
    //        } catch (Exception unErrore) {
    //        }
    //        printAttivita(sorgente, ottenutoAttivita);
    //    }


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_ELABORA")
    @Order(12)
    @DisplayName("12 - fixAttivitaValida (come stringa)")
    void fixAttivitaValida(String attivita) {
        System.out.println("12 - fixAttivitaValida (come stringa)");

        sorgente = attivita;
        ottenuto = service.fixAttivitaValida(sorgente);
        printNome(sorgente, ottenuto);
    }


    @ParameterizedTest
    @MethodSource(value = "NAZIONALITA_ELABORA")
    @Order(13)
    @DisplayName("13 - fixNazionalita (come stringa)")
    void fixNazionalita(String nazionalita) {
        System.out.println("13 - fixNazionalita (come stringa)");

        sorgente = nazionalita;
        ottenuto = service.fixNazionalita(sorgente);
        printNome(sorgente, ottenuto);
    }


    @ParameterizedTest
    @MethodSource(value = "NAZIONALITA_ELABORA")
    @Order(14)
    @DisplayName("14 - fixNazionalitaLink (come Nazionalita esistente)")
    void fixNazionalitaLink(String nazionalita) {
        System.out.println("14 - fixNazionalitaLink (come Nazionalita esistente)");

        sorgente = nazionalita;
        ottenutoNazionalita = null;
        try {
            ottenutoNazionalita = service.fixNazionalitaLink(sorgente);
        } catch (Exception unErrore) {
        }
        printNazionalita(sorgente, ottenutoNazionalita);
    }


    @ParameterizedTest
    @MethodSource(value = "NAZIONALITA_ELABORA")
    @Order(15)
    @DisplayName("15 - fixNazionalitaValida (come stringa)")
    void fixNazionalitaValida(String nazionalita) {
        System.out.println("15 - fixNazionalitaValida (come stringa)");

        sorgente = nazionalita;
        ottenuto = service.fixNazionalitaValida(sorgente);
        printNome(sorgente, ottenuto);
    }

    @Test
    @Order(16)
    @DisplayName("16 - nazionalità maschile e femminile")
    void ciclo() {
        System.out.println("16 - nazionalità maschile e femminile");
        System.out.println(VUOTA);

        sorgente = "Karel Škorpil";
        bio = queryService.getBio(sorgente);
    }

    @Test
    @Order(17)
    @DisplayName("17 - nazionalità maschile e femminile")
    void ciclo17() {
        System.out.println("17 - nazionalità maschile e femminile");
        System.out.println(VUOTA);

        sorgente = "Laura Mancinelli";
        bio = queryService.getBio(sorgente);
    }

    @Test
    @Order(18)
    @DisplayName("18 - nazionalità maschile e femminile")
    void ciclo18() {
        System.out.println("18 - nazionalità maschile e femminile");
        System.out.println(VUOTA);

        sorgente = "Johann Georg Kastner";
        bio = queryService.getBio(sorgente);
    }


    private void printNome(final String tagInput, final String tagValido) {
        System.out.println(String.format("'%s' -> '%s'", tagInput, tagValido));
        System.out.println(VUOTA);
    }

    private void printSesso(final String tagInput, final String tagValido) {
        System.out.println(String.format("'%s' -> '%s'", tagInput, tagValido));
        System.out.println(VUOTA);
    }

    private void printGiorno(final String tagInput, final GiornoWiki giornoValido) {
        if (giornoValido != null) {
            System.out.println(String.format("'%s' -> '%s'", tagInput, giornoValido.nome));
            System.out.println(VUOTA);
        }
        else {
            System.out.println(String.format("Non esiste un giorno corrispondente a '%s'", tagInput));
            System.out.println(VUOTA);
        }
    }

    private void printAnno(final String tagInput, final AnnoWiki annoValido) {
        if (annoValido != null) {
            System.out.println(String.format("'%s' -> [%s]", tagInput, annoValido.nome));
            System.out.println(VUOTA);
        }
        else {
            System.out.println(String.format("Non esiste un anno corrispondente a '%s'", tagInput));
            System.out.println(VUOTA);
        }
    }

    private void printAttivita(final String tagInput, final Attivita attivitaValida) {
        if (attivitaValida != null) {
            System.out.println(String.format("'%s' -> [%s]", tagInput, attivitaValida.singolare));
            System.out.println(VUOTA);
        }
        else {
            System.out.println(String.format("Non esiste un'attività corrispondente a '%s'", tagInput));
            System.out.println(VUOTA);
        }
    }


    private void printNazionalita(final String tagInput, final Nazionalita nazionalitaValida) {
        if (nazionalitaValida != null) {
            System.out.println(String.format("'%s' -> [%s]", tagInput, nazionalitaValida.singolare));
            System.out.println(VUOTA);
        }
        else {
            System.out.println(String.format("Non esiste una nazionalità corrispondente a '%s'", tagInput));
            System.out.println(VUOTA);
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