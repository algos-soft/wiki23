package it.algos.unit.wiki;

import it.algos.*;
import it.algos.test.*;
import it.algos.vaad23.backend.boot.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.packages.crono.anno.*;
import it.algos.vaad23.backend.packages.crono.giorno.*;
import it.algos.wiki23.backend.packages.attivita.*;
import it.algos.wiki23.backend.packages.nazionalita.*;
import it.algos.wiki23.backend.service.*;
import org.junit.jupiter.api.*;
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
@DisplayName("ElaboraService - Elaborazione delle biografie.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ElaboraServiceTest extends ATest {

    protected static final String NOME_1 = "";

    protected static final String NOME_2 = "Marcello <ref>Da levare</ref>";

    protected static final String NOME_3 = "Antonio [html:pippoz]";

    protected static final String NOME_4 = "Roberto Marco Maria";

    protected static final String NOME_5 = "Colin Campbell (generale)";

    protected static final String NOME_6 = "Giovan Battista";

    protected static final String NOME_7 = "Anna Maria";

    protected static final String NOME_8 = "testo errato";

    protected static final String NOME_9 = "[[Roberto]]";

    protected static final String COGNOME_1 = "";

    protected static final String COGNOME_2 = "Brambilla <ref>Da levare</ref>";

    protected static final String COGNOME_3 = "Rossi [html:pippoz]";

    protected static final String COGNOME_4 = "Bayley";

    protected static final String COGNOME_5 = "Mora Porras";

    protected static final String COGNOME_6 = "Ørsted";

    protected static final String COGNOME_7 = "de Bruillard";

    protected static final String COGNOME_8 = "testo errato";

    protected static final String COGNOME_9 = "[[Rossi]]";

    protected static final String GIORNO_1 = "";

    protected static final String GIORNO_2 = "31 febbraio";

    protected static final String GIORNO_3 = "4 termidoro";

    protected static final String GIORNO_4 = "17 marzo";

    protected static final String GIORNO_5 = "testo errato";

    protected static final String GIORNO_6 = "12 [[Luglio]] <ref>Da levare</ref>";

    protected static final String GIORNO_7 = "24aprile";

    protected static final String GIORNO_8 = "2 Novembre";

    protected static final String GIORNO_9 = "2Novembre";

    protected static final String GIORNO_10 = "?";

    protected static final String GIORNO_11 = "3 dicembre?";

    protected static final String GIORNO_12 = "3 dicembre circa";

    protected static final String GIORNO_13 = "[[8 agosto]]";

    protected static final String GIORNO_14 = "21[Maggio]";

    protected static final String GIORNO_15 = "[4 febbraio]";

    protected static final String GIORNO_16 = "settembre 5";

    protected static final String GIORNO_17 = "27 ottobre <!--eh eh eh-->";

    protected static final String GIORNO_18 = "29 giugno <nowiki> levare";

    protected static final String GIORNO_19 = "dicembre";

    protected static final String GIORNO_20 = "12/5";

    protected static final String GIORNO_21 = "12-5";

    protected static final String ANNO_1 = "";

    protected static final String ANNO_2 = "3145";

    protected static final String ANNO_3 = "1874";

    protected static final String ANNO_4 = "testo errato";

    protected static final String ANNO_5 = "[[1954]]";

    protected static final String ANNO_6 = "1512?";

    protected static final String ANNO_7 = "?";

    protected static final String ANNO_8 = "1649 circa";

    protected static final String ANNO_9 = "1649 <ref>Da levare</ref>";

    protected static final String ANNO_10 = "754 a.C.";

    protected static final String ANNO_11 = "754 a.c.";

    protected static final String ANNO_12 = "754a.c.";

    protected static final String ANNO_13 = "754a.C.";

    protected static final String ANNO_14 = "754 A.C.";

    protected static final String ANNO_15 = "754 AC";

    protected static final String ANNO_16 = "754 ac";

    protected static final String ANNO_17 = "novecento";

    protected static final String ANNO_18 = "3 secolo";

    protected static final String ANNO_19 = "1532/1537";

    protected static final String ANNO_20 = "754 a.C. circa";

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

    public static String[] NOMI() {
        return new String[]{NOME_1, NOME_2, NOME_3, NOME_4, NOME_5, NOME_6, NOME_7, NOME_8, NOME_9};
    }

    public static String[] COGNOMI() {
        return new String[]{COGNOME_1, COGNOME_2, COGNOME_3, COGNOME_4, COGNOME_5, COGNOME_6, COGNOME_7, COGNOME_8, COGNOME_9};
    }

    public static String[] GIORNI() {
        return new String[]{
                GIORNO_1, GIORNO_2, GIORNO_3, GIORNO_4, GIORNO_5, GIORNO_6, GIORNO_7,
                GIORNO_8, GIORNO_9, GIORNO_10, GIORNO_11, GIORNO_12, GIORNO_13, GIORNO_14,
                GIORNO_15, GIORNO_16, GIORNO_17, GIORNO_18, GIORNO_19, GIORNO_20, GIORNO_21};
    }

    public static String[] ANNI() {
        return new String[]{
                ANNO_1, ANNO_2, ANNO_3, ANNO_4, ANNO_5, ANNO_6, ANNO_7, ANNO_8, ANNO_9, ANNO_10,
                ANNO_11, ANNO_12, ANNO_13, ANNO_14, ANNO_15, ANNO_16, ANNO_17, ANNO_18, ANNO_19, ANNO_20};
    }

    public static String[] ATTIVITA_ELABORA() {
        return new String[]{ATT_1, ATT_2, ATT_3, ATT_4, ATT_5, ATT_6, ATT_7, ATT_8, ATT_9, ATT_10, ATT_11, ATT_12, ATT_13};
    }

    public static String[] NAZIONALITA() {
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
//        service = elaboraService;

//        service.mongo = mongoService;
//        service.text = textService;
//        service.annotation = annotationService;
//        service.wikiBotService = wikiBotService;
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
    void testWithStringParameterNome(String nome) {
        System.out.println("1 - fixNome (come stringa)");

        sorgente = nome;
        ottenuto = service.fixNome(sorgente);
        printNome(sorgente, ottenuto);
    }


    @ParameterizedTest
    @MethodSource(value = "COGNOMI")
    @Order(2)
    @DisplayName("2 - fixCognome (come stringa)")
    void testWithStringParameterCognome(String cognome) {
        System.out.println("2 - fixCognome (come stringa)");

        sorgente = cognome;
        ottenuto = service.fixCognome(sorgente);
        printNome(sorgente, ottenuto);
    }

    @Test
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
    void testWithStringParameterGiorno(String giorno) {
        System.out.println("4 - fixGiorno (come stringa)");

        sorgente = giorno;
        ottenuto = service.fixGiorno(sorgente);
        printNome(sorgente, ottenuto);
    }


    @ParameterizedTest
    @MethodSource(value = "GIORNI")
    @Order(5)
    @DisplayName("5 - fixGiornoLink (come Giorno esistente)")
    void testWithStringParameterGiornoLink(String giorno) {
        System.out.println("5 - fixGiornoLink (come Giorno esistente)");

        sorgente = giorno;
        ottenutoGiorno = null;
        try {
//            ottenutoGiorno = service.fixGiornoLink(sorgente);
        } catch (Exception unErrore) {
        }
        printGiorno(sorgente, ottenutoGiorno);
    }

    @ParameterizedTest
    @MethodSource(value = "GIORNI")
    @Order(6)
    @DisplayName("6 - fixGiornoValido (come stringa)")
    void testWithStringParameterGiornoValido(String giorno) {
        System.out.println("6 - fixGiornoValido (come stringa)");

        sorgente = giorno;
        ottenuto = service.fixGiornoValido(sorgente);
        printNome(sorgente, ottenuto);
    }


    @ParameterizedTest
    @MethodSource(value = "ANNI")
    @Order(7)
    @DisplayName("7 - fixAnno (come stringa)")
    void testWithStringParameterAnno(String anno) {
        System.out.println("7 - fixAnno (come stringa)");

        sorgente = anno;
        ottenuto = service.fixAnno(sorgente);
        printNome(sorgente, ottenuto);

    }


    @ParameterizedTest
    @MethodSource(value = "ANNI")
    @Order(8)
    @DisplayName("8 - fixAnnoLink (come Anno esistente)")
    void testWithStringParameterAnnoLink(String anno) {
        System.out.println("8 - fixAnnoLink (come Anno esistente)");

        sorgente = anno;
        ottenutoAnno = null;
        try {
//            ottenutoAnno = service.fixAnnoLink(sorgente);
        } catch (Exception unErrore) {
        }
        printAnno(sorgente, ottenutoAnno);
    }


    @ParameterizedTest
    @MethodSource(value = "ANNI")
    @Order(9)
    @DisplayName("9 - fixAnnoValido (come stringa)")
    void testWithStringParameterAnnoValido(String anno) {
        System.out.println("9 - fixAnnoValido (come stringa)");

        sorgente = anno;
        ottenuto = service.fixAnno(sorgente);
        printNome(sorgente, ottenuto);
    }

    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_ELABORA")
    @Order(10)
    @DisplayName("10 - fixAttivita (come stringa)")
    void testWithStringParameterAttivita(String attivita) {
        System.out.println("10 - fixAttivita (come stringa)");

        sorgente = attivita;
        ottenuto = service.fixAttivita(sorgente);
        printNome(sorgente, ottenuto);
    }


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_ELABORA")
    @Order(11)
    @DisplayName("11 - fixAttivitaLink (come Attivita esistente)")
    void testWithStringParameterAttivitaLink(String attivita) {
        System.out.println("11 - fixAttivitaLink (come Attivita esistente)");

        sorgente = attivita;
        ottenutoAttivita = null;
        try {
//            ottenutoAttivita = service.fixAttivita(sorgente);
        } catch (Exception unErrore) {
        }
        printAttivita(sorgente, ottenutoAttivita);
    }


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_ELABORA")
    @Order(12)
    @DisplayName("12 - fixAttivitaValida (come stringa)")
    void testWithStringParameterAttivitaValida(String attivita) {
        System.out.println("12 - fixAttivitaValida (come stringa)");

        sorgente = attivita;
        ottenuto = service.fixAttivitaValida(sorgente);
        printNome(sorgente, ottenuto);
    }


    @ParameterizedTest
    @MethodSource(value = "NAZIONALITA")
    @Order(13)
    @DisplayName("13 - fixNazionalita (come stringa)")
    void testWithStringParameterNazionalita(String nazionalita) {
        System.out.println("13 - fixNazionalita (come stringa)");

        sorgente = nazionalita;
        ottenuto = service.fixNazionalita(sorgente);
        printNome(sorgente, ottenuto);
    }


    @ParameterizedTest
    @MethodSource(value = "NAZIONALITA")
    @Order(14)
    @DisplayName("14 - fixNazionalitaLink (come Nazionalita esistente)")
    void testWithStringParameterNazionalitaLink(String nazionalita) {
        System.out.println("14 - fixNazionalitaLink (come Nazionalita esistente)");

        sorgente = nazionalita;
        ottenutoNazionalita = null;
        try {
//            ottenutoNazionalita = service.fixNazionalitaLink(sorgente);
        } catch (Exception unErrore) {
        }
        printNazionalita(sorgente, ottenutoNazionalita);
    }


    @ParameterizedTest
    @MethodSource(value = "NAZIONALITA")
    @Order(15)
    @DisplayName("15 - fixNazionalitaValida (come stringa)")
    void testWithStringParameterNazionalitaValida(String nazionalita) {
        System.out.println("15 - fixNazionalitaValida (come stringa)");

        sorgente = nazionalita;
        ottenuto = service.fixNazionalitaValida(sorgente);
        printNome(sorgente, ottenuto);
    }



    private void printNome(final String tagInput, final String tagValido) {
        System.out.println(String.format("'%s' -> '%s'", tagInput, tagValido));
        System.out.println(VUOTA);
    }

    private void printSesso(final String tagInput, final String tagValido) {
        System.out.println(String.format("'%s' -> '%s'", tagInput, tagValido));
        System.out.println(VUOTA);
    }

    private void printGiorno(final String tagInput, final Giorno giornoValido) {
        if (giornoValido != null) {
            System.out.println(String.format("'%s' -> '%s'", tagInput, giornoValido.nome));
            System.out.println(VUOTA);
        }
        else {
            System.out.println(String.format("Non esiste un giorno corrispondente a '%s'", tagInput));
            System.out.println(VUOTA);
        }
    }

    private void printAnno(final String tagInput, final Anno annoValido) {
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